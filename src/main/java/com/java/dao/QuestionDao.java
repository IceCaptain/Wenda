package com.java.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.java.model.Question;

@Mapper
public interface QuestionDao {
	
	public static final String TABLE_NAME="t_question";
	
	public static final String INSERT_FIELDS="title,content,created_date,user_id,comment_count";
	
	public static final String SELECT_FIELDS="id, " + INSERT_FIELDS;
	
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
	public int addQuestion(Question question);
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id=#{id}"})
	public Question selectById(Integer id);
	
	@Update({"update ",TABLE_NAME," set count=#{count} where id=#{id}"})
	public int updateCommentCount(Integer id, Integer count);
	
	public List<Question> selectLatestQuestions(@Param("userId")Integer userId, @Param("offset")Integer offset, @Param("limit")Integer limit);
	
}
