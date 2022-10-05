package com.example.modulesystem.security;

import com.example.modulecore.response.code.SecurityResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");
        ObjectMapper objectMapper = new ObjectMapper();

        if(exception != null && exception.equals(SecurityResponseCode.F_EXPIRED_TOKEN.name())){
            response.sendRedirect("/exception/expired");
            return;
        }
        response.sendRedirect("/exception/entrypoint");
    }

}
