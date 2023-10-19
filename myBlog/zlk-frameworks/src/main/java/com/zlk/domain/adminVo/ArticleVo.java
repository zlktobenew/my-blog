package com.zlk.domain.adminVo;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    private Integer pageNum;
    private Integer pageSize;

    private String title;

    private String summary;
}
