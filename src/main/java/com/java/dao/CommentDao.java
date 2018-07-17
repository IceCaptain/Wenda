package com.java.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.java.model.Comment;

@Mapper
public interface CommentDao {
	
	public static final String TABLE_NAME="t_comment";
	
	public static final String INSERT_FIELDS="user_id,content,created_date,entity_id,entity_type,status";
	
	public static final String SELECT_FIELDS="id, " + INSERT_FIELDS;
	
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values(#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
	public int addComment(Comment comment);
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
	public List<Comment> selectCommentsByEntity(@Param("entityId")Integer entityId, @Param("entityType")Integer entityType);
	
	@Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
	public int getCommentCount(@Param("entityId")Integer entityId, @Param("entityType")Integer entityType);
	
	@Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
	public int updateCommentStatus(@Param("id")Integer id, @Param("status")Integer status);
	
}
