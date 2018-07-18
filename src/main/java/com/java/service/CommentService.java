package com.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.java.dao.CommentDao;
import com.java.model.Comment;

@Service
public class CommentService {
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private SensitiveService sensitiveService;
	
	public List<Comment> getCommentsByEntity(Integer entityId, Integer entityType){
		return commentDao.selectCommentsByEntity(entityId, entityType);
	}

	public int addComment(Comment comment){
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		comment.setContent(sensitiveService.filter(comment.getContent()));
		return commentDao.addComment(comment)>0 ? comment.getId() : 0;
	}
	
	public int getCommentCount(Integer entityId, Integer entityType){
		return commentDao.getCommentCount(entityId, entityType);
	}
	
	public boolean deleteComment(Integer commentId){
		return commentDao.updateCommentStatus(commentId, 1)>0;
	}
	
	public Comment getCommentById(Integer commentId){
		return commentDao.getCommentById(commentId);
	}
	
}
