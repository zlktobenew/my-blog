package com.zlk.controller;

import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.TagVo;
import com.zlk.domain.dto.TagListDto;
import com.zlk.domain.entity.Tag;
import com.zlk.domain.vo.PageVo;
import com.zlk.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        //不直接使用page方法的原因，因为返回的相应格式要是PageVo，还有TagListDto里面的2个属性可能有值也可能没值，
        //所以只有在name的长度大于0，和remark不为空才进行查询
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addList(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        return tagService.getTag(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    //查看所有tag,PreAuthorize为权限判断,hasPermission为权限判断方法
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
