package com.java.util;

public class RedisKeyUtil {
	
	private static final String SPLIT=":";
	
	private static final String BIZ_LIKE="LIKE";
	
	private static final String BIZ_DISLIKE="DISLIKE";
	
	public static String getLikeKey(Integer entityType, Integer entityId){
		return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}
	
	public static String getDisLikeKey(Integer entityType, Integer entityId){
		return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}
}
