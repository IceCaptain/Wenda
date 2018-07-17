package com.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.java.dao.MessageDao;
import com.java.model.Message;

@Service
public class MessageService {
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private SensitiveService sensitiveService;
	
	public int addMessage(Message message){
		message.setContent(HtmlUtils.htmlEscape(message.getContent()));
		message.setContent(sensitiveService.filter(message.getContent()));
		return messageDao.addMessage(message)>0 ? message.getId() : 0;
	}
	
	public List<Message> getConversationDetail(String conversationId, Integer offset, Integer limit){
		return messageDao.getConversationDetail(conversationId, offset, limit);
	}
	
	public List<Message> getConversationList(Integer userId, Integer offset, Integer limit){
		return messageDao.getConversationList(userId, offset, limit);
	}
	
	public int geConversationUnreadCount(Integer userId, String conversationId){
		return messageDao.geConversationUnreadCount(userId, conversationId);
	}
	
	public void updateHasRead(Integer userId, String conversationId, Integer hasRead){
		messageDao.updateHasRead(userId, conversationId, hasRead);
	}
	
}
