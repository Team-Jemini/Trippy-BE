package org.scoula.config.discord;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = "discordExecutor")
	public ThreadPoolTaskExecutor discordExecutor() {
		ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
		ex.setCorePoolSize(2);
		ex.setMaxPoolSize(4);
		ex.setQueueCapacity(100);
		ex.setThreadNamePrefix("discord-");
		ex.setWaitForTasksToCompleteOnShutdown(true); // 종료시 대기
		ex.setAwaitTerminationSeconds(5);
		ex.initialize();
		return ex;
	}
}
