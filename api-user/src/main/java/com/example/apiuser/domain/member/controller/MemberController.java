package com.example.apiuser.domain.member.controller;


import com.example.apiuser.config.security.AuthMember;
import com.example.apiuser.domain.member.res.LoginRes;
import com.example.apiuser.domain.member.res.MemberInfoRes;
import com.example.apiuser.domain.member.service.MemberAccountService;
import com.example.apiuser.domain.member.service.MemberJoinService;
import com.example.apiuser.domain.member.service.MemberListService;
import com.example.apiuser.domain.member.vo.LoginReq;
import com.example.apiuser.domain.member.vo.MemberReq;
import com.example.modulecore.response.ListResult;
import com.example.modulecore.response.ResponseService;
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

  private final ResponseService responseService;


  @PostMapping("/members")
  @ResponseStatus(HttpStatus.OK)
  public SingleResult<MemberInfoRes> save(@RequestBody @Validated MemberReq memberReq){
    return responseService.getSingleResult(memberJoinService.join(memberReq));
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public SingleResult<LoginRes> login(@RequestBody @Validated LoginReq loginReq) {
    return responseService.getSingleResult(memberAccountService.login(loginReq));
  }
  @GetMapping("/members")
  public ListResult<MemberInfoRes> findAll(){
   return responseService.getListResult(memberListService.findAll());
  }

  @GetMapping("/members/account")
  public SingleResult<MemberInfoRes> findOne(@AuthenticationPrincipal AuthMember authMember){
    return responseService.getSingleResult(memberAccountService.getInfo(authMember.getUsername()));
  }

}
