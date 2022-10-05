package com.example.apiuser.domain.member.res;

import com.example.moduledomain.member.entity.Member;
import com.example.modulesystem.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRes {

  private String accessToken;
  private String refreshToken;
  private String userId;
  private Long id;
  private String name;
  private Role role;

  public  static LoginRes of(String accessToken, String refreshToken, Member member){
    return  LoginRes.builder().accessToken(accessToken)
      .refreshToken(refreshToken)
      .name(member.getName())
      .userId(member.getUserId())
      .role(Role.ROLE_LV1)
      .id(member.getId())
      .build();
  }

}
