package com.java.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.java.model.LoginTicket;

@Mapper
public interface LoginTicketDao {
	
	public static final String TABLE_NAME="t_login_ticket";
	
	public static final String INSERT_FIELDS="user_id,expired,status,ticket";
	
	public static final String SELECT_FIELDS="id, " + INSERT_FIELDS;
	
	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,") values(#{userId}, #{expired}, #{status}, #{ticket})"})
	public int addTicket(LoginTicket loginTicket);
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where ticket=#{ticket}"})
	public LoginTicket selectByTicket(String ticket);
	
	@Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
	public void updateStatus(@Param("ticket")String ticket, @Param("status")Integer i);

}
