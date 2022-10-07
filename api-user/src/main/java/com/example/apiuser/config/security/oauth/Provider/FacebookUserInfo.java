package com.example.apiuser.config.security.oauth.Provider;

import com.example.moduledomain.member.entity.SocialType;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo{


    private Map<String,Object> attributes;


    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public SocialType getProvider() {
        return SocialType.FACEBOOK;
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
