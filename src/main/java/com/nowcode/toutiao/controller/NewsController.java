package com.nowcode.toutiao.controller;


import com.nowcode.toutiao.service.NewsService;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})//图片下载
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }


    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})//图片上传
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file){//二进制流
        try{
            String fileUrl = newsService.saveImage(file);
            if(fileUrl == null){
                return ToutiaoUtil.getJSONString(1, "上传失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        }catch (Exception e){
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }
}
