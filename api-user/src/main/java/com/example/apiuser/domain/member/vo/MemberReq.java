package com.example.apiuser.domain.member.vo;

import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.entity.SocialType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberReq {

  @NotBlank
  @Length(max = 30)
  private String userId;
  @NotBlank
  @Length(max = 30)
  private String password;
  @NotBlank
  @Length(max = 10)
  private String name;

  private SocialType socialType;

  public Member toMember() {
    return Member.builder()
      .userId(this.userId)
      .password(this.password)
      .socialType(SocialType.NORMAL)
      .name(this.name).build();
  }


}
