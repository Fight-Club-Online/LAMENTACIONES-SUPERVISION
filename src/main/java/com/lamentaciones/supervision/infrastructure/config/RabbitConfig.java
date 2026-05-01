package com.lamentaciones.supervision.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String SUPERVISION_EXCHANGE = "supervision.events";
    public static final String AUTH_EXCHANGE = "user.events";
    public static final String CHAT_MESSAGES_QUEUE = "supervision.chat.messages";
    public static final String USER_BANNED_AUTH_QUEUE = "auth.user.banned.queue";
    public static final String USER_SUSPENDED_AUTH_QUEUE = "auth.user.suspended.queue";
    public static final String BAN_LIFTED_AUTH_QUEUE = "auth.ban.lifted.queue";

    @Bean
    public TopicExchange supervisionExchange() {
        return new TopicExchange(SUPERVISION_EXCHANGE);
    }

    @Bean
    public Queue chatMessagesQueue() {
        return new Queue(CHAT_MESSAGES_QUEUE, true);
    }

    @Bean
    public Queue userBannedAuthQueue() {
        return new Queue(USER_BANNED_AUTH_QUEUE, true);
    }

    @Bean
    public Queue userSuspendedAuthQueue() {
        return new Queue(USER_SUSPENDED_AUTH_QUEUE, true);
    }

    @Bean
    public Queue banLiftedAuthQueue() {
        return new Queue(BAN_LIFTED_AUTH_QUEUE, true);
    }

    @Bean
    public Binding userBannedBinding(Queue userBannedAuthQueue, TopicExchange supervisionExchange) {
        return BindingBuilder.bind(userBannedAuthQueue)
            .to(supervisionExchange).with("user.banned");
    }

    @Bean
    public Binding userSuspendedBinding(Queue userSuspendedAuthQueue, TopicExchange supervisionExchange) {
        return BindingBuilder.bind(userSuspendedAuthQueue)
            .to(supervisionExchange).with("user.suspended");
    }

    @Bean
    public Binding banLiftedBinding(Queue banLiftedAuthQueue, TopicExchange supervisionExchange) {
        return BindingBuilder.bind(banLiftedAuthQueue)
            .to(supervisionExchange).with("ban.lifted");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}