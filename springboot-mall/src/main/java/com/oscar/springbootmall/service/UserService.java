package com.oscar.springbootmall.service;

import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;

public interface UserService {
    public Integer register(UserRegisterRequest userRegisterRequest);
    public User getUserById(Integer userId);
}
