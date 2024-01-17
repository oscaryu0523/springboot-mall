package com.oscar.springbootmall.service.impl;

import com.oscar.springbootmall.dao.UserDao;
import com.oscar.springbootmall.dto.UserLoginRequest;
import com.oscar.springbootmall.dto.UserRegisterRequest;
import com.oscar.springbootmall.model.User;
import com.oscar.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {
    private final static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    //註冊
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查註冊的email
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if(user != null) {
            log.warn("該 email {} 已經被註冊",userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //使用 MD5 生成密碼的雜湊值
        String hashPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashPassword);

        //創建帳號
        return userDao.createUser(userRegisterRequest);
    }
    //取得user資料
    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
    //登入
    @Override
    public User login(UserLoginRequest userLoginRequest) {
        //檢查email帳號是否註冊，確認user是否存在
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if(user==null){
            log.warn("該 email {} 尚未註冊",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //檢查密碼是否正確
        if(user.getPassword().equals(hashedPassword)) {
            return user;
        }else{
            log.warn("該 email {} 的密碼不正確",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
