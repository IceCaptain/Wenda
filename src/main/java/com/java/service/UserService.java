package com.java.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.dao.LoginTicketDao;
import com.java.dao.UserDao;
import com.java.model.LoginTicket;
import com.java.model.User;
import com.java.util.WendaUtil;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LoginTicketDao loginTicketDao;
	public Map<String, String> register(String userName, String password){
		
		Map<String, String> map=new HashMap<>();
		if(StringUtils.isBlank(userName)){
			map.put("msg", "用户名不能为空！");
			return map;
		}
		if(StringUtils.isBlank(password)){
			map.put("msg", "密码不能为空！");
			return map;
		}
		
		User user=userDao.selectByName(userName);
		if(user!=null){
			map.put("msg", "用户名已经被注册！");
			return map;
		}
		
		user=new User();
		user.setName(userName);
		user.setSalt(UUID.randomUUID().toString().substring(0, 5));
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setPassword(WendaUtil.MD5(password+user.getSalt()));
		
		userDao.addUser(user);
		
		String ticket=addLoginTicket(user.getId());
		map.put("ticket", ticket);
		
		return map;
	}
	
	public Map<String, String> login(String userName, String password){
		Map<String, String> map=new HashMap<>();
		User user=selectByName(userName);
		if(user==null){
			map.put("msg", "用户名不存在！");
			return map;
		}
		
		if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
			map.put("msg", "密码错误！");
			return map;
		}
		
		String ticket=addLoginTicket(user.getId());
		map.put("ticket", ticket);
		
		return map;
	}
	
	public User selectByName(String name){
		return userDao.selectByName(name);
	}
	
	public void logout(String ticket){
		loginTicketDao.updateStatus(ticket, 1);
	}
	
	public User getUser(Integer id){
		return userDao.selectById(id);
	}
	
	public String addLoginTicket(Integer userId){
		LoginTicket loginTicket=new LoginTicket();
		loginTicket.setUserId(userId);
		Date now=new Date();
		now.setTime(now.getTime() + 1000*3600*24);
		loginTicket.setExpired(now);
		loginTicket.setStatus(0);
		loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		loginTicketDao.addTicket(loginTicket);
		return loginTicket.getTicket();
	}

}
