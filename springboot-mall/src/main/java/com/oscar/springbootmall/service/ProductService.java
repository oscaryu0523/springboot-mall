package com.oscar.springbootmall.service;

import com.oscar.springbootmall.constant.ProductCategory;
import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;


public interface ProductService {
    List<Product> getProducts(ProductCategory category, String search);
    Product getByProductId(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProduct(Integer productId);

}
