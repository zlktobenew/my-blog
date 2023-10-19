package com.zlk.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.Category;
import com.zlk.domain.vo.ExcelCategoryVo;
import com.zlk.enums.AppHttpCodeEnum;
import com.zlk.service.CategoryService;
import com.zlk.utlis.BeanCopyUtils;
import com.zlk.utlis.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//文章分类接口
@RestController
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }


}
