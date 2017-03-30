package dunavnet.com.mojbac.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventData implements Serializable{

	private String text;
	private String imageFormat;
	private String imageData;
	private GPSCoordinates eventLocation;
	private Date eventDate;
	private long expirationTime;
	private String deviceUUID;
	private String address;

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public EventData() {
		
	}
	
	public EventData(String text, String imageFormat, String imageData, GPSCoordinates eventLocation, Date eventDate,
			long expirationTime, String deviceUUID, String address) {
		
		this.text = text;
		this.imageFormat = imageFormat;
		this.imageData = imageData;
		this.eventLocation = eventLocation;
		this.eventDate = eventDate;
		this.expirationTime = expirationTime;
		this.address = address;
	
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public GPSCoordinates getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(GPSCoordinates eventLocation) {
		this.eventLocation = eventLocation;
	}

	public Date getEventDate() {
		return eventDate;
	}

    public String getEventDateStr() {
        return sdf.format(this.eventDate);
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	//public void setEventDate(Date eventDate) {
	//	this.eventDate = eventDate;
	//}

    public void setEventDate(String eventDate) {
        try {
            this.eventDate = sdf.parse(eventDate);

        } catch (ParseException ex) {

            ex.printStackTrace();
        }
    }
    
    

	public long getExpirationTime() {
		return expirationTime;
	}
	
    public String getExpirationTimeStr() {
        Date aux=new Date(this.expirationTime);
        return sdf.format(aux);
    }

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

    public void setExpirationTime(String expirationTime) {
        try {
            Date aux = sdf.parse(expirationTime);
            this.expirationTime = aux.getTime();

        } catch (ParseException ex) {

            ex.printStackTrace();
        }
    }
	
}
