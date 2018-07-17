package com.java.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.java.dao.LoginTicketDao;
import com.java.dao.UserDao;
import com.java.model.HostHolder;
import com.java.model.LoginTicket;
import com.java.model.User;

@Component
public class PassportInterceptor implements HandlerInterceptor{
	
	@Autowired
	private LoginTicketDao loginTicketDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ticket=null;
		if(request.getCookies()!=null){
			for(Cookie cookie:request.getCookies()){
				if(cookie.getName().equals("ticket")){
					ticket=cookie.getValue();
					break;
				}
			}
		}
		if(ticket!=null){
			LoginTicket loginTicket=loginTicketDao.selectByTicket(ticket);
			if(loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
				return true;
			}
			
			User user=userDao.selectById(loginTicket.getUserId());
			hostHolder.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		if(modelAndView!=null){
			modelAndView.addObject("user", hostHolder.getUser());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		hostHolder.clear();
	}

}
