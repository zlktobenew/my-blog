package com.zlk.service;

import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
