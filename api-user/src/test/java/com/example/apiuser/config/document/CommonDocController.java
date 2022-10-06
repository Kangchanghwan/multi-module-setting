package com.example.apiuser.config.document;

import com.example.modulecore.docment.EnumType;
import com.example.moduledomain.member.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class CommonDocController {


  @PostMapping("/error")
  public void errorSample(@RequestBody @Valid SampleRequest dto) {
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SampleRequest {

    @NotEmpty
    private String name;

    @Email
    private String email;
  }



  @GetMapping("/enums")
  public ApiResponseDto<EnumDocs> findEnums(){
    Map<String,String> socialType = getDocs(SocialType.values());

    return ApiResponseDto.from(
      EnumDocs.builder()
        .socialType(socialType)
        .build());
  }

  private Map<String, String> getDocs(EnumType[] enumTypes) {
    return Arrays.stream(enumTypes)
      .collect(Collectors.toMap(EnumType::getName, EnumType::getDescription));
  }

}
