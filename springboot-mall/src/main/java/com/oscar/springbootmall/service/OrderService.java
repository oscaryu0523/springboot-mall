package com.oscar.springbootmall.service;

import com.oscar.springbootmall.dto.CreateOrderRequest;
import com.oscar.springbootmall.model.Order;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);
}
