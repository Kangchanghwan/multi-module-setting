package com.example.modulecore.exception;
import com.example.modulecore.response.code.ResponseCodeInterface;

public class CustomRuntimeException extends RuntimeException{

  private ResponseCodeInterface responseCode;

  public CustomRuntimeException(String message, ResponseCodeInterface responseCode) {
    super(message);
    this.responseCode = responseCode;
  }

  public CustomRuntimeException(ResponseCodeInterface responseCode) {
    this.responseCode = responseCode;
  }

  public ResponseCodeInterface getResponseCode() {
    return responseCode;
  }
}
