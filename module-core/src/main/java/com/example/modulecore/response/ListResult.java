package com.example.modulecore.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ListResult<T> extends CommonResult {
    private List<T> list;
}
