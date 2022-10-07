package com.example.apiuser.config.security.oauth;

import com.example.apiuser.config.security.AuthMember;
import com.example.apiuser.domain.member.res.LoginRes;
import com.example.modulesystem.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    AuthMember oAuth2User = (AuthMember) authentication.getPrincipal();

    String accessToken = jwtProvider.createAccessToken(oAuth2User.getUserId());
    String refreshToken = jwtProvider.createRefreshToken(oAuth2User.getUserId());

    response.setContentType("application/json;charset=UTF-8");

    PrintWriter writer = response.getWriter();
    writer.println(objectMapper.writeValueAsString(LoginRes.of(accessToken,refreshToken)));
    writer.flush();
  }
}
