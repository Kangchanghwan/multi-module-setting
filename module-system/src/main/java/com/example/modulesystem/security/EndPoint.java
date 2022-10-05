package com.example.modulesystem.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

@Getter
@Setter
public class EndPoint {

  private HttpMethod method;
  private String endPointName;
  private Role[] roles;

  public static EndPoint of(HttpMethod method, String endPointName) {
    EndPoint endPoint = new EndPoint();
    endPoint.method = method;
    endPoint.endPointName = endPointName;
    endPoint.roles = new Role[]{Role.ROLE_LV1,Role.ROLE_LV2,Role.ROLE_LV3,Role.ROLE_LV4};
    return endPoint;
  }
  public static EndPoint of(HttpMethod method, String endPointName,Role ...role) {
    EndPoint endPoint = new EndPoint();
    endPoint.method = method;
    endPoint.endPointName = endPointName;
    endPoint.roles = role;
    return endPoint;
  }

  public String[] getRoles() {
    return Arrays.stream(roles)
      .map(Enum::toString)
      .toArray(String[]::new);
  }
}
