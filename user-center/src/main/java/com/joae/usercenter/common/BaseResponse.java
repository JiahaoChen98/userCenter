package com.joae.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T> 数据类型
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description=description;
    }

    public BaseResponse(int code, T data,String message) {
        this(code,data,message,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(ErrorCode errorcode){
        this(errorcode.getCode(),null,errorcode.getMessage(),errorcode.getDescription());
    }
}
