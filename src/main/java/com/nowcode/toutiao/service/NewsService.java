package com.nowcode.toutiao.service;


import com.nowcode.toutiao.dao.NewsDAO;
import com.nowcode.toutiao.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public News getById(int id)
    {
        return newsDAO.selectbyId(id);
    }

    public int addNews(News news)
    {
        return newsDAO.addNews(news);
    }
    public  int  updateCommentCount(int id,int count)
    {
        return newsDAO.updateCommentCount(id,count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }
}
