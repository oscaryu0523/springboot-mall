package com.oscar.springbootmall.service;

import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;
import org.springframework.stereotype.Component;


public interface ProductService {
    Product getByProductId(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
}
