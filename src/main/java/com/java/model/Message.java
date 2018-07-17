package com.java.model;

import java.beans.Transient;
import java.util.Date;

public class Message {
	
	private Integer id;
	
	private Integer fromId;
	
	private Integer toId;
	
	private String content;
	
	private Date createdDate;
	
	private Integer hasRead;
	
	private String conversationId;
	
	private Integer count;//会话数量

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getHasRead() {
		return hasRead;
	}

	public void setHasRead(Integer hasRead) {
		this.hasRead = hasRead;
	}

	public String getConversationId() {
		if(fromId<toId){
			return String.format("%d_%d", fromId, toId);
		}else{
			return String.format("%d_%d", toId, fromId);
		}
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	@Transient
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	} 

}
