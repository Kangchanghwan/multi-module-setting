package com.example.modulesystem.handler;



import com.example.modulecore.response.CommonResult;
import com.example.modulecore.response.ListResult;
import com.example.modulecore.response.ResponseService;
import com.example.modulecore.response.code.CommonResponseCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Order(100)
@RequiredArgsConstructor
@Slf4j
public class UnknownExceptionHandler {
  private final ResponseService responseService;

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected CommonResult handleException() {
    return responseService.getFailResult(CommonResponseCode.F_UNKNOWN_ERR);
  }

}
