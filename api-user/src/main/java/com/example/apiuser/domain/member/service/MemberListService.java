package com.example.apiuser.domain.member.service;

import com.example.modulecore.response.ListResult;
import com.example.modulecore.response.ResponseService;
import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberListService {

  private final MemberRepository memberRepository;
  private final ResponseService responseService;


  public ListResult<Member> findAll(){
    return responseService.getListResult(memberRepository.findAll());
  }

}
