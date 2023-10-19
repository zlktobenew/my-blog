package com.zlk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.TagVo;
import com.zlk.domain.dto.TagListDto;
import com.zlk.domain.entity.Tag;
import com.zlk.domain.entity.User;
import com.zlk.domain.vo.PageVo;
import com.zlk.mapper.TagMapper;
import com.zlk.service.TagService;
import com.zlk.service.UserService;
import com.zlk.utlis.BeanCopyUtils;
import com.zlk.utlis.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-24 22:27:37
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //条件查询
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //新增tag
    @Override
    public ResponseResult addList(Tag tag) {
        Tag tagV=new Tag();
        tagV.setCreateBy(SecurityUtils.getUserId());
        tagV.setName(tag.getName());
        tagV.setRemark(tag.getRemark());
        tagMapper.insert(tagV);
        return ResponseResult.okResult();
    }

    //删除tag
    @Override
    public ResponseResult deleteTag(Long id) {
        // 创建一个UpdateWrapper对象来设置更新条件
        UpdateWrapper<Tag> wrapper = new UpdateWrapper<>();
        // 选择id为id的tag行，将del_flag置为1
        wrapper.eq("id", id).set("del_flag",1);

        // 执行逻辑删除操作
        tagMapper.update(new Tag(), wrapper);
        return ResponseResult.okResult();
    }

    //更新
    @Override
    public ResponseResult updateTag(Tag tag) {
        Long id=tag.getId();
        UpdateWrapper<Tag> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("name",tag.getName()).set("remark",tag.getRemark());

        tagMapper.update(new Tag(), wrapper);
        return ResponseResult.okResult();
    }

    //获取标签
    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = tagMapper.selectById(id);

        //转化成vo
        TagVo TagVos = BeanCopyUtils.copyBean(tag,TagVo.class);
        return ResponseResult.okResult(TagVos);
    }

    //查看所有tag
    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}
