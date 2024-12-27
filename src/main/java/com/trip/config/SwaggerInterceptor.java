package com.trip.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SwaggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("referer");
        String method = request.getMethod();

        if (referer != null && referer.contains("/swagger-ui/")) {
            if (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Update and Delete operations are disabled for Swagger.");
                return false;
            }
        }
        return true;
    }
}