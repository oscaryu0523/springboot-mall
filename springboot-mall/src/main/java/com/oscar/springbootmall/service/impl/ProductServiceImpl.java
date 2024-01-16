package com.oscar.springbootmall.service.impl;

import com.oscar.springbootmall.dao.ProductDao;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    public Product getByProductId(Integer productId) {
        return productDao.getProductById(productId);
    }
}