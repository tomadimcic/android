package dunavnet.com.mojiodzaci.model;

import java.io.Serializable;

public class Event implements Serializable{
	
	private String type;
	private String title;
	private EventData eventData;
	
	
	public Event() {
		
	}
	
	public Event(String type, String title, EventData eventData) {
		this.type = type;
		this.title = title;
		this.eventData = eventData;
	}

	public EventData getEventData() {
		return eventData;
	}

	public void setEventData(EventData eventData) {
		this.eventData = eventData;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
