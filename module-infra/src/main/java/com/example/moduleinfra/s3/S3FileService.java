package com.example.moduleinfra.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3FileService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.file-bucket}")
    private String bucket;

    public String upload(File uploadFile, String filePath) {
        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        log.info("upload success! url : {}",uploadImageUrl);
        removeNewFile(uploadFile);
        return fileName;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("File delete success");
            return;
        }
        System.out.println("File delete fail");
    }

    // 로컬에 파일 업로드 하기

    public S3Object downloadFile(String storedFileName) {
        return amazonS3Client.getObject(new GetObjectRequest(bucket, storedFileName));
    }

    public Boolean deleteFile(String fileName){
        try {
            if(!amazonS3Client.doesObjectExist(bucket,fileName)){
                log.error("Object " + fileName+ " does not exist!");
                //  throw new IllegalArgumentException("해당하는 파일이 없습니다.");
                return false;
            }
            //Delete 객체 생성
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket,  fileName);
            //Delete
            amazonS3Client.deleteObject(deleteObjectRequest);
        } catch (SdkClientException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }
}
