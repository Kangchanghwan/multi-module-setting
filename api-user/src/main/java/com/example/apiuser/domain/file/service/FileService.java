package com.example.apiuser.domain.file.service;


import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.apiuser.domain.file.res.UploadResponse;
import com.example.apiuser.util.FileConverter;
import com.example.modulecore.exception.CustomRuntimeException;
import com.example.modulecore.response.ResponseService;
import com.example.modulecore.response.SingleResult;
import com.example.modulecore.response.code.CommonResponseCode;
import com.example.moduledomain.file.entity.File;
import com.example.moduledomain.file.repository.FileRepository;
import com.example.moduleinfra.s3.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.example.apiuser.util.FileConverter.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

  private final FileRepository fileRepository;
  private final S3FileService s3FileService;
  private final ResponseService responseService;


  public SingleResult<UploadResponse> uploadFiles(MultipartFile file, String fileName) throws IOException {
    String s3FileName;
    String originFileName = file.getOriginalFilename();

    try {
      s3FileName =
        s3FileService.upload(MultipartFileToFile(file).orElseThrow(() -> {
        throw new IllegalStateException("파일 변환에 실패 하였습니다.");
      }), fileName);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
    return responseService.getSingleResult(
      UploadResponse.from(fileRepository.save(
        File.of(originFileName,
          s3FileName,
          file.getSize(),
          Objects.requireNonNull(originFileName)
            .substring(originFileName.lastIndexOf(".") + 1)))));
  }

  public ResponseEntity<byte[]> downloadFile(Long id) {
    File file = fileRepository.findById(id).orElseThrow(
      () -> {
        throw new CustomRuntimeException("해당하는 파일이 없습니다.",CommonResponseCode.F_NOT_FOUND_FILE);
      }
    );
    byte[] bytes;
    try (S3ObjectInputStream objectInputStream = s3FileService.downloadFile(file.getS3FileName()).getObjectContent()) {
      bytes = IOUtils.toByteArray(objectInputStream);
    } catch (Exception e) {
      throw new CustomRuntimeException("파일 다운로드 실패", CommonResponseCode.F_DOWNLOAD_FAIL);
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    httpHeaders.setContentLength(bytes.length);
    httpHeaders.setContentDispositionFormData("attachment", encoding(file.getOriginalFileName()));

    return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
  }

  private String encoding(String originalFileName) {
    return URLEncoder.encode(originalFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
  }

}
