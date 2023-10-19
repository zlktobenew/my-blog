package com.zlk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlk.constants.SystemConstants;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.Menu;
import com.zlk.enums.AppHttpCodeEnum;
import com.zlk.mapper.MenuMapper;
import com.zlk.service.MenuService;
import com.zlk.utlis.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.stream.Collectors;

@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //in代表2个都可以
            wrapper.in(Menu::getMenuType,SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);

            List<Menu> menus = list(wrapper);
            //只需要menus的perms字段
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //思路，这里使用了mapper层的连表查询，在resource层有xml文件，就是从userid中获取roleId，再根据这个rolesId获取所具有的按钮权限
        //否则返回所具有的权限，getBaseMapper返回MenuMapper
        return getBaseMapper().selectPermsByUserId(id);
    }

    //查询Menu
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员，这个方法可以直接拿到用户id，和1比较，1代表管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                //这里由于menu.setChildren(getChildren(menu, menus))是Lamda表达式，需要有返回值，但是set方法没有返回值
                //所以在menu这个类加上注解，开启链式编程，set方法会有返回值，详情看menu类
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * 递归实现找到子菜单
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //上面过滤完就是子菜单了，下面一行可有可无，防止三层子菜单，但是一般只有2层子菜单，删了就适用于2层子菜单
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    @Autowired
    private MenuMapper menuMapper;

    //查看菜单所有列表
    public List<Menu> getAllMenu(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        // 添加模糊查询条件，如果需要的话
        if (menu.getMenuName()!=null) {
            queryWrapper.like(Menu::getMenuName, menu.getMenuName());
        }

        // 添加状态查询条件，如果需要的话
        if (menu.getStatus() != null) {
            queryWrapper.eq(Menu::getStatus, menu.getStatus());
        }

        // 添加排序条件，按照父菜单id升序排列，然后按照orderNum升序排列
        queryWrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);

        // 执行查询
        List<Menu> menuList = menuMapper.selectList(queryWrapper);
        return menuList;
    }

    public ResponseResult getMenuById(Long id) {
        // 根据id查询菜单数据
        Menu menu = menuMapper.selectById(id);
        // 查询成功，返回菜单数据
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
            menuMapper.insert(menu);
            return ResponseResult.okResult();
    }
    @Lazy
    @Autowired
    private MenuService menuService;
    @Override
    public ResponseResult update1Menu(Menu menu) {
// 根据 menu 的 id 查询数据库中对应的菜单记录
        Menu existingMenu = menuMapper.selectById(menu.getId());
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CANT_SETMENU);
        }

        UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
// 设置要更新的字段和值，只更新不为 null 的属性
        updateWrapper.eq("id", menu.getId())
                .set((menu.getMenuName()!=null), "menu_name", menu.getMenuName())
                .set(menu.getParentId() != null, "parent_id", menu.getParentId())
                .set(menu.getOrderNum() != null, "order_num", menu.getOrderNum())
                .set(menu.getPath()!=null, "path", menu.getPath())
                .set(menu.getComponent()!=null, "component", menu.getComponent())
                .set(menu.getIsFrame() != null, "is_frame", menu.getIsFrame())
                .set(menu.getMenuType()!=null, "menu_type", menu.getMenuType())
                .set(menu.getStatus() != null, "status", menu.getStatus())
                .set(menu.getVisible() != null, "visible", menu.getVisible())
                .set(menu.getIcon()!=null, "icon", menu.getIcon())
                .set(menu.getPerms()!=null, "perms", menu.getPerms());

// 执行更新操作
        menuMapper.update(existingMenu, updateWrapper);

        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult deteleMenu1(Long id) {
        // 先查询要删除的菜单
        Menu menuToDelete = menuMapper.selectById(id);

        // 检查是否有子菜单
        if (hasChildMenus(id)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DONTDE_CHIL);
        }
        UpdateWrapper<Menu> wrapper = new UpdateWrapper<>();
        // 选将del_flag置为1
        wrapper.eq("id", id).set("del_flag",1);
        menuMapper.update(new Menu(),wrapper);

        return ResponseResult.okResult();
    }


    // 递归检查是否有子菜单
    private boolean hasChildMenus(Long parentId) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, parentId);
        List<Menu> childMenus = menuMapper.selectList(wrapper);
        if (childMenus.isEmpty()) {
            return false;
        } else {
            // 继续检查子菜单的子菜单
            for (Menu childMenu : childMenus) {
                if (hasChildMenus(childMenu.getId())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }
}