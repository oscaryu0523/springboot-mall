package com.oscar.springbootmall.service.impl;

import com.oscar.springbootmall.dao.ProductDao;
import com.oscar.springbootmall.dto.ProductQueryParams;
import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getByProductId(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
    }

    @Override
    public Integer countTotal(ProductQueryParams productQueryParams) {
        return productDao.countTotal(productQueryParams);
    }


}
