package com.yourname.frontend.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/10/2022 10:08 AM
 */
public class DesensitizedUtils {
    public static String hideIdNumber(String msg){
        if(StringUtils.isBlank(msg)|| msg.length()<7){
            return msg;
        }
        return msg.substring(0,3)+"******"+msg.substring(msg.length()-3);
    }
}
