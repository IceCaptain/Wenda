package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.util.JedisAdapter;
import com.java.util.RedisKeyUtil;

@Service
public class LikeService {
	
	@Autowired
	private JedisAdapter jedisAdapter;
	
	public long getLikeCount(Integer entityType, Integer entityId){
		String likeKey=RedisKeyUtil.getLikeKey(entityType, entityId);
		return jedisAdapter.scard(likeKey);	
	}
	
	public int getLikeStatus(Integer userId, Integer entityType, Integer entityId){
		String likeKey=RedisKeyUtil.getLikeKey(entityType, entityId);
		if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
			return 1;
		}
		String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType, entityId);
		if(jedisAdapter.sismember(disLikeKey, String.valueOf(userId))){
			return -1;
		}
		return 0;
	}
	
	public long like(Integer userId, Integer entityType, Integer entityId){
		String likeKey=RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.sadd(likeKey, String.valueOf(userId));
		
		String dislikeKey=RedisKeyUtil.getDisLikeKey(entityType, entityId);
		jedisAdapter.srem(dislikeKey, String.valueOf(userId));
		
		return jedisAdapter.scard(likeKey);
	}
	
	public long dislike(Integer userId, Integer entityType, Integer entityId){
		String dislikeKey=RedisKeyUtil.getDisLikeKey(entityType, entityId);
		jedisAdapter.sadd(dislikeKey, String.valueOf(userId));
		
		String likeKey=RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.srem(likeKey, String.valueOf(userId));
		
		return jedisAdapter.scard(likeKey);
	}

}
