package org.scoula.external.codef.card;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scoula.domain.card.CardVO;
import org.scoula.mapper.card.CardMapper;
import org.scoula.external.codef.card.CardSaveService;
import org.scoula.common.util.RSAEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.RequiredArgsConstructor;


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
	private String organization1;

	// 카드 2
	@Value("${codef.card-2.login-id}")
	private String loginId2;
	@Value("${codef.card-2.password}")
	private String password2;
	@Value("${codef.card-2.birth-date}")
	private String birthDate2;
	@Value("${codef.card-2.organization}")
	private String organization2;

	public void getAllMyCardsAndSave(Long userId, String accountId) throws Exception {
		String accessToken = accessTokenService.getAccessToken(clientId, clientSecret);

		// 카드 1
		processCardData(userId, accountId, loginId1, password1, birthDate1, organization1, accessToken);

		// 카드 2
		processCardData(userId, accountId, loginId2, password2, birthDate2, organization2, accessToken);
	}

	private void processCardData(Long userId, String accountId, String loginId, String password, String birthDate,
		String organization, String accessToken) throws Exception {
		String encPw = RSAEncryptor.encrypt(password, publicKey);
		String connectedId = extractConnectedIdFromResponse(
			connectedIdService.createConnectedId(accessToken, encPw, loginId, organization));
		String encodedResponse = cardListService.getCardList(accessToken, connectedId, organization, birthDate);

		// ✅ 1. URL 디코딩
		String decoded = URLDecoder.decode(encodedResponse, StandardCharsets.UTF_8);

		// ✅ 2. JSON 파싱
		JSONObject json = new JSONObject(decoded);
		Object data = json.get("data");

		// ✅ 3. 단일 카드 or 배열 분기처리
		if (data instanceof JSONObject) {
			saveCardFromJson((JSONObject) data, userId, accountId);
		} else if (data instanceof JSONArray) {
			JSONArray arr = (JSONArray) data;
			for (int i = 0; i < arr.length(); i++) {
				saveCardFromJson(arr.getJSONObject(i), userId, accountId);
			}
		}
	}

	private void saveCardFromJson(JSONObject cardJson, Long userId, String accountId) {
		String cardName = cardJson.optString("resCardName");
		String cardNo = cardJson.optString("resCardNo");
		String imageLink = cardJson.optString("resImageLink");

		CardVO cardVO = CardVO.builder()
			.userId(userId)
			.accountId(accountId)
			.cardName(cardName)
			.cardNumber(cardNo)
			.cardImg(imageLink)
			.build();

		cardSaveService.saveCard(cardVO);
	}

	private String extractConnectedIdFromResponse(String json) {
		JSONObject obj = new JSONObject(json);
		if (obj.has("data")) {
			JSONObject data = obj.getJSONObject("data");
			if (data.has("connectedId")) {
				return data.getString("connectedId");
			}
		}
		throw new RuntimeException("connectedId를 찾을 수 없습니다. 응답: " + json);
	}
}
