package com.games.tap.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Echo {
    @Schema(example = "0")
    private Integer code;
    @Schema(example = "OK")
    private String message;
    private Object data;

    Echo(RetCode rtc,Object data){
        setCode(rtc.getCode());
        setMessage(rtc.getMessage());
        setData(data);
    }
    Echo(RetCode rtc){
        setCode(rtc.getCode());
        setMessage(rtc.getMessage());
        setData(null);
    }
    Echo(Integer code,String msg,Object data){
        setCode(code);
        setMessage(msg);
        setData(data);
    }
    Echo(Integer code,String msg){
        setCode(code);
        setMessage(msg);
        setData(null);
    }

    public static Echo success(){
        return new Echo(RetCode.SUCCESS);
    }

    public static Echo success(Object data){
        return new Echo(RetCode.SUCCESS,data);
    }

    public static Echo fail(){
        return new Echo(RetCode.FAILURE);
    }

    public static Echo fail(Object data){
        return new Echo(RetCode.FAILURE,data);
    }

    public static Echo fail(String msg ,Object data){
        return new Echo(RetCode.FAILURE.getCode(),msg,data);
    }

    public static Echo fail(String msg){
        return new Echo(RetCode.FAILURE.getCode(),msg);
    }

    public static Echo define(Integer code,String msg,Object data){
        return new Echo(code,msg,data);
    }

    public static Echo define(RetCode rtc){
        return new Echo(rtc);
    }
}
