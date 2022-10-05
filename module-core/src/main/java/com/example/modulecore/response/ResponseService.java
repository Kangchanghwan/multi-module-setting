package com.example.modulecore.response;

import com.example.modulecore.response.code.ResponseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ResponseService {


    //단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data){
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    //여러건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list){
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // 성공결과만 처리하는 메소드
    public CommonResult getSuccessResult(){
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    private void setSuccessResult (CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    //실패결과만 처리하는 메소드
    public CommonResult getFailResult(ResponseCodeInterface errorResponse){
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(errorResponse.getMessageCode());
        result.setMsg(errorResponse.getMessage());
        return result;
    }


    public <T>ListResult <T> getFailResults(ResponseCodeInterface responseCode, List errors){
        ListResult<T> result = new ListResult<>();
        result.setSuccess(false);
        result.setCode(responseCode.getMessageCode());
        result.setMsg(responseCode.getMessage());
        result.setList(errors);
        return result;
    }
    /**
     * 코드와 Msg값을 가지는 열거형이다.
     * */
    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public enum CommonResponse{
        SUCCESS(0,"성공하였습니다."),
        FAIL(-1,"실패하였습니다.");

        private int code;
        private String msg;
    }

}
