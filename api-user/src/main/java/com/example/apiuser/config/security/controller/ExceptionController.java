package com.example.apiuser.config.security.controller;


import com.example.modulecore.exception.CustomRuntimeException;
import com.example.modulecore.response.CommonResult;
import com.example.modulecore.response.code.SecurityResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionController {


    @GetMapping(value = "/entrypoint")
    public ResponseEntity<CommonResult> entrypointException() {

        throw new CustomRuntimeException("해당 리소스에 접근하기 위한 권한이 없습니다.", SecurityResponseCode.F_ACCESS);
    }
    @GetMapping(value = "/expired")
    public ResponseEntity<CommonResult> expiredException() {
        throw new CustomRuntimeException("토큰이 만료되었습니다.",SecurityResponseCode.F_EXPIRED_TOKEN);
    }
    @GetMapping(value = "/accessdenied")
    public ResponseEntity<CommonResult> accessDeniedException()  {
        throw new CustomRuntimeException("보유한 권한으로 접근할 수 없는 리소스 입니다.",SecurityResponseCode.F_ROLE);
    }



}
