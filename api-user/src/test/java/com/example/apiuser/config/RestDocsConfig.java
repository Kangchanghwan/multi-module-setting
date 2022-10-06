package com.example.apiuser.config;

import com.example.modulecore.response.ResponseService;
import com.example.modulesystem.handler.GlobalExceptionHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.*;


@TestConfiguration
public class RestDocsConfig {

  @Bean
  public ResponseService responseService (){
    return new ResponseService();
  }

  @Bean
  public RestDocumentationResultHandler write(){
    return MockMvcRestDocumentation.document(
      "{class-name}/{method-name}",
      //디렉토리 명을 클래스명/메서드 명으로 정한다.
      Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
      Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    );
  }
  public static Attribute field(
    final String key,
    final String value){
    return new Attribute(key,value);
  }
}
