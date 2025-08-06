package org.scoula.external.codef.card;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CardListService {
	@Value("${codef.api.card-list.url}")
	private String cardListUrl;

	@Value("${codef.api.content-type}")
	private String contentType;

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

		RequestBody requestBody = RequestBody.create(bodyJson.toString(), MediaType.parse(contentType));

		Request request = new Request.Builder()
			.url(cardListUrl)
			.addHeader("Authorization", "Bearer " + accessToken)
			.addHeader("Content-Type",contentType)
			.post(requestBody)
			.build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
}