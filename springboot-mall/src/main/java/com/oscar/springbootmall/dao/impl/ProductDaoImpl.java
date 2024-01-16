package com.oscar.springbootmall.dao.impl;

import com.oscar.springbootmall.dao.ProductDao;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.rowmapper.ProdcutRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    //用編號查詢一個商品
    @Override
    public Product getProductById(Integer productId) {
        //sql指令
        String sql="select product_id,product_name, category, image_url, price, stock, description," +
                " created_date, last_modified_date " +
                "from product where product_id=:productId";
        //創建map集合放入篩選條件
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        //執行查詢語句，經過rowmapper返回java物件
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProdcutRowMapper());
        //如果及何不為空，返回第一個物件
        if(productList.size()>0){
            return productList.get(0);
        }else{
            return null;
        }
    }
}
