package com.example.apiuser.config;

import com.example.apiuser.domain.member.controller.MemberController;
import com.example.apiuser.domain.member.service.MemberAccountService;
import com.example.apiuser.domain.member.service.MemberJoinService;
import com.example.apiuser.domain.member.service.MemberListService;
import com.example.modulecore.response.ResponseService;
import com.example.moduledomain.member.repository.MemberRepository;
import com.example.modulesystem.handler.GlobalExceptionHandler;
import com.example.modulesystem.handler.UnknownExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest({
  MemberController.class
})
public abstract class ControllerTest {

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  protected MemberListService memberListService;
  @MockBean
  protected MemberJoinService memberJoinService;
  @MockBean
  protected MemberAccountService memberAccountService;
  @MockBean
  protected UnknownExceptionHandler unknownExceptionHandler;
  @MockBean
  protected GlobalExceptionHandler globalExceptionHandler;
//  @MockBean
//  protected ResponseService responseService;

  protected String createJson(Object dto) throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }
}
