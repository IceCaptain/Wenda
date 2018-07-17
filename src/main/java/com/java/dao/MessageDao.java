package com.java.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.java.model.Message;

@Mapper
public interface MessageDao {
	
	public static final String TABLE_NAME="t_message";
	
	public static final String INSERT_FIELDS="from_id,to_id,content,has_read,conversation_id,created_date";
	
	public static final String SELECT_FIELDS="id, " + INSERT_FIELDS;
	
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values(#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
	public int addMessage(Message message);
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by created_date asc limit #{offset}, #{limit}"})
	public List<Message> getConversationDetail(@Param("conversationId")String conversationId, @Param("offset")Integer offset, @Param("limit")Integer limit);
	
	//select *, count(id) as cnt from (select * from t_message ORDER by created_date desc) tt group by conversation_id order by created_date DESC;
	@Select({"select ", SELECT_FIELDS, " ,count(id) as count from "
			+ "(select * from  ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt"
			+ " group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
	public List<Message> getConversationList(@Param("userId")Integer userId, @Param("offset")Integer offset, @Param("limit")Integer limit);
	
	@Select({"select count(id) from", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
	public int geConversationUnreadCount(@Param("userId")Integer userId, @Param("conversationId")String conversationId);
	
	@Update({"update ", TABLE_NAME, " set has_read=#{hasRead} where to_id=#{userId} and conversation_id=#{conversationId}"})
	public void updateHasRead(@Param("userId")Integer userId, @Param("conversationId")String conversationId, @Param("hasRead")Integer hasRead);
}
