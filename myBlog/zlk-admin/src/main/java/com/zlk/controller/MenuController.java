package com.zlk.controller;


import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.MenuTreeVo;
import com.zlk.domain.adminVo.RoleMenuTreeSelectVo;
import com.zlk.domain.entity.Menu;
import com.zlk.domain.vo.MenuVo;
import com.zlk.service.MenuService;
import com.zlk.utlis.BeanCopyUtils;
import com.zlk.utlis.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/system/menu")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult getAllMenu(Menu menu){
        List<Menu> menus=menuService.getAllMenu(menu);
        return ResponseResult.okResult(menus);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.update1Menu(menu);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult deteleMenu1(@PathVariable Long menuId){
        return menuService.deteleMenu1(menuId);
    }
    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus =  menuService.getAllMenu(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }
    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus =  menuService.getAllMenu(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }

}
