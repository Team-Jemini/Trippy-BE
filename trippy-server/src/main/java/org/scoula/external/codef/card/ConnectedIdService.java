package org.scoula.external.codef.card;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ConnectedIdService {
	@Value("${codef.api.connected-id.url}")
	private String connectedIdUrl;

	@Value("${codef.api.content-type}")
	private String contentType;

	public String createConnectedId(String accessToken, String encryptedPassword, String loginId, String organization) throws Exception {
		OkHttpClient client = new OkHttpClient();

		// 요청 JSON 만들기
		JSONObject accountInfo = new JSONObject();
		accountInfo.put("countryCode", "KR");
		accountInfo.put("businessType", "CD");
		accountInfo.put("clientType", "P");
		accountInfo.put("organization", organization);
		accountInfo.put("loginType", "1");
		accountInfo.put("id", loginId);
		accountInfo.put("password", encryptedPassword);

		JSONObject root = new JSONObject();
		root.put("accountList", new JSONArray().put(accountInfo));

		RequestBody requestBody = RequestBody.create(root.toString(), MediaType.parse(contentType));

		Request request = new Request.Builder()
			.url(connectedIdUrl)
			.addHeader("Authorization", "Bearer " + accessToken)
			.addHeader("Content-Type", contentType)
			.post(requestBody)
			.build();

		try (Response response = client.newCall(request).execute()) {
			String rawResponse = response.body().string(); // 인코딩된 응답

			// URL 디코딩
			String decodedResponse = URLDecoder.decode(rawResponse, StandardCharsets.UTF_8);

			return decodedResponse; // 이 값을 CodefCardService 에서 JSONObject로 파싱
		}
	}
}