package com.zlk.utlis;

import com.zlk.domain.entity.Article;
import com.zlk.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

//拷贝工具类
public class BeanCopyUtils {
    private BeanCopyUtils(){

    }
    // 泛型来实现
    // Class clazz 通过字节码反射方式创建一个目标对象，再实现拷贝
    public static <V, T> T copyBean(V source, Class<T> clazz) {
        // 创建目标对象
        T result = null;
        try {
            result = clazz.newInstance();
            // 实现属性拷贝，通过Bean里面的工具类来拷贝
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回结果
        return result;
    }

    // 拷贝数组对象
    public static <V, T> List<T> copyBeanList(List<V> list, Class<T> clazz) {
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("ss");
        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
