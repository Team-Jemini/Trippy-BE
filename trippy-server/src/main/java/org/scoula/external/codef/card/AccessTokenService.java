package org.scoula.external.codef.card;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

	public String getAccessToken(String clientId, String clientSecret) throws Exception {
		OkHttpClient client = new OkHttpClient();
		String credentials = Credentials.basic(clientId, clientSecret);

		RequestBody formBody = new FormBody.Builder()
			.add("grant_type", "client_credentials")
			.add("client_id", clientId)
			.add("client_secret", clientSecret)
			.add("scope", "read")
			.build();

		Request request = new Request.Builder()
			.url("https://oauth.codef.io/oauth/token")
			.addHeader("Authorization", credentials)
			.addHeader("Content-Type", "application/x-www-form-urlencoded")
			.post(formBody)
			.build();

		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}
}
