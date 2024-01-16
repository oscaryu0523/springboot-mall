package com.oscar.springbootmall.controller;

import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    //根據商品編號查詢單一商品
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getByProductId(productId);

        if(product !=null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    //新增單一商品
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Integer productId = productService.createProduct(productRequest);

        Product product=productService.getByProductId(productId);
        return  ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    //根據商品編號更新單一商品
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){
        Product product = productService.getByProductId(productId);
        //檢查product是否存在
        if(product!=null) {
            //如果商品存在，則修改商品
            productService.updateProduct(productId, productRequest);
            Product updatedProduct = productService.getByProductId(productId);

            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        }else{
            //如果商品不存在，返回404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
