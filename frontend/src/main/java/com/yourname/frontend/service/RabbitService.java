package com.yourname.frontend.service;

import com.yourname.backen.util.JsonMapper;
import com.yourname.frontend.common.QueueConstants;
import com.yourname.frontend.mq.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/11/2022 11:47 PM
 */

@Component
@Slf4j
public class RabbitService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(MessageBody messageBody) {

        try {
            String uuid = UUID.randomUUID().toString();
            CorrelationData correlationData = new CorrelationData(uuid);
            rabbitTemplate.convertAndSend(QueueConstants.COMMON_EXCHANGE, QueueConstants.COMMON_ROUTING,
                    JsonMapper.obj2String(messageBody), message -> {
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);//消息持久化
                        log.info("消息发送成功{}", message);
                        return message;
                    }, correlationData);
        } catch (AmqpException e) {
            log.error("消息发送失败", e);
        }
    }

    public void sendDelay(MessageBody messageBody, int delayMillSeconds) {
        try {
            messageBody.setDelay(delayMillSeconds);
            String uuid = UUID.randomUUID().toString();
            CorrelationData correlationData = new CorrelationData(uuid);
            rabbitTemplate.convertAndSend(QueueConstants.DELAY_EXCHANGE, QueueConstants.DELAY_ROUTING,
                    JsonMapper.obj2String(messageBody), message -> {
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);//消息持久化
                        message.getMessageProperties().setDelay(delayMillSeconds);//设置消息过期时间
                        log.info("延迟消息发送成功{}", message);
                        return message;
                    }, correlationData);
        } catch (AmqpException e) {
            log.error("延迟消息发送失败", e);

        }
    }


}

