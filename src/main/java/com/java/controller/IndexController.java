package com.java.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	@ResponseBody
	public String index(){
		return "Hello World!";
	}
	
	@RequestMapping(path="/tp", method=RequestMethod.GET)
	public ModelAndView template(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("hello", "hello");
		mav.addObject("list", new int[]{1,2,3});
		Map<String, String> map=new HashMap<>();
		for(Entry<String,String> entry:map.entrySet()){
			entry.getValue();
		}
		map.put("key", "value");
		mav.addObject("map", map);
		mav.addObject("mainPage", "/head");
		mav.addObject("mainPageKey", "#h");
		mav.setViewName("/home");
		return mav;
	}

}
