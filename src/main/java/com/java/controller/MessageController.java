package com.java.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.model.HostHolder;
import com.java.model.Message;
import com.java.model.User;
import com.java.model.ViewObject;
import com.java.service.MessageService;
import com.java.service.UserService;
import com.java.util.WendaUtil;

@Controller
public class MessageController {
	
	private static final Logger logger=LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@RequestMapping(path={"/msg/list"}, method={RequestMethod.GET})
	public ModelAndView getConversationList(){
		ModelAndView mav=new ModelAndView();
		if(hostHolder.getUser()==null){
			mav.setViewName("redirect:/reglogin");
			return mav;
		}
		Integer localUserId=hostHolder.getUser().getId();
		List<Message> conversationList=messageService.getConversationList(localUserId, 0, 10);
		List<ViewObject> conversations=new ArrayList<>();
		for(Message message:conversationList){
			ViewObject vo=new ViewObject();
			vo.set("conversation", message);
			Integer tagetId=message.getFromId()==localUserId ? message.getToId() : message.getFromId();
			vo.set("user", userService.getUser(tagetId));
			vo.set("unread", messageService.geConversationUnreadCount(hostHolder.getUser().getId(), message.getConversationId()));
			conversations.add(vo);
		}
		mav.addObject("conversations", conversations);
		mav.setViewName("/letter");
		return mav;
	}
	
	@RequestMapping(path={"/msg/detail"}, method={RequestMethod.GET})
	public ModelAndView getConversationDetail(@RequestParam("conversationId")String conversationId){
		ModelAndView mav=new ModelAndView();
		try{
			if(hostHolder.getUser()==null){
				mav.setViewName("redirect:/reglogin");
				return mav;
			}
			List<Message> messageList=messageService.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages=new ArrayList<>();
			for(Message message:messageList){
				ViewObject vo=new ViewObject();
				if(message.getToId()==hostHolder.getUser().getId()){
					messageService.updateHasRead(hostHolder.getUser().getId(), conversationId, 1);
				}
				vo.set("message", message);
				vo.set("user", userService.getUser(message.getFromId()));
				messages.add(vo);
			}
			mav.addObject("messages", messages);
		}catch(Exception e){
			logger.error("获取详情失败！"+e.getMessage());
		}
		
		mav.setViewName("/letterDetail");
		return mav;
	}
	
	@RequestMapping(path={"/msg/addMessage"}, method={RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("toName")String toName, @RequestParam("content")String content){
		try{
			if(hostHolder.getUser()==null){
				return WendaUtil.getJSONString(999, "未登录");
			}
			
			User user=userService.selectByName(toName);
			if(user==null){
				return WendaUtil.getJSONString(1, "用户不存在");
			}
			
			Message message=new Message();
			message.setCreatedDate(new Date());
			message.setFromId(hostHolder.getUser().getId());
			message.setToId(user.getId());
			message.setContent(content);
			message.setHasRead(0);
			messageService.addMessage(message);
			
			return WendaUtil.getJSONString(0);
		}catch (Exception e) {
			logger.error("发送消息失败！"+e.getMessage());
			return WendaUtil.getJSONString(1, "发信失败");
		}
	}

}
