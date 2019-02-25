package com.nowcode.toutiao.controller;


import com.nowcode.toutiao.model.*;
import com.nowcode.toutiao.service.CommentService;
import com.nowcode.toutiao.service.NewsService;
import com.nowcode.toutiao.service.UserService;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.apache.catalina.Host;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        if(news != null){
            //评论
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<>();
            for(Comment comment: comments){
                ViewObject commentVO = new ViewObject();
                commentVO.set("comment", comment);
                commentVO.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(commentVO);
            }
            model.addAttribute("comments", commentVOs);

        }
        model.addAttribute("news", news);//新闻
        model.addAttribute("owner", userService.getUser(news.getUserId()));//作者
        return "detail";
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})//增加评论数
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(newsId, count);

        } catch (Exception e) {
            //     logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})//展示图片，打开/image+文件名可以看到图片
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

    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})//添加新闻,由于没有缩图所以图片显示都是失败，缩图使用云存储
    @ResponseBody
    public String addNew(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            News news = new News();
            if(hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else {
                //匿名id
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加咨询错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
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
