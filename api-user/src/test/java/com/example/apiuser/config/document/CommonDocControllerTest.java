package com.example.apiuser.config.document;
import com.example.apiuser.config.RestDocsTestSupport;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommonDocController.class)
public class CommonDocControllerTest extends RestDocsTestSupport {



  @Test
  public void errorSample() throws Exception {
    CommonDocController.SampleRequest sampleRequest = new CommonDocController.SampleRequest("","hhh.naver");
    mockMvc.perform(
        post("/test/error")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(sampleRequest))
      )
      .andExpect(status().isBadRequest())
      .andDo(
        restDocs.document(
          responseFields(
            beneathPath("list[0]"),
            fieldWithPath("field").description("에러 메시지"),
            fieldWithPath("message").description("Error Code"),
            fieldWithPath("code").description("Error 값 배열 값")
//            fieldWithPath("errors[0].field").description("문제 있는 필드"),
//            fieldWithPath("errors[0].value").description("문제가 있는 값"),
//            fieldWithPath("errors[0].reason").description("문재가 있는 이유")
          )
        )
      )
    ;
  }



  @Test
  public void enums() throws Exception {

    ResultActions result = this.mockMvc.perform(
      get("/test/enums").contentType(MediaType.APPLICATION_JSON));

    // 결과값
    MvcResult mvcResult = result.andReturn();

    // 데이터 파싱
    EnumDocs enumDocs = getData(mvcResult);

    // 문서화 진행
    result.andExpect(status().isOk())
      .andDo(restDocs.document(
        customResponseFields("custom-response", beneathPath("data.socialType").withSubsectionId("socialType"), // (1)
          attributes(key("title").value("socialType")),
          enumConvertFieldDescriptor((enumDocs.getSocialType()))
        )
      ));

  }


  // 커스텀 템플릿 사용을 위한 함수
  public static CustomResponseFieldsSnippet customResponseFields
  (String type,
   PayloadSubsectionExtractor<?> subsectionExtractor,
   Map<String, Object> attributes, FieldDescriptor... descriptors) {
    return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
      , true);
  }

  // Map으로 넘어온 enumValue를 fieldWithPath로 변경하여 리턴
  private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
    return enumValues.entrySet().stream()
      .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
      .toArray(FieldDescriptor[]::new);
  }


  private EnumDocs getData(MvcResult result) throws IOException {
    ApiResponseDto<EnumDocs> apiResponseDto = objectMapper
      .readValue(result.getResponse().getContentAsByteArray(),
        new TypeReference<ApiResponseDto<EnumDocs>>() {}
      );
    return apiResponseDto.getData();
  }
}
