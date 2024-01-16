package com.oscar.springbootmall.dao;


import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
}
