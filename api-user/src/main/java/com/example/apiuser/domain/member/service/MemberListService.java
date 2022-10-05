package com.example.apiuser.domain.member.service;

import com.example.apiuser.domain.member.res.MemberInfoRes;
import com.example.modulecore.response.ListResult;
import com.example.modulecore.response.ResponseService;
import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberListService {

  private final MemberRepository memberRepository;


  public List<MemberInfoRes> findAll(){
   return memberRepository.findAll()
                         .stream()
                         .map(MemberInfoRes::of)
                         .collect(Collectors.toList());
  }

}
