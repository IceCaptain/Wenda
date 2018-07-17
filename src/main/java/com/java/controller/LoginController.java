package com.java.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java.service.UserService;

@Controller
public class LoginController {
	
	private static final Logger logger=LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path={"/login"}, method={RequestMethod.POST})
	public ModelAndView login(@RequestParam("userName")String userName, 
							  @RequestParam("password")String password, 
							  @RequestParam(value="next", required=false)String next,
							  @RequestParam(value="rememberme", defaultValue="false")boolean rememberme, 
							  HttpServletResponse response){
		ModelAndView mav=new ModelAndView();
		try{
			Map<String, String> map=userService.login(userName, password);
			if(map.containsKey("ticket")){
				Cookie cookie=new Cookie("ticket", map.get("ticket"));
				cookie.setPath("/");
				if(rememberme){
					cookie.setMaxAge(3600*24*5);
				}
				response.addCookie(cookie);
				mav.setViewName("redirect:/");
				if(StringUtils.isNoneBlank(next)){
					mav.setViewName("redirect:"+ next);
				}
			}else{
				mav.addObject("msg", map.get("msg"));
				mav.setViewName("/login");
			}
			return mav;
		}catch (Exception e) {
			logger.error("登录异常！"+e.getMessage());
			mav.setViewName("/login");
			return mav;
		}
	}
	
	@RequestMapping(path={"/reg"}, method={RequestMethod.POST})
	public ModelAndView reg(@RequestParam("userName")String userName, 
						    @RequestParam("password")String password, 
						    @RequestParam(value="next", required=false)String next,
						    HttpServletResponse response){
		ModelAndView mav=new ModelAndView();
		try{
			Map<String, String> map=userService.register(userName, password);
			if(map.containsKey("ticket")){
				Cookie cookie=new Cookie("ticket", map.get("ticket"));
				cookie.setPath("/");
				response.addCookie(cookie);
				mav.setViewName("redirect:/");
				if(StringUtils.isNoneBlank(next)){
					mav.setViewName("redirect:"+ next);
				}
			}else{
				mav.addObject("msg", map.get("msg"));
				mav.setViewName("/login");
			}
			return mav;
		}catch (Exception e) {
			logger.error("注册异常！"+e.getMessage());
			mav.setViewName("/login");
			return mav;
		}
	}
	
	@RequestMapping(path={"/reglogin"}, method={RequestMethod.GET})
	public ModelAndView reg(@RequestParam(value="next", required=false)String next){
		ModelAndView mav=new ModelAndView();
		mav.addObject("next", next);
		mav.setViewName("/login");
		return mav;
	}
	
	@RequestMapping(path={"/logout"}, method={RequestMethod.GET})
	public String logout(@CookieValue("ticket")String ticket){
		userService.logout(ticket);
		return "redirect:/";
	}

}
