package com.oscar.springbootmall.service.impl;

import com.oscar.springbootmall.dao.UserDao;
import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;
import com.oscar.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest){
        return userDao.register(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
