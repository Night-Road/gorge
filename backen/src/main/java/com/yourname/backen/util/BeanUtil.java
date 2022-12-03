package com.yourname.backen.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description TODO
 * @Author dell
 * @Date 11/6/2022 11:18 AM
 */
@Slf4j
public class BeanUtil {
    public static void setFieldValueByName(Object obj, String fieldName, Object value) {
        try {
            //System.out.println(value.getClass().toString());
            // 获取obj类的字节文件对象
            Class c = obj.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            if (f.getType() == Integer.class) {
                f.set(obj, Integer.parseInt((String) value));
            } else if (f.getType() == Long.class) {
                f.set(obj, Long.parseLong((String) value));
            } else if (f.getType() == LocalDateTime.class) {
                LocalDateTime time = LocalDateTime.parse((String)value, DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                        "HH:mm:ss"));
                f.set(obj, time);
            } else {
                f.set(obj, value);
            }
        } catch (Exception e) {
            log.error("无法添加属性值！");
            System.out.println(e.getMessage());
        }
    }
}
