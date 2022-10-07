package com.example.moduledomain.member.entity;

import com.example.modulecore.docment.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum SocialType implements EnumType {
  KAKAO("카카오"),
  NAVER("네이버"),
  FACEBOOK("페이스북"),
  GOOGLE("구글"),
  APPLE("애플"),
  NORMAL("일반가입");

  private final String description;

  @Override
  public String getName() {
    return this.name();
  }

  @Override
  public String getDescription() {
    return description;
  }
}
