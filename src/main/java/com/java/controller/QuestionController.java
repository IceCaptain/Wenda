package com.java.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.model.Comment;
import com.java.model.EntityType;
import com.java.model.HostHolder;
import com.java.model.Question;
import com.java.model.ViewObject;
import com.java.service.CommentService;
import com.java.service.LikeService;
import com.java.service.QuestionService;
import com.java.service.UserService;
import com.java.util.WendaUtil;

@Controller
public class QuestionController {
	
	private static final Logger logger=LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@RequestMapping(path={"/question/add"}, method={RequestMethod.POST})
	@ResponseBody
	public String addQuestion(@RequestParam("title")String title, @RequestParam("content")String content){
		try{
			if(hostHolder.getUser()==null){
				return WendaUtil.getJSONString(999);
			}
			Question question=new Question();
			question.setTitle(title);
			question.setContent(content);
			question.setCreatedDate(new Date());
			question.setCommentCount(0);
			question.setUserId(hostHolder.getUser().getId());
			if(questionService.addQuestion(question)>0){
				return WendaUtil.getJSONString(0);
			}
		}catch(Exception e){
			logger.error("添加题目失败！"+e.getMessage());
		}
		return WendaUtil.getJSONString(1, "失败");
	}
	
	@RequestMapping(path={"/question/{qid}"})
	public ModelAndView questionDetail(@PathVariable("qid")Integer qid){
		ModelAndView mav=new ModelAndView();
		Question question=questionService.selectById(qid);
		mav.addObject("question", question);
		mav.addObject("user", userService.getUser(question.getUserId()));
		
		List<Comment> commentList=commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> comments=new ArrayList<>();
		for(Comment comment:commentList){
			ViewObject vo=new ViewObject();
			vo.set("comment", comment);
			if(hostHolder.getUser()==null){
				vo.set("liked", 0);
			}else{
				vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
			}
			vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
			vo.set("user", userService.getUser(comment.getUserId()));
			comments.add(vo);
		}
		
		mav.addObject("comments", comments);
		
		mav.setViewName("/detail");
		return mav;
	}

}
