package com.nowcode.toutiao.controller;


import com.nowcode.toutiao.service.NewsService;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
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
