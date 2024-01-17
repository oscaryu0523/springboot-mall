package com.oscar.springbootmall.dao;

import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User getUserByEmail(String email);

}
