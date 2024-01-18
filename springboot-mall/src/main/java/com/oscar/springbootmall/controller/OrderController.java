package com.oscar.springbootmall.controller;

import com.oscar.springbootmall.dto.BuyItem;
import com.oscar.springbootmall.dto.CreateOrderRequest;
import com.oscar.springbootmall.model.Order;
import com.oscar.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){

        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order=orderService.getOrderById(orderId);


        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
