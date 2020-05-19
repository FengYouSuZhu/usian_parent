package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/19 16:35
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    private FastFileStorageClient storageClient;

    private  static  final List<String> contentTypeList= Arrays.asList("image/jpeg","image/gif");

    @RequestMapping("/upload")
    public Result fileUpload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        //校验文件的类型
        String contentType = file.getContentType();
        if(!contentTypeList.contains(contentType)){
            // 文件类型不合法，直接返回
            return Result.error("文件类型不合法:"+filename);
        }

        //校验文件的内容
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage ==null){
            return Result.error("文件内容不合法：" + filename);
        }
        // 保存到服务器
        String substring = filename.substring(filename.lastIndexOf("."));

        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), substring, null);
        // 生成url地址，返回
        return Result.ok("http://image.usian.com/" + storePath.getFullPath());

       // return Result.error("服务器内部错误");
    }

}
