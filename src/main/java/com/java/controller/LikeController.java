package com.java.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.model.EntityType;
import com.java.model.HostHolder;
import com.java.service.LikeService;
import com.java.util.WendaUtil;

@Controller
public class LikeController {
	
	private static final Logger logger=LoggerFactory.getLogger(LikeController.class);
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private LikeService likeService;
	
	@RequestMapping(path={"/like"}, method=RequestMethod.POST)
	@ResponseBody
	public String like(@RequestParam("commentId")Integer commentId){
		if(hostHolder.getUser()==null){
			return WendaUtil.getJSONString(999);
		}
		
		long likeCount=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		return WendaUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	@RequestMapping(path={"/dislike"}, method=RequestMethod.POST)
	@ResponseBody
	public String dislike(@RequestParam("commentId")Integer commentId){
		if(hostHolder.getUser()==null){
			return WendaUtil.getJSONString(999);
		}
		
		long likeCount=likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		return WendaUtil.getJSONString(0, String.valueOf(likeCount));
	}

}
