package com.oscar.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscar.springbootmall.dto.BuyItem;
import com.oscar.springbootmall.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper=new ObjectMapper();

    //生成訂單
    @Transactional
    @Test
    public void createOrder_success() throws Exception{
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();
        BuyItem buyItem = new BuyItem();
        buyItem.setProductId(1);
        buyItem.setQuantity(5);
        buyItemList.add(buyItem);

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(2);
        buyItem1.setQuantity(2);
        buyItemList.add(buyItem1);

        createOrderRequest.setBuyItemList(buyItemList);

        String json= objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/createOrder",2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.orderId",notNullValue()))
                .andExpect(jsonPath("$.userId",equalTo(2)))
                .andExpect(jsonPath("$.totalAmount",equalTo(750)))
                .andExpect(jsonPath("$.orderItemList", hasSize(2)))
                .andExpect(jsonPath("$.createdDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate",notNullValue()));
    }
    @Transactional
    @Test
    public void createOrder_userNotExist() throws Exception{
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();
        createOrderRequest.setBuyItemList(buyItemList);

        String json= objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/createOrder",50)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
    @Transactional
    @Test
    public void createOrder_productNotExist() throws Exception{
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem = new BuyItem();
        buyItem.setProductId(50);
        buyItem.setQuantity(5);
        buyItemList.add(buyItem);

        createOrderRequest.setBuyItemList(buyItemList);

        String json= objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/createOrder",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
    @Transactional
    @Test
    public void createOrder_stockNotEnough() throws Exception{
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem = new BuyItem();
        buyItem.setProductId(1);
        buyItem.setQuantity(1000);
        buyItemList.add(buyItem);

        createOrderRequest.setBuyItemList(buyItemList);

        String json= objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/createOrder",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    //查詢訂單列表
    @Test
    public void getOrders_success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/userOrders",1);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit",notNullValue()))
                .andExpect(jsonPath("$.offset",notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results",hasSize(2)))
                .andExpect(jsonPath("$.results[0].orderId",notNullValue()))
                .andExpect(jsonPath("$.results[0].userId",equalTo(1)))
                .andExpect(jsonPath("$.results[0].totalAmount",equalTo(100000)))
                .andExpect(jsonPath("$.results[0].orderItemList",hasSize(1)))
                .andExpect(jsonPath("$.results[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].orderId",notNullValue()))
                .andExpect(jsonPath("$.results[1].userId",equalTo(1)))
                .andExpect(jsonPath("$.results[1].totalAmount",equalTo(500690)))
                .andExpect(jsonPath("$.results[1].orderItemList",hasSize(3)))
                .andExpect(jsonPath("$.results[1].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[1].lastModifiedDate", notNullValue()));
    }
    @Test
    public void getOrders_pagination() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/userOrders",1)
                .param("limit","2")
                .param("offset","2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit",notNullValue()))
                .andExpect(jsonPath("$.offset",notNullValue()))
                .andExpect(jsonPath("$.total",notNullValue()))
                .andExpect(jsonPath("$.results",hasSize(0)));

    }
    @Test
    public void getOrders_userHasNoOrder() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/userOrders",2);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit",notNullValue()))
                .andExpect(jsonPath("$.offset",notNullValue()))
                .andExpect(jsonPath("$.total",notNullValue()))
                .andExpect(jsonPath("$.results",hasSize(0)));
    }
    @Test
    public void getOrders_userNoExist() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/userOrders",100);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit",notNullValue()))
                .andExpect(jsonPath("$.offset",notNullValue()))
                .andExpect(jsonPath("$.total",notNullValue()))
                .andExpect(jsonPath("$.results",hasSize(0)));
    }

}