package com.example.modulesystem.handler;

import com.example.modulecore.exception.CustomRuntimeException;
import com.example.modulecore.response.CommonResult;
import com.example.modulecore.response.ListResult;
import com.example.modulecore.response.ResponseService;
import com.example.modulecore.response.code.CommonResponseCode;
import com.example.modulecore.response.code.SecurityResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice(basePackages = "com.example")
@RequiredArgsConstructor
@Order(0)
@Slf4j
public class GlobalExceptionHandler {
  private final ResponseService responseService;


  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ListResult<Object> handleClientRequestValidException(BindException exception) {
    BindingResult bindResult = exception.getBindingResult();
    List<FieldError> bindResultList = bindResult.getFieldErrors();
    List<ErrorDto> errorDtos = bindResultList.stream().map(error -> {
      ErrorDto errorDto = new ErrorDto();
      errorDto.setCode(error.getCode());
      errorDto.setField(error.getField());
      errorDto.setMessage(error.getDefaultMessage());
      return errorDto;
    }).collect(Collectors.toList());

    return responseService.getFailResults(CommonResponseCode.F_VALIDATION, errorDtos);
  }

  @ExceptionHandler(FileSizeLimitExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected CommonResult handleFileSizeLimitExceededException() {
    return responseService.getFailResult(CommonResponseCode.F_IMG_UPLOAD_FAIL);
  }


  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  protected CommonResult handleAccessDeniedException() {
    return responseService.getFailResult(SecurityResponseCode.F_ROLE);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected CommonResult handleExpiredJwtException() {
    return responseService.getFailResult(SecurityResponseCode.F_EXPIRED_TOKEN);
  }

  @ExceptionHandler(CustomRuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected CommonResult handleCustomRuntimeException(CustomRuntimeException e){
    return responseService.getFailResult(e.getResponseCode());
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  static class ErrorDto {

    private String field;
    private String message;
    private String code;
  }

}
