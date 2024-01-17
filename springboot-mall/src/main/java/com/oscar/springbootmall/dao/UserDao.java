package com.oscar.springbootmall.dao;

import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;

public interface UserDao {
    public Integer register(UserRegisterRequest userRegisterRequest);
    public User getUserById(Integer userId);

}
