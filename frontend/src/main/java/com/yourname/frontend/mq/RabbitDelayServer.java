package com.yourname.frontend.mq;

import com.yourname.frontend.common.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/12/2022 7:38 PM
 */

@Component
@Slf4j
public class RabbitDelayServer {

    @RabbitListener(queues = QueueConstants.DELAY_QUEUE)
    public void receive(String message){
        log.info("已经接收延迟消息{}",message);
    }
}
