package com.example.apiuser.domain.member.service;

import com.example.apiuser.domain.member.res.LoginRes;
import com.example.apiuser.domain.member.res.MemberInfoRes;
import com.example.apiuser.domain.member.vo.LoginReq;
import com.example.modulecore.response.ResponseService;
import com.example.modulecore.response.SingleResult;
import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.repository.MemberRepository;
import com.example.modulesystem.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MemberAccountService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  public LoginRes login(LoginReq loginReq) {
    Member member = memberRepository.findByUserId(loginReq.getUserId()).orElseThrow(notFoundMember());
    checkPassword(loginReq.getPassword(), member.getPassword());
    String accessToken = jwtProvider.createAccessToken(member.getUserId());
    String refreshToken = jwtProvider.createRefreshToken(member.getUserId());

    return LoginRes.of(accessToken,refreshToken,member);

  }

  private Supplier<UsernameNotFoundException> notFoundMember() {
    return () -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
  }

  private void checkPassword(String password, String encodedPassword) {
    boolean isSame = passwordEncoder.matches(password, encodedPassword);
    if(!isSame) {
      throw new IllegalStateException("아이디 혹은 비밀번호를 확인하세요.");
    }
  }

  public MemberInfoRes getInfo(String userId) {
    return MemberInfoRes.of(memberRepository.searchMember(userId).orElseThrow(notFoundMember()));
  }
}
