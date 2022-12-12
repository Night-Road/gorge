package com.yourname.frontend.configuration;

import com.yourname.frontend.common.QueueConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/11/2022 11:24 PM
 */
@Configuration
public class RabbitmqConfig {


    @Bean("directExchange")
    @Primary
    public DirectExchange directExchange() {
        return new DirectExchange(QueueConstants.COMMON_EXCHANGE, true, false);
    }

    @Bean("notifyQueue")
    @Primary
    public Queue notifyQueue() {
        return new Queue(QueueConstants.COMMON_QUEUE);
    }

    @Bean("bindingNotify")
    @Primary
    public Binding bindingNotify(@Qualifier("directExchange") DirectExchange directExchange,
                                 @Qualifier("notifyQueue") Queue notifyQueue) {
        return BindingBuilder.bind(notifyQueue).to(directExchange).with(QueueConstants.COMMON_ROUTING);
    }
}
