package com.konosuba.file.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author konosuba
 */
@RestController
@RequestMapping("/files")
public class FilesController {
    /**
     * 文件上传
     * 要用到spring自带的MultipartFile类
     * @RequestParam("file") 中的file参数与你使用的上传控件的名称相对应
     * 即<input type="file" name="file"/>中的name的值一致
     *
     * 这样就是最简单的文件上传了，但是默认上传文件大小只有1M，大的就不能传了
     * 想要传更大的文件就要在配置文件里面设置一下
     */
    @PostMapping("/upload")
    public void upload(@RequestParam("file")MultipartFile file) throws Exception {
        //如果这里没有设置路径的话，会默认保存到项目的根目录下
        // getOriginalFilename()获取文件名
        String fileName = file.getOriginalFilename();
        if(fileName == null){
            throw new Exception("文件名为空");
        }
        // 获取文件名后缀
        String extension = fileName.substring(fileName.lastIndexOf("."));
        // 生成新的文件名
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + extension;
        String filePath = "D:/upload/" + newFileName;
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        //将上传的文件转为字节流保存下来
        outputStream.write(file.getBytes());
        outputStream.flush();
        outputStream.close();
    }
    /**
     * 文件下载
     * 要用到spring的ResponseEntity类
     */
    @GetMapping("/download")
    public ResponseEntity download() throws Exception {
        //设置选择下载的文件
        FileSystemResource file = new FileSystemResource("A标准2寸.jpg");
        HttpHeaders headers = new HttpHeaders();
        String name = "123.jpg";
        // 头文件的值，用来为下载文件重命名
        String value = "attachment; filename=" + name;
        //在响应头中添加这个，设置下载文件默认的名称
        headers.add("Content-Disposition", value);
        return ResponseEntity.ok().headers(headers).contentLength(file.contentLength()).contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
