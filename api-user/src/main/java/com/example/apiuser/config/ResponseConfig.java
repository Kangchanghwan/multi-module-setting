package com.example.apiuser.config;

import com.example.modulecore.response.ResponseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseConfig {

  @Bean
  public ResponseService initializingResponseServiceBean(){
    return new ResponseService();
  }

}
