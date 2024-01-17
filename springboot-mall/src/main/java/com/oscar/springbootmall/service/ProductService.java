package com.oscar.springbootmall.service;

import com.oscar.springbootmall.dto.ProductQueryParams;
import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;

import java.util.List;


public interface ProductService {
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getByProductId(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProduct(Integer productId);

}
