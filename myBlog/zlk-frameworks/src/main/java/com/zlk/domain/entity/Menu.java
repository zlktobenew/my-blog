package com.zlk.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 菜单权限表(Menu)表实体类
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-25 08:58:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
@Accessors(chain = true)
//@Accessors(chain = true)注解开启链式编程，set方法会有返回值，返回这个对象本身
/* public void setId(Long id){
        this.id=id;
    }变为
    public Menu setId(Long id){
        this.id=id;
        return this;}*/
public class Menu {
    //菜单ID
    @TableId
    private Long id;
    //菜单名称

    private String menuName;
    //父菜单ID
    private Long parentId;
    //显示顺序
    private Integer orderNum;
    //路由地址
    private String path;
    //组件路径
    private String component;
    //是否为外链（0是 1否）
    private Integer isFrame;
    //菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    //菜单状态（0显示 1隐藏）
    private String visible;
    //菜单状态（0正常 1停用）
    private String status;
    //权限标识
    private String perms;
    //菜单图标
    private String icon;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    //备注
    private String remark;
    
    private String delFlag;

    //这里表示在查询的时候，告诉mybatis-plus这个字段是不存在的，所以查询的时候就不会去使用这个字段。
    //不然在查询的时候，mybatis-plus会根据实体类去查询这里的每一个字段，但是数据库没有这个字段，所以会报错。
    //但是在返回前端接口的时候，需要返回这个字段
    //解决方法有2个，一个是封装一个MenuVo,另外一个就是使用下面的注解
    @TableField(exist = false)
    private List<Menu> children;
}
