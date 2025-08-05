package org.scoula.external.codef.accounts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.external.codef.accounts.dto.ConnectedIdRequestDTO;
import org.scoula.external.codef.accounts.dto.ConnectedIdRequestDTOList;
import org.scoula.mapper.account.AccountMapper;
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
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

import org.scoula.common.util.CodefRsaUtil;

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

    private final AccountMapper accountMapper;

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

    public String getConnectedId() {

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

            if (!"CF-00000".equals(code)) {
                throw new RuntimeException("Connected ID 요청 실패: " + result.get("message"));
            }

            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return data != null && data.get("connectedId") != null
                    ? data.get("connectedId").toString()
                    : null;

        } catch (Exception e) {
            throw new RuntimeException("Connected ID 요청 실패", e); // Todo 병현냥 코드 머지되면 에러 로그 교체해두겠습니다!
        }
    }

    public String getAccountList() {

        // HTTP 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Bearer Access Token 전달
        String accessToken = getAccessToken();
        headers.set("Authorization", "Bearer " + accessToken);

        String connectedId = getConnectedId();

        // Body 설정: organization은 임시로 0004(국민은행)으로 지정
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
            throw new RuntimeException("계좌 목록 조회 실패", e);
        }
    }

    public void saveAccountsToDB(AccountVO request) {
        try {
            String accountListJson = getAccountList();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(accountListJson, Map.class);

            List<Map<String, Object>> accountList = (List<Map<String, Object>>)
                    ((Map<String, Object>) responseMap.get("data")).get("resDepositTrust");

            for (Map<String, Object> account : accountList) {
                String balanceStr = (String) account.get("resAccountBalance");
                Long balance = 0L;
                if (balanceStr != null && !balanceStr.isEmpty()) {
                    balance = Long.parseLong(balanceStr);
                }

                AccountVO vo = AccountVO.builder()
                        .userId(request.getUserId())
                        .accountId((String) account.get("resAccount"))
                        .accountName((String) account.get("resAccountName"))
                        .accountType(AccountType.valueOf("person"))
                        .ownerId(request.getUserId())
                        .balance(balance)
                        .accountCurrency((String) account.get("resAccountCurrency"))
                        .isDeleted(DeletedStatus.N)
                        .build();

                accountMapper.insertAccount(vo);
            }

        } catch (Exception e) {
            throw new RuntimeException("계좌 목록 저장 실패", e);
        }
    }
}
