package com.oscar.springbootmall.dao;


import com.oscar.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
