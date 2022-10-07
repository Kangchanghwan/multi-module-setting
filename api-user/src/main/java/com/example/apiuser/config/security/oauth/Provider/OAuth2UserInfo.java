package com.example.apiuser.config.security.oauth.Provider;

import com.example.moduledomain.member.entity.SocialType;

public interface OAuth2UserInfo {

    String getProviderId();
    SocialType getProvider();
    String getEmail();
    String getName();

}
