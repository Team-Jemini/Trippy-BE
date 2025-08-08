package org.scoula.config.swagger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

public class SwaggerAuthHeaderFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String authHeader = httpRequest.getHeader("Authorization");

		// Swagger 요청 중 Authorization 헤더가 존재하고 Bearer가 붙지 않았다면 자동 추가
		if (authHeader != null && !authHeader.toLowerCase().startsWith("bearer ")) {
			HttpServletRequest wrapper = new HttpServletRequestWrapper(httpRequest) {
				@Override
				public String getHeader(String name) {
					if ("Authorization".equalsIgnoreCase(name)) {
						return "Bearer " + authHeader;
					}
					return super.getHeader(name);
				}

				@Override
				public Enumeration<String> getHeaders(String name) {
					if ("Authorization".equalsIgnoreCase(name)) {
						return Collections.enumeration(List.of("Bearer " + authHeader));
					}
					return super.getHeaders(name);
				}
			};

			chain.doFilter(wrapper, response);
		} else {
			chain.doFilter(request, response);
		}
	}
}