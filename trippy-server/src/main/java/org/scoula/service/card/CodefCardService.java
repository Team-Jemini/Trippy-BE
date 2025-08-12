package org.scoula.service.card;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scoula.domain.card.CardVO;
import org.scoula.external.codef.card.AccessTokenService;
import org.scoula.external.codef.card.CardListService;
import org.scoula.external.codef.card.ConnectedIdService;
import org.scoula.common.util.RSAEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scoula.common.exception.enums.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodefCardService {

	private final AccessTokenService accessTokenService;
	private final ConnectedIdService connectedIdService;
	private final CardListService cardListService;
	private final CardSaveService cardSaveService;

	@Value("${codef.client-id}")
	private String clientId;

	@Value("${codef.client-secret}")
	private String clientSecret;

	@Value("${codef.public-key}")
	private String publicKey;

	// 카드 1
	@Value("${codef.card-1.login-id}")
	private String loginId1;
	@Value("${codef.card-1.password}")
	private String password1;
	@Value("${codef.card-1.birth-date}")
	private String birthDate1;
	@Value("${codef.card-1.organization}")
	private String organization1; // 예: "0301" (KB)

	// 카드 2
	@Value("${codef.card-2.login-id}")
	private String loginId2;
	@Value("${codef.card-2.password}")
	private String password2;
	@Value("${codef.card-2.birth-date}")
	private String birthDate2;
	@Value("${codef.card-2.organization}")
	private String organization2; // 예: "0306" (신한)

	public void getAllMyCardsAndSave(Long userId, String accountId) {
		String accessToken;
		try {
			accessToken = accessTokenService.getAccessToken(clientId, clientSecret);
		} catch (Exception e) {
			log.error("{}: {}", ErrorCode.ACCESS_TOKEN_FAILED.getMessage(), e.getMessage(), e);
			return;
		}

		// 각 카드사 호출에 재시도(최대 2회) 적용
		processCardDataWithRetry(userId, accountId, loginId1, password1, birthDate1, organization1, accessToken);
		sleepQuiet(600); // 두 호출 사이 약간의 텀 (레이트 제한 완화)
		processCardDataWithRetry(userId, accountId, loginId2, password2, birthDate2, organization2, accessToken);
	}

	private void processCardDataWithRetry(Long userId, String accountId,
		String loginId, String password, String birthDate,
		String organization, String accessToken) {
		int maxAttempts = 2;
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				processCardData(userId, accountId, loginId, password, birthDate, organization, accessToken);
				return; // 성공
			} catch (Exception e) {
				log.warn("processCardData 실패 ({}번째 시도, org={}): {}", attempt, organization, e.getMessage());
				if (attempt < maxAttempts) {
					sleepQuiet(800L * attempt); // 점증 backoff
				}
			}
		}
	}

	private void processCardData(Long userId, String accountId, String loginId, String password, String birthDate,
		String organization, String accessToken) throws Exception {
		String encPw = RSAEncryptor.encrypt(password, publicKey);
		String connectedId = extractConnectedIdFromResponse(
			connectedIdService.createConnectedId(accessToken, encPw, loginId, organization)
		);

		String encodedResponse = cardListService.getCardList(accessToken, connectedId, organization, birthDate);

		// 1) URL 디코딩
		String decoded = URLDecoder.decode(encodedResponse, StandardCharsets.UTF_8);

		// 2) JSON 파싱
		JSONObject json = new JSONObject(decoded);
		Object data = json.get("data");

		// 3) 단일/배열 분기
		if (data instanceof JSONObject) {
			saveCardFromJson((JSONObject) data, userId, accountId, organization);
		} else if (data instanceof JSONArray) {
			JSONArray arr = (JSONArray) data;
			for (int i = 0; i < arr.length(); i++) {
				saveCardFromJson(arr.getJSONObject(i), userId, accountId, organization);
			}
		}
	}

	// organization 코드(예: "0301")를 field에 저장
	private void saveCardFromJson(JSONObject cardJson, Long userId, String accountId, String organization) {
		String cardName = cardJson.optString("resCardName");
		String cardNo = cardJson.optString("resCardNo");
		String imageLink = cardJson.optString("resImageLink");

		Integer fieldCode = toFieldCode(organization); // "0301" -> 301

		CardVO cardVO = CardVO.builder()
			.userId(userId)
			.accountId(accountId)
			.cardName(cardName)
			.cardNumber(cardNo)
			.cardImg(imageLink)
			.field(fieldCode) // ✅ 프론트에서 4자리 문자열로 포맷해서 사용
			.build();

		cardSaveService.saveCard(cardVO);
	}

	private Integer toFieldCode(String organization) {
		if (organization == null) return null;
		try {
			return Integer.valueOf(organization);
		} catch (NumberFormatException e) {
			log.warn("organization 파싱 실패: {} -> null 처리", organization);
			return null;
		}
	}

	private String extractConnectedIdFromResponse(String json) {
		JSONObject obj = new JSONObject(json);
		if (obj.has("data")) {
			JSONObject data = obj.getJSONObject("data");
			if (data.has("connectedId")) {
				return data.getString("connectedId");
			}
		}
		throw new RuntimeException(ErrorCode.CONNECTED_ID_NOT_FOUND_EXCEPTION.getMessage() + ": " + json);
	}

	private void sleepQuiet(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}
}
