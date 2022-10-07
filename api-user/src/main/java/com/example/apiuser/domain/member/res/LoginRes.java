package com.example.apiuser.domain.member.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRes {

  private String accessToken;
  private String refreshToken;

  public  static LoginRes of(String accessToken, String refreshToken){
    return  LoginRes.builder().accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

}
