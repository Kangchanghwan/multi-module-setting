package com.example.modulesystem.security;

import java.util.List;

/**
 * LV1, LV2, LV3, LV4  -> 레벨 별 권한만 접근 가능
 * COMMON ->  모든 권한 접근 가능 (토큰 필요)
 * NONE -> 권한 없이 접근 가능 (토큰 불필요)
 * */

public enum EndPointAccessByLevel {

  ROLES(List.of(
    //----------- day-off ------------------
    //EndPoint.of(GET,"/api/v1/day-off/remaining",ROLE_LV1,ROLE_LV2),


  )),

  COMMON(List.of(
    //----------- Day-off ------------------


  )),

  NONE(List.of(
    //--------------login-------------------------


  ));
  private final List<EndPoint> endPoints;

  EndPointAccessByLevel(List<EndPoint> endPointList) {
    endPoints = endPointList;
  }

  public List<EndPoint> getEndPoints() {
    return endPoints;
  }
}
