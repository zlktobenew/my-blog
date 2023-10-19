package com.zlk.controller;

import com.zlk.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 照片上传接口
 */
@RestController
public class UploadController {
    private static final String UPLOAD_DIR = "E:\\images"; // 存储文件的目录，可以配置为可配置项

    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile multipartFile) throws IOException {
            // 获取原始文件名
            String originalFilename = multipartFile.getOriginalFilename();

            // 构造唯一的文件名(不能重复)，--uuid(通用唯一识别码)
            int index = originalFilename.lastIndexOf(".");
            String extname = originalFilename.substring(index);
            String newFileName = UUID.randomUUID().toString() + extname;
            // 将文件存储在服务器的磁盘目录下
            File file = new File(UPLOAD_DIR, newFileName);
            multipartFile.transferTo(file);
            // 返回成功消息或文件URL
            return ResponseResult.okResult(newFileName);
        }
    }







