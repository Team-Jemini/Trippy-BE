package org.scoula.external.codef.card;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
@Service
public class ConnectedIdService {

	public String createConnectedId(String accessToken, String encryptedPassword, String loginId, String organization) throws Exception {
		OkHttpClient client = new OkHttpClient();

		// 요청 JSON 만들기
		JSONObject accountInfo = new JSONObject();
		accountInfo.put("countryCode", "KR");
		accountInfo.put("businessType", "CD");
		accountInfo.put("clientType", "P");
		accountInfo.put("organization", organization);  // 🔄 파라미터로 받은 organization
		accountInfo.put("loginType", "1");
		accountInfo.put("id", loginId);
		accountInfo.put("password", encryptedPassword);

		JSONObject root = new JSONObject();
		root.put("accountList", new JSONArray().put(accountInfo));

		RequestBody requestBody = RequestBody.create(root.toString(), MediaType.parse("application/json"));

		Request request = new Request.Builder()
			.url("https://development.codef.io/v1/account/create")
			.addHeader("Authorization", "Bearer " + accessToken)
			.addHeader("Content-Type", "application/json")
			.post(requestBody)
			.build();

		try (Response response = client.newCall(request).execute()) {
			String rawResponse = response.body().string(); // 인코딩된 응답

			// ✅ URL 디코딩
			String decodedResponse = URLDecoder.decode(rawResponse, StandardCharsets.UTF_8);

			// 확인용 로그 출력
			System.out.println("디코딩된 CODEF 응답: " + decodedResponse);

			return decodedResponse; // 이 값을 CodefCardService 에서 JSONObject로 파싱
		}
	}
}