package com.example.apiuser.domain.member.controller;


import com.example.apiuser.config.security.AuthMember;
import com.example.apiuser.domain.member.res.LoginRes;
import com.example.apiuser.domain.member.service.MemberAccountService;
import com.example.apiuser.domain.member.service.MemberJoinService;
import com.example.apiuser.domain.member.service.MemberListService;
import com.example.apiuser.domain.member.vo.LoginReq;
import com.example.apiuser.domain.member.vo.MemberReq;
import com.example.modulecore.response.ListResult;
import com.example.modulecore.response.SingleResult;
import com.example.moduledomain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {


  private final MemberListService memberListService;
  private final MemberJoinService memberJoinService;
  private final MemberAccountService memberAccountService;


  @PostMapping("/members")
  public SingleResult<Member> save(@RequestBody @Validated MemberReq memberReq){
    return  memberJoinService.join(memberReq);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public SingleResult<LoginRes> mobileLogin(@RequestBody @Validated LoginReq loginReq) {
    return memberAccountService.login(loginReq);
  }
  @GetMapping("/members")
  public ListResult<Member> findAll(){
   return memberListService.findAll();
  }

  @GetMapping("/members/account")
  public SingleResult<Member> findOne(@AuthenticationPrincipal AuthMember authMember){
    return memberAccountService.getInfo(authMember.getUsername());
  }

}
