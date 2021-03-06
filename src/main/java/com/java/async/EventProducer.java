package com.java.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.java.util.JedisAdapter;
import com.java.util.RedisKeyUtil;

@Service
public class EventProducer {
	
	@Autowired
	private JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel eventModel){
		try{
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisKeyUtil.getEventQueueKey();
			jedisAdapter.lpush(key, json);
			return true;
		} catch(Exception e){
			return false;
		}
	}

}
