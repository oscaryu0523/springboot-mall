package com.oscar.springbootmall.dao.impl;

import com.oscar.springbootmall.dao.UserDao;
import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;
import com.oscar.springbootmall.rowmapper.UserRowMapper;
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
public class UserDaoImpl implements UserDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    //新增一筆帳號資料
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        String sql="INSERT INTO user (email, password, created_date, last_modified_date) " +
                "VALUES (:email, :password, :createdDate, :lastModifiedDate)";
        Map<String, Object> map = new HashMap<>();
        map.put("email",userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());
        Date now=new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();

        return userId;
    }
    //根據userId查詢一筆帳號資料
    @Override
    public User getUserById(Integer userId) {
        String sql="SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM user WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();

        map.put("userId",userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size() > 0){
            return userList.get(0);
        }else {
             return null;
        }
    }
}