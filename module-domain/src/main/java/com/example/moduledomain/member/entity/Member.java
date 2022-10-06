package com.example.moduledomain.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 30 , name = "name")
  private String name;
  @Column
  private String userId;
  @Column
  private String password;
  @Column
  @Enumerated(EnumType.STRING)
  private SocialType socialType;

}
