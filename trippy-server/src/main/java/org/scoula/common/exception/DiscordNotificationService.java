package org.scoula.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

@Service
@Log4j2
@RequiredArgsConstructor
public class DiscordNotificationService {

    @Value("${logging.discord.500.webhook-uri}")
    private String webhook5xxUrl;

    @Value("${logging.discord.400.webhook-uri}")
    private String webhook4xxUrl;

    private final OkHttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 500 에러용 (기존)
    @Async("discordExecutor")
    public void send5xxNotification(String errorMessage, String stackTrace, String requestInfo) {
        try {
            String message = create5xxMessage(errorMessage, stackTrace, requestInfo);
            sendToDiscordAsync(message, webhook5xxUrl, "🚨 서버 에러 발생 🚨");
        } catch (Exception e) {
            log.error("Discord 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    // 400대 에러용 (새로 추가)
    @Async("discordExecutor")
    public void send4xxNotification(String errorMessage, String requestInfo) {
        try {
            if (webhook4xxUrl == null || webhook4xxUrl.isEmpty()) {
                log.debug("400대 에러 웹훅 URL이 설정되지 않음");
                return;
            }

            String message = create4xxMessage(errorMessage, requestInfo);
            sendToDiscordAsync(message, webhook4xxUrl, "👻 클라이언트 에러 발생 👻");
        } catch (Exception e) {
            log.error("Discord 4xx 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    private String create4xxMessage(String errorMessage, String requestInfo) {
        StringBuilder message = new StringBuilder();
        message.append("**에러 발생 시간:** ")
            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .append("\n");
        message.append("**에러 메시지:** ").append(nz(errorMessage, "Unknown Error")).append("\n");

        if (nz(requestInfo, "").contains(" ")) {
            String[] parts = requestInfo.split(" ");
            if (parts.length >= 2) {
                String method = parts[0];
                String uri = parts[1];
                message.append("**요청 메소드:** ").append(method).append("\n");
                message.append("**요청 URI:** ").append(uri).append("\n");
            }
        }

        return message.toString();
    }


    private String create5xxMessage(String errorMessage, String stackTrace, String requestInfo) {
        StringBuilder message = new StringBuilder();
        message.append("**에러 발생 시간:** ")
            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .append("\n");
        message.append("**에러 메시지:** ").append(nz(errorMessage, "Unknown Error")).append("\n");

        if (nz(requestInfo, "").contains(" ")) {
            String[] parts = requestInfo.split(" ");
            if (parts.length >= 2) {
                String method = parts[0];
                String uri = parts[1];
                message.append("**요청 메소드:** ").append(method).append("\n");
                message.append("**요청 URI:** ").append(uri).append("\n");
            }
        }

        message.append("```\n");
        String st = (stackTrace == null) ? "No stack trace available"
            : (stackTrace.length() > 1800 ? stackTrace.substring(0, 1800) + "..." : stackTrace);
        message.append(st).append("\n```");
        return message.toString();
    }


    private void sendToDiscordAsync(String message, String webhookUrl, String title) throws IOException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", message);
        payload.put("username", title);

        String json = objectMapper.writeValueAsString(payload);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(webhookUrl).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                log.error("Discord 웹훅 전송 실패: {}", e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) {
                try (response) {
                    if (!response.isSuccessful()) {
                        log.error("Discord 웹훅 응답 실패: code={}", response.code());
                    }
                }
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        try {
            client.dispatcher().cancelAll(); //모든 호출 취소
            client.connectionPool().evictAll(); // 커넥션 풀 비우기
            ExecutorService es = client.dispatcher().executorService(); //실행기 정상 종료 시도
            es.shutdown();
            if (!es.awaitTermination(5, TimeUnit.SECONDS)) {
                es.shutdownNow(); // 강제 종료
            }

            if (client.cache() != null) client.cache().close();

            log.info("OkHttp 리소스 정리 완료");
        } catch (Exception e) {
            log.warn("OkHttp 리소스 정리 중 예외: {}", e.getMessage());
        }
    }

    private static String nz(String v, String d) { return (v == null || v.isEmpty()) ? d : v; }
}