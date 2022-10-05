package com.example.modulecore.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class CommonResult {
    private boolean success;
    private int code;
    private String msg;

}
