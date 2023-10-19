package com.zlk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.Link;
import com.zlk.domain.vo.PageVo;


/**
 * 友链(Link)表服务接口
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-22 16:22:31
 */
public interface LinkService extends IService<Link> {

 ResponseResult getAllLink() ;

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
