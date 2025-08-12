package org.scoula.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class DiscordNotificationService {

    @Value("${logging.discord.500.webhook-uri}")
    private String webhook5xxUrl;

    @Value("${logging.discord.400.webhook-uri}")
    private String webhook4xxUrl;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 500 에러용 (기존)
    public void sendErrorNotification(String errorMessage, String stackTrace, String requestInfo) {
        try {
            String message = createErrorMessage(errorMessage, stackTrace, requestInfo);
            sendToDiscord(message, webhook5xxUrl, "🚨 서버 에러 발생 🚨");
        } catch (Exception e) {
            log.error("Discord 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    // 400대 에러용 (새로 추가)
    public void send4xxNotification(String errorMessage, String requestInfo, String clientInfo) {
        try {
            if (webhook4xxUrl == null || webhook4xxUrl.isEmpty()) {
                log.debug("400대 에러 웹훅 URL이 설정되지 않음");
                return;
            }

            String message = create4xxMessage(errorMessage, requestInfo, clientInfo);
            sendToDiscord(message, webhook4xxUrl, "👻 클라이언트 에러 발생 👻");
        } catch (Exception e) {
            log.error("Discord 4xx 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    private String create4xxMessage(String errorMessage, String requestInfo, String clientInfo) {
        StringBuilder message = new StringBuilder();
        message.append("**에러 발생 시간:** ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        message.append("**에러 메시지:** ").append(errorMessage != null ? errorMessage : "Unknown Error").append("\n");

        if (requestInfo != null && !requestInfo.isEmpty()) {
            String[] parts = requestInfo.split(" ");
            if (parts.length >= 2) {
                String method = parts[0];
                String uri = parts[1];
                message.append("**요청 메소드:** ").append(method).append("\n");
                message.append("**요청 URI:** ").append(uri).append("\n");
            }
        }

        if (clientInfo != null && !clientInfo.isEmpty()) {
            message.append("**클라이언트 정보:** ").append(clientInfo).append("\n");
        }

        return message.toString();
    }


    private String createErrorMessage(String errorMessage, String stackTrace, String requestInfo) {
        StringBuilder message = new StringBuilder();
        message.append("**에러 발생 시간:** ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        message.append("**에러 메시지:** ").append(errorMessage != null ? errorMessage : "Unknown Error").append("\n");

        if (requestInfo != null && !requestInfo.isEmpty()) {
            // URI와 Method만 추출
            String[] parts = requestInfo.split(" ");
            if (parts.length >= 2) {
                String method = parts[3];  // GET, POST 등
                String uri = parts[1];     // /api/accounts 등
                message.append("**요청 메소드:** ").append(method).append("\n");
                message.append("**요청 URI:** ").append(uri).append("\n");
            }
        }
        
        message.append("```\n");
        message.append(stackTrace != null ? (stackTrace.length() > 1000 ? stackTrace.substring(0, 1000) + "..." : stackTrace) : "No stack trace available");
        message.append("\n```");
        
        return message.toString();
    }

    private void sendToDiscord(String message, String webhookUrl, String title) throws IOException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", message);
        payload.put("username", title);

        String json = objectMapper.writeValueAsString(payload);

        RequestBody body = RequestBody.create(
            json,
            MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(webhookUrl)
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Discord 웹훅 전송 실패. 응답 코드: {}", response.code());
            }
        }
    }
}