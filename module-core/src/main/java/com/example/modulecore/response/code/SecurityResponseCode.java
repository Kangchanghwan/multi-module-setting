package com.example.modulecore.response.code;


public enum SecurityResponseCode implements ResponseCodeInterface {

    /**
     *  각각 엔티티별로 에러코드를 정규화 한다.
     *  000번대는 엔티티별 고유코드
     *  100 -> User 관련 에러
     *  200 -> Board 관련 에러
     *  900 -> Security 관련 에러
     * */

    //----------------Security---------------
    F_ID_OF_TOKEN(901, "UserId Of Token Fail"),
    F_TOKEN(902, "Unknown Token Fail"),
    F_ACCESS(903,"No Access"),
    F_ROLE(904,"No Access Role"),
    F_EXPIRED_TOKEN(905,"Expired Jwt Token");



    public final int messageCode;
    public final String message;

    SecurityResponseCode(int messageCode, String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getMessageCode() {
        return this.messageCode;
    }
}
