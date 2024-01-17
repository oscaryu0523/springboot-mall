package com.oscar.springbootmall.service;

import com.oscar.springbootmall.dto.UserLoginRequest;
import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User login(UserLoginRequest userLoginRequest);

}
