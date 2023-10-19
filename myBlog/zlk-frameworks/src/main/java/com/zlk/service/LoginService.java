package com.zlk.service;

import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);


    ResponseResult logout();
}
