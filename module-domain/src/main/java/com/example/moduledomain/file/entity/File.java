package com.example.moduledomain.file.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "origin_file_name")
    private String originalFileName;
    @Column(name = "save_file_name")
    private String S3FileName;
    @Column(name="size")
    private Long size;
    @Column(name="extension")
    private String extension;
    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;



    public static File of (String originalFileName , String s3FileName,Long size,String extension){
       return new File(null,originalFileName,s3FileName,size,extension,LocalDateTime.now());
    }
    public static File from(Long id){
        File file = new File();
        file.id = id;
        return file;
    }


}
