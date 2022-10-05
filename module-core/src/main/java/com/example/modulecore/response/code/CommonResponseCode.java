package com.example.modulecore.response.code;


public enum CommonResponseCode implements ResponseCodeInterface {

    /**
     *  각각 엔티티별로 에러코드를 정규화 한다.
     *  000번대는 엔티티별 고유코드
     *  100 -> User 관련 에러
     *  200 -> Board 관련 에러
     *  900 -> Security 관련 에러
     * */

    //----------------Common---------------
    F_VALIDATION(800,"Validation failed."),
    F_NOT_FOUND_FILE(801,"Not Found File"),
    F_IMG_UPLOAD_FAIL(802,"image upload Fail"),
    F_DOWNLOAD_FAIL(803,"Download Fail"),
    F_CONVERSION_IMG(804,"Image conversion failed"),

    //----------------unknown---------------
    F_UNKNOWN_ERR(999,"unknown error");



    public final int messageCode;
    public final String message;

    CommonResponseCode(int messageCode, String message) {
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
