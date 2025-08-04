package org.scoula.external.codef.accounts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Log4j2
@Service
public class CodefAccountService {

    @Value("${codef.account.clientID}")
    private String clientID;

    @Value("${codef.account.clientSecret}")
    private String clientSecret;

    @Value("${codef.accessToken.endpoint}")
    private String accessTokenUrl;

    @Value("${codef.connectedID.endpoint}")
    private String connectedIdUrl;

    @Value("${codef.account.endpoint}")
    private String getAccountUrl;

    public String getAccessToken() {

        // HTTP 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth에 맞게 변환
        String auth = clientID + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        // Body 작성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Access Token 요청
        ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);

        // Access Token 추출
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response.getBody(), Map.class);
            return json.get("access_token").toString();
        } catch (Exception e) {
            throw new ServerErrorException("파싱 실패 오류입니다."); // 병현님이 추가해주신 error 메세지 머지되면 수정할 예정입니다.
        }
    }
}
