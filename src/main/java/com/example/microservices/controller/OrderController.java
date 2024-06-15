package com.example.microservices.controller;

import com.example.microservices.model.Order;
import com.example.microservices.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {

    private  final OrderService orderService;
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return ResponseEntity.ok(order);
    }

    @RabbitListener(queues = "notification_queue")
    public void receiveOrderNotification(Order order) {
        System.out.println("Received order: " + order);
        // Обработка полученного сообщения, если требуется.
    }
}
