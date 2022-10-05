package com.example.modulesystem.security;

import lombok.Getter;


@Getter
public enum Role {

    ROLE_LV1("1","일반사원"),
    ROLE_LV2("2","경영"),
    ROLE_LV3("3","전무"),
    ROLE_LV4("4","대표"),
    undecided("5","미정");

    private final String code;
    private final String name;

    Role(String code, String name) {

        this.code = code;
        this.name = name;

    }

}
