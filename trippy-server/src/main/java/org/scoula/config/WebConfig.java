package org.scoula.config;

import org.scoula.config.swagger.SwaggerAuthHeaderFilter;
import org.scoula.config.swagger.SwaggerConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.File;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ServletConfig.class, SwaggerConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/", "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/webjars/**"
        };
    }

    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();

        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        SwaggerAuthHeaderFilter swaggerAuthHeaderFilter = new SwaggerAuthHeaderFilter();

        return new Filter[] { characterEncodingFilter, swaggerAuthHeaderFilter };
    }

    final String LOCATION = System.getProperty("java.io.tmpdir") + "/trippy_uploads";
    final long MAX_FILE_SIZE = 1024 * 1024 * 10L;
    final long MAX_REQUEST_SIZE =  1024 * 1024 * 20L;
    final int FILE_SIZE_THRESHOLD = 1024 * 1024 * 5;;

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");

        // 디렉토리 없으면 생성
        File uploadDir = new File(LOCATION);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartConfigElement multipartConfig = new MultipartConfigElement(
            LOCATION,
            MAX_FILE_SIZE,
            MAX_REQUEST_SIZE,
            FILE_SIZE_THRESHOLD
        );
        registration.setMultipartConfig(multipartConfig);
    }

}
