package com.example.apiuser.domain.file.res;

import com.example.moduledomain.file.entity.File;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UploadResponse {

    private Long fileId;
    private String originFileName;
    private String s3FileName;
    private Long size;
    private String extension;
    public static UploadResponse from(File file){
        return new UploadResponse(file.getId(),
                file.getOriginalFileName(),
                file.getS3FileName(),
                file.getSize(),
                file.getExtension());
    }

}

