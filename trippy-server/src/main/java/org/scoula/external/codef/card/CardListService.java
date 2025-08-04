package org.scoula.external.codef.card;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CardListService {

	public String getCardList(String accessToken, String connectedId, String organization, String birthDate) throws Exception {
		OkHttpClient client = new OkHttpClient();

		JSONObject bodyJson = new JSONObject();
		bodyJson.put("connectedId", connectedId);
		bodyJson.put("organization", organization);
		bodyJson.put("account", "");
		bodyJson.put("birthDate", birthDate);
		bodyJson.put("cardNo", "");
		bodyJson.put("loginType", "1");
		bodyJson.put("inquiryType", "1");

		RequestBody requestBody = RequestBody.create(bodyJson.toString(), MediaType.parse("application/json"));

		Request request = new Request.Builder()
			.url("https://development.codef.io/v1/kr/card/p/account/card-list")
			.addHeader("Authorization", "Bearer " + accessToken)
			.addHeader("Content-Type", "application/json")
			.post(requestBody)
			.build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
}