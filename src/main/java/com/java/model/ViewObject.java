package com.java.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图实体
 * @author zxy
 *
 */
public class ViewObject {
	
	private Map<String, Object> objs=new HashMap<>();
	
	public void set(String key, Object value){
		objs.put(key, value);
	}
	
	public Object get(String key){
		return objs.get(key);
	}

	public Map<String, Object> getObjs() {
		return objs;
	}

	public void setObjs(Map<String, Object> objs) {
		this.objs = objs;
	}

	
}
