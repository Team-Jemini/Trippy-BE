package org.scoula.external.naver.identification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.external.naver.identification.dto.IdCardDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.scoula.common.exception.enums.ErrorCode.*;

@Log4j2
@Service
public class NaverOcrService {

    @Value("${naver.ocr.url}")
    private String url;

    @Value("${naver.ocr.secret}")
    private String secret;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public IdCardDTO callOCRApi(MultipartFile file) {

        RestTemplate restTemplate = new RestTemplate();

        // 공통 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-OCR-SECRET", secret);

        // file 변환
        ByteArrayResource fileResource = getByteArrayResource(file);
        // 헤더 설정
        HttpHeaders filePartHeaders = new HttpHeaders();
        // 업로드 파일 타입 지정 // TODO: 확장자 더 넣기 ex) IMAGE_PNG
        MediaType fileMediaType = MediaType.IMAGE_JPEG;
        if (file.getContentType() != null) {
            try { fileMediaType = MediaType.parseMediaType(file.getContentType()); } catch (Exception ignored) {}
        }
        filePartHeaders.setContentType(fileMediaType);
        HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(fileResource, filePartHeaders);

        // message json 작성
        String messageJson = createMessageJson();
        HttpHeaders msgPartHeaders = new HttpHeaders();
        msgPartHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> msgPart = new HttpEntity<>(messageJson, msgPartHeaders);


        // 바디 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", filePart);
        body.add("message", msgPart);

        // 요청
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, request, byte[].class);

        if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ServerErrorException(INTERNAL_SERVER_EXCEPTION);
        }

        // 디코딩 (UTF-8)
        MediaType ct = response.getHeaders().getContentType();
        Charset cs = (ct != null && ct.getCharset() != null) ? ct.getCharset() : StandardCharsets.UTF_8;
        String responseData = new String(response.getBody(), cs);

        // 파싱 및 정보 추출
        return getParsedIdCard(responseData);
    }

    @NotNull
    private static IdCardDTO getParsedIdCard(String responseData) {
        try {
            JsonNode root = MAPPER.readTree(responseData);

            // 공통 경로 추출
            JsonNode icNode = root.path("images").path(0)
                    .path("idCard").path("result").path("ic");

            String name = icNode.path("name").path(0)
                    .path("formatted").path("value").asText();

            String personalNum = icNode.path("personalNum").path(0)
                    .path("formatted").path("value").asText();

            String address = icNode.path("address").path(0)
                    .path("formatted").path("value").asText();

            JsonNode issueDateNode = icNode.path("issueDate").path(0).path("formatted");
            String issueDate = issueDateNode.path("year").asText() + "-" +
                    issueDateNode.path("month").asText() + "-" +
                    issueDateNode.path("day").asText();

            String authority = icNode.path("authority").path(0)
                    .path("formatted").path("value").asText();

            return new IdCardDTO(name, personalNum, address, issueDate, authority);
        } catch (JsonProcessingException e) {
            throw new ServerErrorException(PARSING_FAIL_EXCEPTION);
        }
    }

    @NotNull
    private static ByteArrayResource getByteArrayResource(MultipartFile file) {
        try {
            return new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        }catch (IOException e) {
            throw new ServerErrorException(FILE_PROCESSING_EXCEPTION);
        }
    }

    private static String createMessageJson() {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("version", "V2");
        messageMap.put("requestId", "1234");
        messageMap.put("timestamp", System.currentTimeMillis());
        messageMap.put("lang", "ko");

        List<Map<String, String>> images = new ArrayList<>();
        Map<String, String> img = new HashMap<>();
        img.put("format", "jpg");
        img.put("name", "id-card");
        images.add(img);

        messageMap.put("images", images);

        try {
            return MAPPER.writeValueAsString(messageMap);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
