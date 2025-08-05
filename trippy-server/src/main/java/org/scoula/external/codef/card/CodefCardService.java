package org.scoula.external.codef.card;

import org.json.JSONObject;
import org.scoula.common.util.RSAEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	@Value("${codef.login-id}")
	private String loginId;

	@Value("${codef.password}")
	private String password;

	@Value("${codef.organization}")
	private String organization;

	@Value("${codef.birth-date}")
	private String birthDate;

	public String getMyCards() throws Exception {
		String accessToken = accessTokenService.getAccessToken(clientId, clientSecret);
		String encryptedPw = RSAEncryptor.encrypt(password, publicKey);
		String connectedIdJson = connectedIdService.createConnectedId(accessToken, encryptedPw, loginId, organization);
		String connectedId = extractConnectedIdFromResponse(connectedIdJson);
		return cardListService.getCardList(accessToken, connectedId, organization, birthDate);
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
