package com.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.java.dao.QuestionDao;
import com.java.model.Question;

@Service
public class QuestionService {
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private SensitiveService sensitiveService;
	
	public int addQuestion(Question question){
		//HTML过滤，转义问题内容
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		
		//敏感词过滤
		question.setTitle(sensitiveService.filter(question.getTitle()));
		question.setContent(sensitiveService.filter(question.getContent()));
		return questionDao.addQuestion(question)>0 ? question.getId() : 0;
	}
	
	public Question selectById(Integer id){
		return questionDao.selectById(id);
	}
	
	public List<Question> getLatestQuestions(Integer userId, Integer offset, Integer limit){
		return questionDao.selectLatestQuestions(userId, offset, limit);
	}
	
	public int updateCommentCount(Integer id, Integer count) {
        return questionDao.updateCommentCount(id, count);
    }
	
}
