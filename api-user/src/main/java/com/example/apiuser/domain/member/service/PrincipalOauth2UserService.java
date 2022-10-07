package com.example.apiuser.domain.member.service;


import com.example.apiuser.config.security.AuthMember;
import com.example.apiuser.config.security.oauth.Provider.FacebookUserInfo;
import com.example.apiuser.config.security.oauth.Provider.GoogleUserInfo;
import com.example.apiuser.config.security.oauth.Provider.NaverUserInfo;
import com.example.apiuser.config.security.oauth.Provider.OAuth2UserInfo;
import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


  private final PasswordEncoder encoder;
  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
    OAuth2UserInfo info = null;
    switch (userRequest.getClientRegistration().getRegistrationId()) {
      case "google" -> info = new GoogleUserInfo(oAuth2User.getAttributes());
      case "facebook" -> info = new FacebookUserInfo(oAuth2User.getAttributes());
      case "naver" -> info = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
      default -> System.out.println("지원하지 않는 소셜로그인입니다.");
    }

    String providerId = Objects.requireNonNull(info).getProviderId();
    String userId = info.getProvider() + "_" + providerId; // google_12312415151;;
    String password = encoder.encode("start");
    String email = info.getEmail();
    String role = "ROLE_USER";

    Member findMember = memberRepository.findByUserId(userId).orElse(null);

    if (findMember == null) {
      findMember = Member.builder()
        .userId(userId)
        .socialType(info.getProvider())
        .name(info.getName())
        .password(password)
        .build();
      memberRepository.save(findMember);
    }
    return new AuthMember(findMember, oAuth2User.getAttributes());
  }
}
