package com.oscar.springbootmall.dao.impl;

import com.oscar.springbootmall.constant.ProductCategory;
import com.oscar.springbootmall.dao.ProductDao;
import com.oscar.springbootmall.dto.ProductQueryParams;
import com.oscar.springbootmall.dto.ProductRequest;
import com.oscar.springbootmall.model.Product;
import com.oscar.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    //查詢商品總數
    @Override
    public Integer countTotal(ProductQueryParams productQueryParams) {
        String sql="SELECT count(*) FROM product WHERE 1=1";

        HashMap<String, Object> map = new HashMap<>();

        ProductCategory category = productQueryParams.getCategory();
        String search = productQueryParams.getSearch();

        //分類篩選及產品名模糊查詢
        sql = addFilteringSql(sql,map,productQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }


    //商品列表(複合查詢、排序、分頁)
    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "select product_id,product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "from product where 1=1";

        HashMap<String, Object> map = new HashMap<>();

        //分類篩選及產品名模糊查詢
        sql = addFilteringSql(sql,map,productQueryParams);
        //排序
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();
        //分頁
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit",productQueryParams.getLimit());
        map.put("offset",productQueryParams.getOffset());



        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;
    }

    //用編號查詢一個商品
    @Override
    public Product getProductById(Integer productId) {
        //sql指令
        String sql = "select product_id,product_name, category, image_url, price, stock, description," +
                " created_date, last_modified_date " +
                "from product where product_id=:productId";
        // 創建一個HashMap來儲存SQL語句中的參數和它們對應的值
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        //執行查詢語句，經過rowmapper返回java物件
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        //如果集合不為空，返回第一個物件
        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    //新增一個商品
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createDate, :lastModifiedDate)";
        // 創建一個HashMap來儲存SQL語句中的參數和它們對應的值
        HashMap<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createDate", now);
        map.put("lastModifiedDate", now);

        // 用於接收自動生成的主鍵（產品ID）
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        // 執行新增操作，並將生成的主鍵保存到keyHolder中
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        // 從keyHolder中獲取產品ID，並返回該ID
        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    //更新一個商品
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name =:productName, category =:category, " +
                "image_url =:imageUrl, price =:price, stock =:stock, description =:description, " +
                "last_modified_date =:lastModifiedDate " +
                "where product_id =:productId";
        // 創建一個HashMap來儲存SQL語句中的參數和它們對應的值
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        //更新最後修改時間
        map.put("lastModifiedDate", new Date());
        // 執行新增操作
        namedParameterJdbcTemplate.update(sql, map);
    }

    //更新庫存數量
    @Override
    public void updateStock(Integer productId, Integer stock) {
        String sql="UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate " +
                "WHERE product_id =:productId";

        HashMap<String, Object> map = new HashMap<>();
        map.put("stock", stock);
        map.put("lastModifiedDate",new Date());
        map.put("productId",productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    //刪除一個商品
    @Override
    public void deleteProduct(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id=:productId";
        // 創建一個HashMap來儲存SQL語句中的參數和它們對應的值
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        // 執行刪除操作
        namedParameterJdbcTemplate.update(sql, map);
    }

    //分類篩選及產品名模糊查詢
    private String addFilteringSql(String sql, Map <String,Object> map, ProductQueryParams productQueryParams){
        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }
        //產品名模糊查詢
        if (productQueryParams.getSearch() != null) {
            sql += " AND product_Name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }
        return sql;
    }

}
