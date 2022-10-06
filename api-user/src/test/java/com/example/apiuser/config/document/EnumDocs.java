package com.example.apiuser.config.document;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumDocs {

  Map<String,String> socialType;

}
