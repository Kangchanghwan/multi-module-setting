package com.example.apiuser.domain.member.vo;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginReq {

  @NotBlank
  private String userId;
  @NotBlank
  private String password;
}
