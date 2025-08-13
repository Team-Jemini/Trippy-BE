package org.scoula.external.sms;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;


@Component
public class DiscordSmsServiceImpl implements SmsService {
	private static final String CONTENT_PROPERTY = "content";

	@Value("${discord.otp.auth-uri}")
	private String discordWebHookUrl;

	private final RestTemplate restTemplate;

	public DiscordSmsServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public boolean sendSms(String to, String verificationCode) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> body = Map.of(CONTENT_PROPERTY, verificationCode);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<String> resp = restTemplate.postForEntity(discordWebHookUrl, entity, String.class);
		return resp.getStatusCode().is2xxSuccessful();
	}
}
