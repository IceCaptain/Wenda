package com.java.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.java.async.EventHandler;
import com.java.async.EventModel;
import com.java.async.EventType;
import com.java.model.HostHolder;
import com.java.model.Message;
import com.java.model.User;
import com.java.service.MessageService;
import com.java.service.UserService;
import com.java.util.WendaUtil;

@Component
public class LikeHandler implements EventHandler {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;

	@Override
	public void doHandler(EventModel eventModel) {
		// TODO Auto-generated method stub
		User user = userService.getUser(eventModel.getActorId());
		Message message = new Message();
		message.setFromId(user.getId());
		message.setToId(eventModel.getEntityOwnerId());
		message.setHasRead(0);
		message.setCreatedDate(new Date());
		
		message.setContent("用户" + user.getName() + "赞了你的评论，http://127.0.0.1/question/" + eventModel.getExts("questionId"));
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTyped() {
		// TODO Auto-generated method stub
		return Arrays.asList(EventType.LIKE);
	}

}
