package com.yourname.frontend.configuration;

import com.yourname.frontend.common.QueueConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/11/2022 11:39 PM
 */
@Configuration
public class RabbitDelayConfig {

    @Bean("delayDirectExchange")
    public DirectExchange delayDirectExchange() {
        DirectExchange delayDirectExchange = new DirectExchange(QueueConstants.DELAY_EXCHANGE, true, false);
        delayDirectExchange.setDelayed(true);
        return delayDirectExchange;
    }

    @Bean("delayNotifyQueue")
    public Queue delayNotifyQueue() {
        return new Queue(QueueConstants.DELAY_QUEUE);
    }

    @Bean("delayBindingNotify")
    public Binding delayBindingNotify(@Qualifier("delayDirectExchange") DirectExchange delayDirectExchange,
                                      @Qualifier("delayNotifyQueue") Queue delayNotifyQueue) {
        return BindingBuilder.bind(delayNotifyQueue).to(delayDirectExchange).with(QueueConstants.DELAY_ROUTING);
    }
}
