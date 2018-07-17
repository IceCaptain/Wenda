package com.java.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.java.model.Question;
import com.java.model.ViewObject;
import com.java.service.QuestionService;
import com.java.service.UserService;

@Controller
public class HomeController {
	
	private static final Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping(path={"/user/{id}"}, method={RequestMethod.GET})
	public ModelAndView userIndex(@PathVariable("id")Integer id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("vos", getQuestions(id, 0, 10));
		mav.setViewName("/index");
		return mav;
	}
	
	@RequestMapping(path={"/","/index"}, method={RequestMethod.GET})
	public ModelAndView index(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("vos", getQuestions(null, 0, 10));
		mav.setViewName("/index");
		return mav;
	}
	
	private List<ViewObject> getQuestions(Integer userId, Integer offset, Integer limit){
		List<Question> questionList=questionService.getLatestQuestions(userId, offset, limit);
		List<ViewObject> vos=new ArrayList<>();
		for(Question question:questionList){
			ViewObject vo=new ViewObject();
			vo.set("question", question);
			vo.set("user", userService.getUser(question.getUserId()));
			vos.add(vo);
		}
		return vos;
	}

}
