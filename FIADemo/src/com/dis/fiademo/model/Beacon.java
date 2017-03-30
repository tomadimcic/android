package com.dis.fiademo.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Beacon implements Serializable{

    String id;
    String title;
    String gps;
    int distance;
    String email;
    String sms;
    String urlCMS;
    String IPv4CMS;
    String IPv6CMS;
    String activated = "";
    String sent = "";
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getGps() {
        return gps;
    }
    public void setGps(String gps) {
        this.gps = gps;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSms() {
        return sms;
    }
    public void setSms(String sms) {
        this.sms = sms;
    }
    public String getUrlCMS() {
        return urlCMS;
    }
    public void setUrlCMS(String urlCMS) {
        this.urlCMS = urlCMS;
    }
    public String getIPv4CMS() {
        return IPv4CMS;
    }
    public void setIPv4CMS(String iPv4CMS) {
        IPv4CMS = iPv4CMS;
    }
    public String getIPv6CMS() {
        return IPv6CMS;
    }
    public void setIPv6CMS(String iPv6CMS) {
        IPv6CMS = iPv6CMS;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getActivated() {
        return activated;
    }
    public void setActivated(String activated) {
        this.activated = activated;
    }
    public String getSent() {
        return sent;
    }
    public void setSent(String sent) {
        this.sent = sent;
    }
    
    public JSONObject toJson() throws JSONException {
	JSONObject json = new JSONObject();
	json.put("id", getId());
	json.put("title", getTitle());
	json.put("gps", getGps());
	json.put("distance", getDistance());
	json.put("email", getEmail());
	json.put("sms", getSms());
	json.put("urlCMS", getUrlCMS());
	json.put("IPv4CMS", getIPv4CMS());
	json.put("IPv6CMS", getIPv6CMS());
	return json;
    }
    
    public static Beacon fromJson(JSONObject json) throws JSONException{
	//JSONObject json = jsonO.getJSONObject("beacon");
	Beacon b = new Beacon();
	try {
	    b.setId(json.getString("id"));
	    b.setTitle(json.getString("title"));
	    b.setGps(json.getString("gps"));
	    b.setDistance(json.getInt("distance"));
	    b.setEmail(json.getString("email"));
	    b.setSms(json.getString("sms"));
	    b.setUrlCMS(json.getString("urlCMS"));
	    b.setIPv4CMS(json.getString("IPv4CMS"));
	    b.setIPv6CMS(json.getString("IPv6CMS"));
	    
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return b;
    }
    public JSONObject toJsonFoRCMS(String username, String imei)  throws JSONException {
	JSONObject json = new JSONObject();
	json.put("user", username);
	json.put("imei", imei);
	json.put("id", getId());
	json.put("title", getTitle());
	json.put("gps", getGps());
	json.put("distance", getDistance());
	json.put("email", getEmail());
	json.put("sms", getSms());
	json.put("urlCMS", getUrlCMS());
	json.put("IPv4CMS", getIPv4CMS());
	json.put("IPv6CMS", getIPv6CMS());
	return json;
    }
    
    
}
