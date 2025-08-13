package org.scoula.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource({"classpath:/application.properties"})
@MapperScan("org.scoula.mapper")
@ComponentScan(basePackages = {"org.scoula"})
@EnableTransactionManagement
public class RootConfig {

	@Value("${jdbc.driver}")   private String driver;
	@Value("${jdbc.url}")      private String url;
	@Value("${jdbc.username}") private String username;
	@Value("${jdbc.password}") private String password;

	// Hikari 튜닝값을 프로퍼티에서 바꾸고 싶으면 아래 @Value로 받아도 됨
	// (지금은 30분 wait_timeout 가정값으로 안전한 기본치 세팅)
	private static final long WAIT_TIMEOUT_SEC   = 1800L;          // RDS wait_timeout (예: 30분)
	private static final long MAX_LIFETIME_MS    = (WAIT_TIMEOUT_SEC - 100) * 1000L; // 1700s
	private static final long IDLE_TIMEOUT_MS    = 600_000L;       // 10분
	private static final long CONNECTION_TIMEOUT = 30_000L;        // 30초
	private static final int  MAX_POOL_SIZE      = 10;
	private static final int  MIN_IDLE           = 2;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public DataSource dataSource() {
		HikariConfig cfg = new HikariConfig();

		// ⚠ log4jdbc 사용 시 드라이버/URL을 log4jdbc로 일관되게
		cfg.setDriverClassName(driver);
		cfg.setJdbcUrl(url);
		cfg.setUsername(username);
		cfg.setPassword(password);

		// === Hikari 핵심 설정 ===
		cfg.setMaximumPoolSize(MAX_POOL_SIZE);          // DB max_connections 대비 작게
		cfg.setMinimumIdle(MIN_IDLE);
		cfg.setConnectionTimeout(CONNECTION_TIMEOUT);
		cfg.setIdleTimeout(IDLE_TIMEOUT_MS);
		cfg.setMaxLifetime(MAX_LIFETIME_MS);            // wait_timeout보다 30~90초 짧게
		cfg.setLeakDetectionThreshold(2_000);           // 2초 이상 점유 시 누수 스택로그

		// MySQL 옵티마이즈(선택)
		cfg.addDataSourceProperty("cachePrepStmts", "true");
		cfg.addDataSourceProperty("prepStmtCacheSize", "256");
		cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		cfg.addDataSourceProperty("useServerPrepStmts", "true");

		cfg.setPoolName("main-pool");

		return new HikariDataSource(cfg);
	}

	// DataSource를 파라미터로 주입받아 "같은 풀"을 재사용 (직접 dataSource() 호출 X)
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setConfigLocation(applicationContext.getResource("classpath:/mybatis-config.xml"));
		factory.setDataSource(dataSource);
		// 필요 시 매퍼 XML 경로 지정:
		// factory.setMapperLocations(applicationContext.getResources("classpath*:/mapper/**/*.xml"));
		return factory.getObject();
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}