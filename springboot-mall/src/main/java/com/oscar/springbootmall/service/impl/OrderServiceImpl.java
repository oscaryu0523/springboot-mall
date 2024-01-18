package com.oscar.springbootmall.service.impl;

import com.oscar.springbootmall.dao.OrderDao;
import com.oscar.springbootmall.dao.ProductDao;
import com.oscar.springbootmall.dao.UserDao;
import com.oscar.springbootmall.dto.BuyItem;
import com.oscar.springbootmall.dto.CreateOrderRequest;
import com.oscar.springbootmall.model.Order;
import com.oscar.springbootmall.model.OrderItem;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.model.User;
import com.oscar.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    //生成一筆訂單
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //檢查user是否存在
        User user = userDao.getUserById(userId);

        if(user == null){
            log.warn("該userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        List<OrderItem> orderItemList= new ArrayList<>();
        int totalAmount = 0;

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            //檢查商品是否存在，庫存是否足夠
            if(product == null){
                log.warn("商品 {} 不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 庫存不足，無法購買，剩餘庫存 {} ，欲購買數量 {}",
                        buyItem.getProductId(),product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //扣除商品庫存
            productDao.updateStock(product.getProductId(),product.getStock() - buyItem.getQuantity());

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
