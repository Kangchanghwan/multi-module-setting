package com.example.apiuser.config.security;

import com.example.apiuser.domain.member.service.CustomUserDetailService;
import com.example.modulesystem.security.CustomAccessDeniedHandler;
import com.example.modulesystem.security.CustomAuthenticationEntryPoint;
import com.example.modulesystem.security.EndPointAccessByLevel;
import com.example.modulesystem.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration {

  private final JwtProvider jwtProvider;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  private final CustomUserDetailService customUserDetailService;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  @Value("${spring.config.activate.on-profile}")
  private String profile;

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    /**
     * 실제 환경 security 설정
     * */
    if (profile.equals(
      //"dev"
      "prod"
    )
      || profile.equals("test")
    ) {
      log.info("---------운영, 테스트 환경 security 설정을 시작합니다. -------");

      http
        .cors()

        .and()
        .csrf().disable()
        .httpBasic().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler)
        .and()
        .addFilterBefore(new CustomAuthorizationFilter(jwtProvider,customUserDetailService), UsernamePasswordAuthenticationFilter.class);
      log.info("---------운영, 테스트 환경 security 설정을 종료합니다. -------");
      authorizeRequests(http); // 권한 설정

      return http.build();
    }

    log.info("---------개발 환경 security 설정을 시작합니다. -------");
    /**
     * 개발 환경 security 설정
     * */
    http
      .cors()

      .and()
      .csrf().disable()
      .httpBasic().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

      .and()
      .exceptionHandling()
      .authenticationEntryPoint(customAuthenticationEntryPoint)
      .accessDeniedHandler(customAccessDeniedHandler)

      .and()
      .authorizeRequests() // 보호된 리소스 URI에 접근할 수 있는 권한 설정
      .antMatchers(GET, "/exception/**").permitAll()
      .antMatchers(POST, "/api/**/members").permitAll()
      .antMatchers(POST, "/api/**/users").permitAll()
      .antMatchers(POST, "/api/**/login/**").permitAll()
      .antMatchers("/privacy").permitAll()
      .antMatchers(POST, "/api/**/re-issue").permitAll()
      .antMatchers("/api/**/anonymous/**").permitAll()
      .antMatchers("/api/**/anonymous").permitAll()
      .antMatchers("/api/**/comments").permitAll()
      .antMatchers("/api/**/comments/**/**").permitAll()
      .antMatchers("/api/**/comments/parents/**").permitAll()

      .antMatchers(GET, "/api/**/img/**").permitAll()

//          지출결의
      .antMatchers(GET, "/api/**/expense/**").permitAll() //
      .antMatchers(POST, "/api/**/expense/**").permitAll()
      .antMatchers(PUT, "/api/**/expense/**").permitAll()
//          .antMatchers(PUT, "/api/**/expense/**").access("hasRole('ROLE_LV3')")
      .antMatchers(POST, "/api/**/commutes").permitAll() // 출퇴근

      .antMatchers(GET, "/api/v1/members").access("hasRole('ROLE_LV1')") // ROLE_ADMIN 권한을 가진 사용자만 접근 허용
      .antMatchers(GET, "/api/v1/members/*").access("hasRole('ROLE_LV1')") // ROLE_ADMIN 권한을 가진 사용자만 접근 허용
      .antMatchers("/api/v1/members/**").access("hasRole('ROLE_TEMPORARY_MEMBER')") // ROLE_TEMPORARY_MEMBER 권한을 가진 사용자만 접근 허용
      .antMatchers("/api/**/re-issue").authenticated() // 인증된 사용자만 접근 허용
      .antMatchers(GET, "/api/**/members", "/api/**/members/**").authenticated()
      .anyRequest().authenticated()// 그 외 항목 전부 인증 적용

      .and()
      .addFilterBefore(new CustomAuthorizationFilter(jwtProvider,customUserDetailService), UsernamePasswordAuthenticationFilter.class);
    log.info("---------개발 환경 security 설정을 종료합니다. -------");


    return http.build();
  }

  private void authorizeRequests(HttpSecurity http) throws Exception {


    EndPointAccessByLevel.NONE.getEndPoints()
      .forEach(endPoint -> {
        try {
          http.authorizeRequests()
            .antMatchers(endPoint.getMethod(), endPoint.getEndPointName()).permitAll();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });

    EndPointAccessByLevel.ROLES.getEndPoints()
      .forEach(endPoint -> {
        try {
          http.authorizeRequests()
            .antMatchers(endPoint.getMethod(), endPoint.getEndPointName()).hasAnyAuthority(endPoint.getRoles());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    EndPointAccessByLevel.COMMON.getEndPoints()
      .forEach(endPoint -> {
        try {
          http.authorizeRequests()
            .antMatchers(endPoint.getMethod(), endPoint.getEndPointName()).hasAnyAuthority(endPoint.getRoles());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    http.authorizeRequests().anyRequest().authenticated();// 그 외 항목 전부 인증 적용
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers("/exception/**");

  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


}
