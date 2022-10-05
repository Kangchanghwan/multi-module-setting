package com.example.apiuser.domain.member.service;

import com.example.apiuser.config.security.AuthMember;
import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.repository.MemberRepository;
import com.example.modulesystem.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member = memberRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

    return AuthMember.builder()
      .userId(member.getUserId())
      .password(member.getPassword())
      .role(Role.ROLE_LV1).build();
  }
}
