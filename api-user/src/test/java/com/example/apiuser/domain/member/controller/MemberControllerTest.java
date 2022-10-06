package com.example.apiuser.domain.member.controller;

import com.example.apiuser.config.RestDocsTestSupport;
import com.example.apiuser.config.document.utils.DocumentLinkGenerator;
import com.example.apiuser.domain.member.res.LoginRes;
import com.example.apiuser.domain.member.res.MemberInfoRes;
import com.example.apiuser.domain.member.service.MemberAccountService;
import com.example.apiuser.domain.member.service.MemberJoinService;
import com.example.apiuser.domain.member.service.MemberListService;
import com.example.apiuser.domain.member.vo.LoginReq;
import com.example.apiuser.domain.member.vo.MemberReq;
import com.example.moduledomain.member.entity.SocialType;
import com.example.modulesystem.handler.GlobalExceptionHandler;
import com.example.modulesystem.handler.UnknownExceptionHandler;
import com.example.modulesystem.security.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.apiuser.config.RestDocsConfig.field;
import static com.example.apiuser.config.document.utils.DocumentLinkGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends RestDocsTestSupport {

  final String JOIN = "/api/v1/members";
  final String LOGIN = "/api/v1/login";


  @Test
  public void 회원가입() throws Exception {

    //given
    MemberReq memberReq = new MemberReq();
    memberReq.setName("테스트1");
    memberReq.setPassword("12341234");
    memberReq.setUserId("coke");
    memberReq.setSocialType(SocialType.NORMAL);
    MemberInfoRes memberInfoRes =
      MemberInfoRes
        .builder()
        .id(1L)
        .name("테스트1")
        .userId("coke")
        .socialType(SocialType.NORMAL)
        .role(Role.ROLE_LV1).build();

    when(memberJoinService.normalJoin(any())).thenReturn(memberInfoRes);

    //when then
    mockMvc.perform(post(JOIN)
        .content(createJson(memberReq))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(
        restDocs.document(requestFields(
            fieldWithPath("name").type(STRING).description("회원 이름").attributes(field("constraints", "길이 10 이하")),
            fieldWithPath("userId").type(STRING).description("회원 아이디").attributes(field("constraints", "길이 30이하")),
            fieldWithPath("password").type(STRING).description("회원 비밀번호").attributes(field("constraints", "길이 30이하")),
            fieldWithPath("socialType").type(STRING).description(generateLinkCode(DocUrl.SOCIAL_TYPE))
          )
        )
      );
  }

  @Test
  public void 로그인() throws Exception {
    //given
    LoginRes loginRes = LoginRes.builder()
      .accessToken("accessToken")
      .id(1L)
      .name("홍길동")
      .refreshToken("refreshToken")
      .role(Role.ROLE_LV1)
      .socialType(SocialType.NORMAL)
      .userId("user1").build();
    when(memberAccountService.login(any())).thenReturn(loginRes);

    LoginReq loginReq = new LoginReq();
    loginReq.setPassword("12341234");
    loginReq.setUserId("user1");

    //when then
    mockMvc.perform(post(LOGIN)
        .content(createJson(loginReq))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(
        restDocs.document(
          requestFields(fieldWithPath("userId").type(STRING).description("회원 ID"),
            fieldWithPath("password").type(STRING).description("회원 Password")
          ),
          responseFields(beneathPath("data"),
            fieldWithPath("userId").type(STRING).description("회원 ID"),
            fieldWithPath("id").type(NUMBER).description("회원 고유 ID"),
            fieldWithPath("name").type(STRING).description("이름"),
            fieldWithPath("accessToken").type(STRING).description("JWT Access Token"),
            fieldWithPath("refreshToken").type(STRING).description("JWT Refresh Token"),
            fieldWithPath("role").type(STRING).description("권한"),
            fieldWithPath("socialType").type(STRING).description("소셜 로그인 정보")
          )
        )
      );

  }


}