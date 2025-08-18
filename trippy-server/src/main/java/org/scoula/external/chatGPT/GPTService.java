// org.scoula.external.chatGPT.GPTService
package org.scoula.external.chatGPT;

import com.google.gson.*;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GPTService {

	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final String MODEL = "gpt-4o-mini"; // 저렴 & 충분

	@Value("${openai.api.key}")
	private String apiKey;

	@Value("${openai.api.base:https://api.openai.com/v1}")
	private String apiBase;

	private final OkHttpClient http = new OkHttpClient.Builder().callTimeout(25, TimeUnit.SECONDS).build();

	/**
	 * title만으로 고정 카테고리 분류.
	 * 실패/이상응답 시 "OTHER" 반환.
	 */
	public String suggestCategory(String title) {
		String system = """
			You are a strict text classifier.
			- Task: Assign one category for a given transaction title.
			- Categories:
			  * FOOD: 음식, 카페, 식당, 음료, 레스토랑
			  * TRANSPORT: 택시, 버스, 지하철, 항공, Uber, 교통
			  * SHOPPING: 마트, 편의점, 쇼핑몰, 상점
			  * ACCOMMODATION: 호텔, 숙박, 에어비앤비
			  * ACTIVITY: 관광, 티켓, 액티비티, 공연, 체험
			  * OTHER: 위에 속하지 않거나 애매하면 반드시 OTHER
			- Rules:
			  * Use ONLY one of [FOOD, TRANSPORT, SHOPPING, ACCOMMODATION, ACTIVITY, OTHER].
			  * Output MUST be JSON only. No explanation. No extra text.
			  * If unclear → return {"category":"OTHER"}.
			  Examples:
			  "스타벅스 아이스 아메리카노" → {"category":"FOOD"}
			  "우버 라과디아 → 맨해튼" → {"category":"TRANSPORT"}
			  "7-Eleven 결제" → {"category":"SHOPPING"}
			  "신라호텔 1박" → {"category":"ACCOMMODATION"}
			  "박물관 입장권" → {"category":"ACTIVITY"}
			  "테스트 결제 123" → {"category":"OTHER"}
			""";

		String user = String.format("""
			Classify the following transaction:
			
			title: "%s"
			
			Return ONLY in JSON form:
			{"category":"<ONE_OF [FOOD|TRANSPORT|SHOPPING|ACCOMMODATION|ACTIVITY|OTHER]"}
			""", title);

		// Structured Outputs 유사하게 강제: function-like JSON schema
		JsonObject schema = new JsonObject();
		schema.addProperty("type", "object");
		JsonObject props = new JsonObject();
		JsonObject category = new JsonObject();
		category.addProperty("type", "string");
		JsonArray enums = new JsonArray();
		for (String v : new String[] {"FOOD", "TRANSPORT", "SHOPPING", "ACCOMMODATION", "ACTIVITY", "OTHER"}) {
			enums.add(v);
		}
		category.add("enum", enums);
		props.add("category", category);
		schema.add("properties", props);
		JsonArray required = new JsonArray();
		required.add("category");
		schema.add("required", required);

		JsonObject responseFormat = new JsonObject();
		responseFormat.addProperty("type", "json_schema");
		responseFormat.addProperty("name", "TxCategory");
		responseFormat.add("json_schema", schema);

		JsonObject body = new JsonObject();
		body.addProperty("model", MODEL);
		JsonArray input = new JsonArray();
		JsonObject msgSystem = new JsonObject();
		msgSystem.addProperty("role", "system");
		msgSystem.addProperty("content", system);
		JsonObject msgUser = new JsonObject();
		msgUser.addProperty("role", "user");
		msgUser.addProperty("content", user);
		input.add(msgSystem);
		input.add(msgUser);
		body.add("input", input);
		body.add("response_format", responseFormat);

		Request request = new Request.Builder().url(apiBase + "/responses")
			.addHeader("Authorization", "Bearer " + apiKey)
			.post(RequestBody.create(body.toString(), JSON))
			.build();

		try (Response resp = http.newCall(request).execute()) {
			if (!resp.isSuccessful() || resp.body() == null)
				return "OTHER";
			String s = resp.body().string();
			// Responses API: output[0].content[0].text 또는 structured JSON
			JsonObject root = JsonParser.parseString(s).getAsJsonObject();
			String outText = extractText(root);
			if (outText == null || outText.isBlank())
				return "OTHER";
			// JSON 파싱 시도
			try {
				JsonObject jobj = JsonParser.parseString(outText.trim()).getAsJsonObject();
				String cat = jobj.get("category").getAsString().toUpperCase();
				return normalize(cat);
			} catch (Exception ignore) {
				// 만약 평문이면 안전 필터
				return normalize(outText.trim().toUpperCase());
			}
		} catch (IOException e) {
			return "OTHER";
		}
	}

	private static String extractText(JsonObject root) {
		// Responses API 표준 필드: output_text가 있으면 우선
		if (root.has("output_text"))
			return root.get("output_text").getAsString();
		// fallback: output -> [ { content: [ { type:"output_text", text:"..." } ] } ]
		try {
			JsonArray out = root.getAsJsonArray("output");
			JsonObject first = out.get(0).getAsJsonObject();
			JsonArray content = first.getAsJsonArray("content");
			for (var el : content) {
				JsonObject c = el.getAsJsonObject();
				if (c.has("text"))
					return c.get("text").getAsString();
			}
		} catch (Exception ignore) {
		}
		return null;
	}

	private static String normalize(String x) {
		return switch (x) {
			case "FOOD", "TRANSPORT", "SHOPPING", "ACCOMMODATION", "ACTIVITY", "OTHER" -> x;
			default -> "OTHER";
		};
	}
}