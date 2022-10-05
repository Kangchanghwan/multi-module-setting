package com.example.apiuser.domain.img.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.apiuser.domain.img.res.ImgRes;
import com.example.modulecore.exception.CustomRuntimeException;
import com.example.modulecore.response.ResponseService;
import com.example.modulecore.response.SingleResult;
import com.example.modulecore.response.code.CommonResponseCode;
import com.example.moduleinfra.s3.S3ImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.example.apiuser.util.FileConverter.MultipartFileToFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImgService {

    private final S3ImgService s3ImgService;
    private final ResponseService responseService;

    public SingleResult<ImgRes> uploadFiles(MultipartFile file, String fileName)  {
        String saveFileName = "";

        try {
            File covertFile = MultipartFileToFile(file).orElseThrow(() -> {
                throw new CustomRuntimeException("파일 변환에 실패했습니다.",CommonResponseCode.F_CONVERSION_IMG);
            });
            saveFileName = s3ImgService.upload(covertFile, fileName);
        } catch (Exception e) {
            throw new CustomRuntimeException("이미지 업로드에 실패했습니다.",CommonResponseCode.F_IMG_UPLOAD_FAIL);
        }
        return responseService.getSingleResult(ImgRes.of(saveFileName));
    }

}
