package com.example.microservices.service;

import com.example.microservices.model.Order;
import com.example.microservices.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;

    public void createOrder(Order order) {
        orderRepository.save(order);
        rabbitTemplate.convertAndSend("order_exchange", "", order); // Fanout exchange не использует routing key
    }
}
