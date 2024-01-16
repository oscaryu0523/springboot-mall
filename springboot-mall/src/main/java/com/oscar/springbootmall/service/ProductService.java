package com.oscar.springbootmall.service;

import com.oscar.springbootmall.model.Product;
import org.springframework.stereotype.Component;


public interface ProductService {
    Product getByProductId(Integer productId);
}
