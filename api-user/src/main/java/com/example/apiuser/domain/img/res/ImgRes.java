package com.example.apiuser.domain.img.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImgRes {

  private static final String cloudFrontUrl = "https://dvmlwfo38sqhb.cloudfront.net/";

  private String url;

  public static ImgRes of(String fileName){
    ImgRes imgRes = new ImgRes();
    imgRes.url = cloudFrontUrl+fileName;
    return imgRes;
  }

}
