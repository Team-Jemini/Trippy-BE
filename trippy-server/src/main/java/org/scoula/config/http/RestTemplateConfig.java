package org.scoula.config.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		// 커넥션 풀
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);             // 전체 커넥션 수
		cm.setDefaultMaxPerRoute(50);    // 라우트당 최대

		HttpClient httpClient = HttpClients.custom()
			.setConnectionManager(cm)
			.disableCookieManagement()
			.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(httpClient);
		factory.setConnectTimeout(3000);   // 연결 타임아웃(ms)
		factory.setReadTimeout(5000);      // 응답 대기 타임아웃(ms)
		factory.setConnectionRequestTimeout(2000); // 풀에서 커넥션 빌림 대기

		return new RestTemplate(factory);
	}
}
