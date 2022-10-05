package com.example.apiuser.domain.member.vo;

import com.example.moduledomain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberReq {

  @NotBlank
  private String userId;
  @NotBlank
  private String password;
  @NotBlank
  private String name;

  public Member toMember() {
    return Member.builder()
      .userId(this.userId)
      .password(this.password)
      .name(this.name).build();
  }


}
