package com.example.apiuser.domain.file.controller;



import com.example.apiuser.domain.file.res.UploadResponse;
import com.example.apiuser.domain.file.service.FileService;
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
public class FileControllerV1 {

  private final FileService fileService;

  /**
   *
   * 파일 등록
   * */
  @PostMapping(value = "/files")
  @ResponseStatus(HttpStatus.OK)
  public SingleResult<UploadResponse> fileUpload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
    return fileService.uploadFiles(multipartFile, "templates");
  }
  /**
   * 파일 다운로드
   * */
  @GetMapping("/files/{id}")
  public ResponseEntity<byte[]> fileDownload(@PathVariable Long id) throws  IOException {
    return fileService.downloadFile(id);
  }
  /**
   * 파일 삭제
   * deprecated
   */


}
