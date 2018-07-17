package com.java.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java.model.Comment;
import com.java.model.EntityType;
import com.java.model.HostHolder;
import com.java.service.CommentService;
import com.java.service.QuestionService;

@Controller
public class CommentController {
	
	private static final Logger logger=LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@RequestMapping(path={"/addComment"}, method={RequestMethod.POST})
	public ModelAndView addComment(@RequestParam("questionId")Integer questionId, @RequestParam("content")String content){
		ModelAndView mav=new ModelAndView();
		try{
			Comment comment=new Comment();
			if(hostHolder.getUser()!=null){
				comment.setUserId(hostHolder.getUser().getId());
			}else{
//				comment.setUserId(WendaUtil.ANONYMOUS_USERID);
				mav.setViewName("redirect:/reglogin");
				return mav;
			}
			comment.setContent(content);
			comment.setCreatedDate(new Date());
			
			comment.setEntityType(EntityType.ENTITY_QUESTION);
			comment.setEntityId(questionId);
			comment.setStatus(0);
			
			int count=commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
			questionService.updateCommentCount(comment.getEntityId(), count);
			
			commentService.addComment(comment);
		}catch(Exception e){
			logger.error("增加评论失败！"+e.getMessage());
		}
		mav.setViewName("redirect:/question/"+questionId);
		return mav;
	}

}
