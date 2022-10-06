package com.example.apiuser.domain.member.res;

import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.entity.SocialType;
import com.example.modulesystem.security.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoRes {

  private String name;
  private String userId;
  private Role role;
  private Long id;
  private SocialType socialType;
  public static MemberInfoRes of(Member member) {
    return MemberInfoRes.builder()
                        .name(member.getName())
                        .id(member.getId())
                        .userId(member.getUserId())
                        .role(Role.ROLE_LV1)
                        .socialType(member.getSocialType())
                        .build();
  }


}
