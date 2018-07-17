package com.java.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.java.model.User;

@Mapper
public interface UserDao {
	
	public static final String TABLE_NAME="t_user";
	
	public static final String INSERT_FIELDS="name,password,salt,head_url";
	
	public static final String SELECT_FIELDS="id, " + INSERT_FIELDS;
	
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values(#{name},#{password},#{salt},#{headUrl})"})
	public int addUser(User user);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	public User selectById(Integer id);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
	public User selectByName(String name);
	
	@Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
	public void updatePassword(User user);
	
	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	public int deleteById(Integer id);
}
