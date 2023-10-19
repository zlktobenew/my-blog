package com.zlk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.domain.entity.RoleMenu;

public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}