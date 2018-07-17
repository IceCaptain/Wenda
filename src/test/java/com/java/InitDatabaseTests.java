package com.java;

import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.java.dao.QuestionDao;
import com.java.dao.UserDao;
import com.java.model.Question;
import com.java.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	@Test
	public void initUserDatabase(){
		Random random=new Random();
		
		for(int i=0;i<11;i++){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);
			
			user.setId(i+1);
			user.setPassword("xx");
			userDao.updatePassword(user);
			
		}
		
//		Assert.assertEquals("xx", userDao.selectById(1).getPassword());
//		userDao.deleteById(1);
//		Assert.assertNull(userDao.selectById(1));
	}
	
	@Test
	public void initQuestionDatabase(){
		
		for(int i=0;i<11;i++){
			Question question=new Question();
			question.setCommentCount(i);
			Date date=new Date();
			date.setTime(date.getTime()+1000*3600*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("Balalalaalala Content %d", i));
			
			questionDao.addQuestion(question);
		}
		
	}

}
