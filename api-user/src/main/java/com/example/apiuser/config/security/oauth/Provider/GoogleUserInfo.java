package com.example.apiuser.config.security.oauth.Provider;

import com.example.moduledomain.member.entity.SocialType;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{


    private Map<String,Object> attributes;


    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public SocialType getProvider() {
        return SocialType.GOOGLE;
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }
}
