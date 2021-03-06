package com.nowcode.toutiao.controller;


import com.nowcode.toutiao.model.HostHolder;
import com.nowcode.toutiao.model.Message;
import com.nowcode.toutiao.model.User;
import com.nowcode.toutiao.model.ViewObject;
import com.nowcode.toutiao.service.MessageService;
import com.nowcode.toutiao.service.UserService;
import com.nowcode.toutiao.util.ToutiaoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})//某个用户发来的详细
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> messages = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("user",user);
                messages.add(vo);
           }
            model.addAttribute("messages", messages);
           return "letterDetail";
        } catch (Exception e) {
          //  logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})//发来私信的用户列表
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 20);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();//判断对方id
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("conversationsCount",messageService.getConversationCout(msg.getConversationId()));
                vo.set("unread", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            System.out.println(conversations.size());
            return "letter";
        } catch (Exception e) {
           //
            System.out.println(e.toString());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})//插入消息
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            //fromId=hostHolder.getUser().getId();
            Message msg = new Message();
            msg.setContent(content);
            msg.setCreatedDate(new Date());
            msg.setToId(toId);
            msg.setFromId(fromId);
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
            //        String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(msg.getId());
        } catch (Exception e){
            return ToutiaoUtil.getJSONString(1, "插入评论失败");
        }

    }
}
