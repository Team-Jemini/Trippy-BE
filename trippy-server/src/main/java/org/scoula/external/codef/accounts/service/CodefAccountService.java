package org.scoula.external.codef.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.scoula.external.codef.accounts.dto.ConnectedIdRequestDTO;
import org.scoula.external.codef.accounts.dto.ConnectedIdRequestDTOList;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.common.util.CodefRsaUtil;
import static org.scoula.common.exception.enums.ErrorCode.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

@Log4j2
@Service
@RequiredArgsConstructor
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

    @Value("${codef.account.kbBankID}")
    private String kbBankID;

    @Value("${codef.account.kbBankPW}")
    private String kbBankPW;

    @Value("${codef.account.publicKey}")
    private String publicKey;

    /** Codef AccessToken 발급 */
    private String getAccessToken() {

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
            throw new ServerErrorException(PARSING_FAIL_EXCEPTION);
        }
    }

    /** Codef ConnectedID 발급 */
    private String getConnectedId() {

        // HTTP 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Bearer Access Token 전달
        String accessToken = getAccessToken();
        headers.set("Authorization", "Bearer " + accessToken);

        String rsaPassword = CodefRsaUtil.runEncryption(publicKey, kbBankPW);

        ConnectedIdRequestDTO accountInfo = new ConnectedIdRequestDTO(
                "KR",
                "BK",
                "P",
                "0004",
                "1",
                kbBankID,
                rsaPassword
        );

        ConnectedIdRequestDTOList requestDTOList = new ConnectedIdRequestDTOList(List.of(accountInfo));

        try {
            HttpEntity<ConnectedIdRequestDTOList> request = new HttpEntity<>(requestDTOList, headers);
            ResponseEntity<String> response = restTemplate.exchange(
              connectedIdUrl, HttpMethod.POST, request, String.class
            );

            // URL 인코딩된 Codef 응답을 디코딩
            String decodedBody = URLDecoder.decode(response.getBody(), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseBody = mapper.readValue(decodedBody, Map.class);

            Map<String, Object> result = (Map<String, Object>) responseBody.get("result");
            String code = (String) result.get("code");

            // Codef로부터 ConnectedID 생성 실패 응답을 받은 경우 ("CF-00000": 생성 성공 응답)
            if (!"CF-00000".equals(code)) {
                throw new ServerErrorException(CREATE_CONNECTED_ID_FAILED);
            }

            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return data != null && data.get("connectedId") != null
                    ? data.get("connectedId").toString()
                    : null;

        } catch (Exception e) {
            throw new ServerErrorException(CREATE_CONNECTED_ID_FAILED);
        }
    }

    /** Codef 내 계좌 목록 조회 */
    public String getAccountList() {

        // HTTP 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Bearer Access Token 전달
        String accessToken = getAccessToken();
        headers.set("Authorization", "Bearer " + accessToken);

        String connectedId = getConnectedId();

        // Body 설정: organization 값 임시로 0004(국민은행)으로 지정
        Map<String, String> requestBody = Map.of(
                "organization", "0004",
                "connectedId", connectedId
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    getAccountUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            String decodedBody = URLDecoder.decode(response.getBody(), StandardCharsets.UTF_8);
            return decodedBody;
        } catch (Exception e) {
            throw new ServerErrorException(GET_ACCOUNTS_LIST_FAILED);
        }
    }
}
