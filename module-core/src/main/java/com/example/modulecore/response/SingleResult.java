package com.example.modulecore.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SingleResult<T> extends CommonResult{

    private T data;


}
