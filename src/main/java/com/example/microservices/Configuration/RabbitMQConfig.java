package com.example.microservices.Configuration;

import com.example.microservices.controller.OrderController;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "notification_queue";

    public static final String EXCHANGE_NAME = "order_exchange";

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, true);
    }


    @Bean
    public FanoutExchange emailExchange() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding binding(Queue emailQueue, FanoutExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange);
    }

    @Bean
    public Binding anotherBinding(Queue anotherQueue, FanoutExchange emailExchange) {
        return BindingBuilder.bind(anotherQueue).to(emailExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(OrderController receiver, Jackson2JsonMessageConverter converter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveOrderNotification");
        adapter.setMessageConverter(converter);
        return adapter;
    }
}
