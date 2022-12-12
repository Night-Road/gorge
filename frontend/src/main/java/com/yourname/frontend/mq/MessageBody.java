package com.yourname.frontend.mq;

import lombok.Data;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/11/2022 11:16 PM
 */
@Data
public class MessageBody {
    private int topic;
    private int delay;
    private long sendTime = System.currentTimeMillis();

    private String detail;
}
