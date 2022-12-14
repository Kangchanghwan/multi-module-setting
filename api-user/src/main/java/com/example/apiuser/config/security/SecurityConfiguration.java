package com.example.apiuser.config.security;

import com.example.apiuser.config.security.oauth.OAuth2SuccessHandler;
import com.example.apiuser.domain.member.service.CustomUserDetailService;
import com.example.apiuser.domain.member.service.PrincipalOauth2UserService;
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
  private final PrincipalOauth2UserService principalOauth2UserService;

  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final CustomUserDetailService customUserDetailService;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  @Value("${spring.config.activate.on-profile}")
  private String profile;

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    /**
     * ?????? ?????? security ??????
     * */
    if (profile.equals(
      //"dev"
      "prod"
    )
      || profile.equals("test")
    ) {
      log.info("---------??????, ????????? ?????? security ????????? ???????????????. -------");

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
        .addFilterBefore(new CustomAuthorizationFilter(jwtProvider,customUserDetailService), UsernamePasswordAuthenticationFilter.class)
        .oauth2Login().loginPage("/login").successHandler(oAuth2SuccessHandler).userInfoEndpoint().userService(principalOauth2UserService);
      log.info("---------??????, ????????? ?????? security ????????? ???????????????. -------");
      authorizeRequests(http); // ?????? ??????

      return http.build();
    }

    log.info("---------?????? ?????? security ????????? ???????????????. -------");
    /**
     * ?????? ?????? security ??????
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
      .authorizeRequests() // ????????? ????????? URI??? ????????? ??? ?????? ?????? ??????
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
      .antMatchers("/oauth2/authorization/**").permitAll()
      .antMatchers("/login/**").permitAll()
      .antMatchers(GET, "/api/**/img/**").permitAll()

//          ????????????
      .antMatchers(GET, "/api/**/expense/**").permitAll() //
      .antMatchers(POST, "/api/**/expense/**").permitAll()
      .antMatchers(PUT, "/api/**/expense/**").permitAll()
//          .antMatchers(PUT, "/api/**/expense/**").access("hasRole('ROLE_LV3')")
      .antMatchers(POST, "/api/**/commutes").permitAll() // ?????????

      .antMatchers(GET, "/api/v1/members").access("hasRole('ROLE_LV1')") // ROLE_ADMIN ????????? ?????? ???????????? ?????? ??????
      .antMatchers(GET, "/api/v1/members/*").access("hasRole('ROLE_LV1')") // ROLE_ADMIN ????????? ?????? ???????????? ?????? ??????
      .antMatchers("/api/v1/members/**").access("hasRole('ROLE_TEMPORARY_MEMBER')") // ROLE_TEMPORARY_MEMBER ????????? ?????? ???????????? ?????? ??????
      .antMatchers("/api/**/re-issue").authenticated() // ????????? ???????????? ?????? ??????
      .antMatchers(GET, "/api/**/members", "/api/**/members/**").authenticated()
      .anyRequest().authenticated()// ??? ??? ?????? ?????? ?????? ??????
      .and()
      .addFilterBefore(new CustomAuthorizationFilter(jwtProvider,customUserDetailService), UsernamePasswordAuthenticationFilter.class)
      .oauth2Login().loginPage("/login").successHandler(oAuth2SuccessHandler).userInfoEndpoint().userService(principalOauth2UserService);// oauth2

    log.info("---------?????? ?????? security ????????? ???????????????. -------");


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
    http.authorizeRequests().anyRequest().authenticated();// ??? ??? ?????? ?????? ?????? ??????
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
