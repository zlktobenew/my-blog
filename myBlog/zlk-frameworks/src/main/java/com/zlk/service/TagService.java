package com.zlk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.TagVo;
import com.zlk.domain.dto.TagListDto;
import com.zlk.domain.entity.Tag;
import com.zlk.domain.vo.PageVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-24 22:27:37
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addList(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult getTag(Long id);

    List<TagVo> listAllTag();
}
