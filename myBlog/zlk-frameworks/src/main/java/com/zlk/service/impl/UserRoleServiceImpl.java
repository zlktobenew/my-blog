package com.zlk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlk.domain.adminVo.UserRole;
import com.zlk.mapper.UserRoleMapper;
import com.zlk.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
