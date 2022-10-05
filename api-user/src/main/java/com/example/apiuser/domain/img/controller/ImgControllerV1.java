package com.example.apiuser.domain.img.controller;

import com.example.apiuser.domain.img.res.ImgRes;
import com.example.apiuser.domain.img.service.ImgService;
import com.example.modulecore.response.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ImgControllerV1 {

  private final ImgService imgService;

  @PostMapping(value = "/img")
  @ResponseStatus(HttpStatus.OK)
  public SingleResult<ImgRes> imgUpload(@RequestPart("img") MultipartFile multipartFile) {
    return imgService.uploadFiles(multipartFile, "prod");
  }

}
