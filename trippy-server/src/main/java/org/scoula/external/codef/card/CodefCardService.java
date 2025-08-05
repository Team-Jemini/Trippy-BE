package org.scoula.external.codef.card;

import org.json.JSONObject;
import org.scoula.common.util.RSAEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodefCardService {

	private final AccessTokenService accessTokenService = new AccessTokenService();
	private final ConnectedIdService connectedIdService = new ConnectedIdService();
	private final CardListService cardListService = new CardListService();

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

	public List<String> getAllMyCards() throws Exception {
		String accessToken = accessTokenService.getAccessToken(clientId, clientSecret);
		List<String> results = new ArrayList<>();

		// 국민카드 조회
		String encPw1 = RSAEncryptor.encrypt(password1, publicKey);
		String connectedId1 = extractConnectedIdFromResponse(
			connectedIdService.createConnectedId(accessToken, encPw1, loginId1, organization1));
		results.add(cardListService.getCardList(accessToken, connectedId1, organization1, birthDate1));

		// 신한카드 조회
		String encPw2 = RSAEncryptor.encrypt(password2, publicKey);
		String connectedId2 = extractConnectedIdFromResponse(
			connectedIdService.createConnectedId(accessToken, encPw2, loginId2, organization2));
		results.add(cardListService.getCardList(accessToken, connectedId2, organization2, birthDate2));

		return results;
	}

	private String extractConnectedIdFromResponse(String json) {
		if (json == null || !json.trim().startsWith("{")) {
			throw new RuntimeException("CODEF 응답이 JSON이 아닙니다: " + json);
		}
		JSONObject obj = new JSONObject(json);
		if (obj.has("data")) {
			JSONObject data = obj.getJSONObject("data");
			if (data.has("connectedId")) {
				return data.getString("connectedId");
			}
		}
		if (obj.has("result")) {
			JSONObject result = obj.getJSONObject("result");
			String code = result.optString("code");
			String message = result.optString("message");
			throw new RuntimeException("CODEF 응답 에러: " + code + " - " + message);
		}
		throw new RuntimeException("connectedId를 찾을 수 없습니다. 전체 응답: " + json);
	}
}
