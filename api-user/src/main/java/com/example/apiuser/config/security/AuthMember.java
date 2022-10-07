package com.example.apiuser.config.security;

import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.entity.SocialType;
import com.example.modulesystem.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthMember implements UserDetails, OAuth2User {

  private Long id;
  private String userId;
  private String password;
  private Role role;
  private SocialType socialType;

  private Map<String,Object> attribute;
  public AuthMember(Member member,Map<String,Object> attributes) {
    this.userId = member.getUserId();
    this.password = member.getPassword();
    this.role = Role.ROLE_LV1;
    this.socialType = member.getSocialType();
    this.attribute = attributes;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attribute;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.name()));
    return authorities;  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return null;
  }
}
