package com.example.apiuser.domain.member.service;

import com.example.apiuser.domain.member.vo.MemberReq;
import com.example.modulecore.response.ResponseService;
import com.example.modulecore.response.SingleResult;
import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberJoinService {


  private final MemberRepository memberRepository;
  private final ResponseService responseService;

  private final PasswordEncoder passwordEncoder;

  public SingleResult<Member> join(MemberReq memberReq){
    encodingPassword(memberReq);
    return responseService.getSingleResult( memberRepository.save(memberReq.toMember()));
  }

  private void encodingPassword(MemberReq memberReq) {
    memberReq.setPassword(passwordEncoder.encode(memberReq.getPassword()));
  }

}
