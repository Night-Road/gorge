package com.yourname.backen.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 数据返回的固定格式
 * @Author dell
 * @Date 10/18/2022 9:53 AM
 */
@Getter
@Setter
public class R {

    private final static int SYSTEM_ERROR = 1;
    private boolean ret;//是否成功
    private String msg;
    private Object data;
    private int code;//状态码


    public R(boolean ret){
        this.ret = ret;
    }
    public static R success(){
        return new R(true);
    }
    public static R success(Object data){
        R r = new R(true);
        r.setData(data);
        return r;
    }
    public static R success(Object data,String msg){
        R r = new R(true);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
    public static R success(String msg){
        R r = new R(true);
        r.setMsg(msg);
        return r;
    }
    public static R failed(String msg){
        R r = new R(false);
        r.setCode(SYSTEM_ERROR);
        r.setMsg(msg);
        return r;
    }

    /**
     *  其他啊失败的状态码
     * @param code
     * @param msg
     * @return
     */
    public static R failed(int code,String msg){
        R r = new R(false);
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
