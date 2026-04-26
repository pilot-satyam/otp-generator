package org.otp.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String MAIN_QUEUE = "otp.queue";
    public static final String DLQ = "otp.dlq";
    public static final String RETRY_QUEUE = "otp.retry";

    @Bean
    public Queue queue(){
        return QueueBuilder.durable(MAIN_QUEUE).build();
    }

    @Bean
    public Queue retryQueue(){
        return QueueBuilder.durable(RETRY_QUEUE)
                .withArgument("x-message-ttl", 10000) // 10 sec delay
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", MAIN_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue(){
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
