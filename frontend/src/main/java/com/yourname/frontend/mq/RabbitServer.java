package com.yourname.frontend.mq;

import com.yourname.frontend.common.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description 消费者
 * @Author dell
 * @Date 12/12/2022 7:37 PM
 */

@Component
@Slf4j
public class RabbitServer {

    @RabbitListener(queues = QueueConstants.COMMON_QUEUE)
    public void receive(String message){
        log.info("已经接收消息{}",message);
    }
}
