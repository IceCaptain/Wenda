package com.java.async;

import java.util.List;

public interface EventHandler {
		
	public void doHandler(EventModel eventModel);
	
	public List<EventType> getSupportEventTyped();
		
}
