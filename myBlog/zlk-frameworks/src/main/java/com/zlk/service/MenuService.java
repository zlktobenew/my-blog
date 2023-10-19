package com.zlk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.Menu;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-25 09:00:40
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> getAllMenu(Menu menu);

    ResponseResult getMenuById(Long id);

    ResponseResult addMenu(Menu menu);

    ResponseResult update1Menu(Menu menu);

    ResponseResult deteleMenu1(Long id);


    List<Long> selectMenuListByRoleId(Long roleId);
}
