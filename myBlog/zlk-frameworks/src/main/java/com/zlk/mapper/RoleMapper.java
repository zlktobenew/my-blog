package com.zlk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlk.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-25 09:28:35
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}
