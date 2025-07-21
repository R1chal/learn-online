package com.richal.learnonline.result;

import com.richal.learnonline.constant.ErrorCode;
import lombok.Data;

/**
 * 封装 Rest 接口返回结果
 *
 * @author Richal
 * @since 2025/07/21
 */
@Data
public class JSONResult {

    private boolean success = true;

    private String message = "成功";

    //错误码，用来描述错误类型 ，20000 表示没有错误
    private String code = "20000";

    //返回的数据
    private Object data;

    public static JSONResult success(){
        return new JSONResult();
    }

    public static JSONResult success(Object obj){
        JSONResult instance = new JSONResult();
        instance.setData(obj);
        return instance;
    }

    public static JSONResult success(Object obj,String code){
        JSONResult instance = new JSONResult();
        instance.setCode(code);
        instance.setData(obj);
        return instance;
    }

    public static JSONResult error(String message,String code){
        JSONResult instance = new JSONResult();
        instance.setMessage(message);
        instance.setSuccess(false);
        instance.setCode(code);
        return instance;
    }

    public static JSONResult error(){
        JSONResult jsonResult = new JSONResult();
        jsonResult.setSuccess(false);
        return jsonResult;
    }

    public static JSONResult error(String message){
        return error(message,null);
    }

    /**
     * 自定义方法
     *
     * @param errorCode errorCode
     * @return result
     */
    public static JSONResult error(ErrorCode errorCode){
        return error(errorCode.getMessage(),errorCode.getCode());
    }

}
