package com.yourname.backen.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {

    public static List<Long> splitToListLong(String str) {
        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return stringList.stream().map(item -> Long.parseLong(item)).collect(Collectors.toList());
    }

    public static List<Integer> splitToListInt(String str) {
        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return stringList.stream().map(item -> Integer.parseInt(item)).collect(Collectors.toList());
    }
    /**
     * 下划线转驼峰
     */
    public static String camelName(String name) {

        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


}
