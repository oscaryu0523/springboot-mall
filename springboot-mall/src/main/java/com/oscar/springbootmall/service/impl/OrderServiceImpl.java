package com.oscar.springbootmall.service.impl;

import com.oscar.springbootmall.dao.OrderDao;
import com.oscar.springbootmall.dao.ProductDao;
import com.oscar.springbootmall.dto.BuyItem;
import com.oscar.springbootmall.dto.CreateOrderRequest;
import com.oscar.springbootmall.model.Order;
import com.oscar.springbootmall.model.OrderItem;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    //生成一筆訂單
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        List<OrderItem> orderItemList= new ArrayList<>();
        int totalAmount = 0;

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());
            //計算訂單總金額
            Integer amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            //將buyItem轉換成orderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        //生成訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        //生成訂單明細
        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
    //根據訂單編號查詢一筆訂單
    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemlist=orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemlist);

        return order;
    }
}
