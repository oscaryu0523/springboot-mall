package com.oscar.springbootmall.service;

import com.oscar.springbootmall.dto.CreateOrderRequest;
import com.oscar.springbootmall.dto.OrderQueryParams;
import com.oscar.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
}
