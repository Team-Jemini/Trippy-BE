package org.scoula.external.codef.identification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.controller.identification.dto.res.ResidentCardOcrDTO;
import org.scoula.external.codef.identification.dto.OcrResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.scoula.common.exception.enums.ErrorCode.*;

// TODO: 예비 OCR
@Log4j2
@Service
public class OcrService {

    @Value("${codef.token-url}")
    private String tokenUrl;

    @Value("${codef.ocr-url}")
    private String ocrUrl;

    @Value("${codef.client-id}")
    private String clientId;

    @Value("${codef.client-secret}")
    private String clientSecret;


    /***
     * 1. 엑세스 토큰 발급
     * 2. 이미지 → Base64 인코딩 문자열로 변환
     * 3. OCR 요청 JSON BODY 작성
     * 4. HTTP 헤더 설정
     * 5. OCR API 호출
     * 6. 응답 디코딩
     * 7. DTO 생성
     */
    public ResidentCardOcrDTO
    callOCRApi(MultipartFile file) {

        String token = getAccessToken();

        String base64Img = EncodingImageToBase64(file);
        Map<String, Object> param = createJsonBody(base64Img);
        HttpHeaders headers = createJsonHeader(token);
        ResponseEntity<byte[]> resp = callOcrApi(param, headers);

        String decodedJson = decodeResponse(resp);
        OcrResponseDTO OcrDto = getOcrResponse(decodedJson);

        return new ResidentCardOcrDTO(
                OcrDto.resUserName(),
                OcrDto.resIssueDate(),
                OcrDto.resUserIdentity(),
                "인천 광역시 중구 일이삼3로 12, 102동 1302호(오류동, 둠칫빰칫2단지)" // 임시
        );
    }

    private static OcrResponseDTO getOcrResponse(String decodedJson) {
        // JSON 파싱 후 "data"만 추출
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode dataNode = mapper.readTree(decodedJson).get("data");
            return mapper.treeToValue(dataNode, OcrResponseDTO.class);
        } catch(Exception e){
            throw new ServerErrorException(PARSING_FAIL_EXCEPTION);
        }
    }

    private static String decodeResponse(ResponseEntity<byte[]> resp) {
        // byte[] → UTF-8 문자열
        String encoded = new String(resp.getBody(), StandardCharsets.UTF_8);
        // URL 디코딩 -> 실제 JSON 문자열
        String decodedJson = URLDecoder.decode(encoded, StandardCharsets.UTF_8);
        return decodedJson;
    }

    private ResponseEntity<byte[]> callOcrApi(Map<String, Object> param, HttpHeaders headers) {
        RestTemplate rt = new RestTemplate();
        HttpEntity<Map<String,Object>> req = new HttpEntity<>(param, headers);
        ResponseEntity<byte[]> resp = rt.exchange(ocrUrl, HttpMethod.POST, req, byte[].class);
        return resp;
    }

    private static Map<String, Object> createJsonBody(String base64Img) {
        Map<String, Object> param = new HashMap<>();
        param.put("Type",          "0");   // base64 업로드
        param.put("secret_mode",   "0");   // 암호화 안 함
        param.put("IdCard_base64", base64Img);
        param.put("image_return",  "0");
        param.put("image_save",    "0");
        return param;
    }

    private static HttpHeaders createJsonHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.setBearerAuth(token);
        return headers;
    }

    private static String EncodingImageToBase64(MultipartFile file) {

        byte[] imgBytes = readByteByFile(file);
        String base64Img = Base64.getEncoder().encodeToString(imgBytes);
        return base64Img;

    }

    private static byte[] readByteByFile(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ServerErrorException(FILE_PROCESSING_EXCEPTION);
        }
    }

    public String getAccessToken(){

        // 1. HTTP 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth에 맞게 변환
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        // 2. Body 작성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 3. ACCESS 토큰 요청
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

        // 4. ACCESS 토큰 추출
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response.getBody(), Map.class);
            return json.get("access_token").toString();
        } catch (Exception e) {
            throw new ServerErrorException(PARSING_FAIL_EXCEPTION);
        }
    }
}
