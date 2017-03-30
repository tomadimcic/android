package com.iotwear.wear.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

import com.bugsense.trace.BugSenseHandler;
import com.iotwear.wear.model.PiControl.PiControlType;


public class Alarm {
    
    public static final String NAME = "name";
    public static final String TIME = "s";
    public static final String DAILY = "d";
    public static final String GROUP = "group";
    
    private String time;
    private String secondsToAlarm;
    private String daily = "0";
    private ArrayList<String> type;
    private ArrayList<String> id;
    private ArrayList<String> value;
    private String name;
    private String deviceName;
    private boolean isActivated;
    
    public Alarm() {
	this.type = new ArrayList<String>();
	this.id = new ArrayList<String>();
	this.value = new ArrayList<String>();
    }
    
    public static String toJsonForSending(Alarm alarm) {
	JSONObject json = new JSONObject();
	try {
			
			json.put(TIME, alarm.getSecondsToAlarm());
			json.put(DAILY, alarm.getDaily());
			json.put(GROUP, new JSONArray(alarm.getGroupJson(alarm)));

		
	} catch (JSONException e) {
		e.getStackTrace();
	}

	return json.toString();
    }
    
    public static JSONObject toJsonForSaving(Alarm alarm) {
	JSONObject json = new JSONObject();
	try {
	    json.put("deviceName", alarm.getDeviceName());
	    json.put(NAME, alarm.getName());
	    json.put(TIME, alarm.getTime());
	    json.put(DAILY, alarm.getDaily());
	    json.put("isActivated", alarm.isActivated());
	    json.put(GROUP, new JSONArray(alarm.getGroupJson(alarm)));

		
	} catch (JSONException e) {
		e.getStackTrace();
	}

	return json;
    }
    
    public String getGroupJson(Alarm alarm){
	JSONArray jsonArray = new JSONArray();
	try {
		for (int i = 0; i < getType().size(); i++) {
			JSONObject json = new JSONObject();
			json.put("type", alarm.getType().get(i));
			json.put("id", alarm.getId().get(i));
			json.put("value", alarm.getValue().get(i));
			jsonArray.put(json);

		}
	} catch (JSONException e) {
		e.getStackTrace();
	}
	return jsonArray.toString();
    }
    
    public static Alarm fromReceivedJsonToAlarm(JSONObject json){
	Alarm alarm = new Alarm();
	String result = "[{\"s\":13546,\"d\":0,\"i\":\"1+2+3\",\"v\":\"1+1+1\",\"t\":\"1+2+1\"},{\"s\":100,\"d\":0,\"i\":\"0\",\"v\":\"1\",\"t\":\"1\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"}]";
	
	try {
	    alarm.setActivated(true);
	    alarm.setTime(json.getString(TIME));
	    alarm.setDaily(json.getString(DAILY));
	    alarm.setActivated(json.getBoolean("isActivated"));
	    String i = json.getString("i");
	    String v = json.getString("v");
	    String t = json.getString("t");
	    
	    for (int k = 0; k < i.split("+").length; k++) {
		alarm.addType1(t.split("+")[k]);
		alarm.addId(i.split("+")[k]);
		alarm.addValue(v.split("+")[k]);
	    }
	    
	}catch (Exception e) {
	    // TODO: handle exception
	}
	
	return alarm;
    }
    
    public static Alarm fromJson(JSONObject json) {
	Alarm alarm = new Alarm();
	try {
	    alarm.setDeviceName(json.getString("deviceName"));
	    alarm.setName(json.getString(NAME));
	    alarm.setTime(json.getString(TIME));
	    alarm.setDaily(json.getString(DAILY));
	    alarm.setActivated(json.getBoolean("isActivated"));
	    JSONArray groupJson = ((JSONArray) json.getJSONArray(GROUP));
	    for (int i = 0; i < groupJson.length(); i++) {
		alarm.addType1(((JSONObject) groupJson.get(i)).getString("type"));
		alarm.addId(((JSONObject) groupJson.get(i)).getString("id"));
		alarm.addValue(((JSONObject) groupJson.get(i)).getString("value"));
	    }
	}catch (Exception e) {
	    // TODO: handle exception
	}
	return alarm;
    }
    
    public static JSONObject getJsonListForSending(ArrayList<Alarm> alForSending) {
	JSONObject json = new JSONObject();
	
	    try {
		json.put("alarms", new JSONArray(toJsonForSendingList(alForSending)));
	    } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	
	return json;
    }
    
    public static String toJsonForSendingList(ArrayList<Alarm> alForSending) {
	
	JSONArray jsonArray = new JSONArray();
	for (Alarm alarm : alForSending) {
	    	try {
	    	JSONObject json = new JSONObject();
			json.put(TIME, alarm.getSecondsToAlarm());
			json.put(DAILY, alarm.getDaily());
			json.put(GROUP, new JSONArray(alarm.getGroupJson(alarm)));
			jsonArray.put(json);

		
        	} catch (JSONException e) {
        		e.getStackTrace();
        	}
	}

	return jsonArray.toString();
    }
    
    public String toQueryForSending(Alarm alarm){
	//n=0&d=0&sec=10&t=1+1+0+1+0+1+1+1&id=0+1+2+3+4+5+6+7&do=1+1+1+0+1+1+0+1
	return "&d=" + getDaily() + "&sec=" + getSecondsToAlarm() + "&t=" + getTypes() + "&id=" + getIds() + "&do=" + getValues();
    }
    
    public String getTypes(){
	String types = "";
	for (String t : type) {
	    types += "+" + t;
	}
	return types.substring(1);
    }
    
    public String getIds(){
	String iDs = "";
	for (String i : id) {
	    iDs += "+" + i;
	}
	return iDs.substring(1);
    }
    
    public String getValues(){
	String values = "";
	for (String v : value) {
	    values += "+" + v;
	}
	return values.substring(1);
    }
    
    public String getSecondsToAlarm() {

	Time currentTime = new Time();
	currentTime.setToNow();
	int currentHour = currentTime.hour;
	int currentMin = currentTime.minute;
	long difference = 0;
	
	SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        try {
	    Date date1 = format.parse(getTime());
	    Date date2 = format.parse(currentHour + ":" + currentMin);
	    System.out.println("alarm time: " + date1 + " current time: " + date2);
	    difference = (date1.getTime() - date2.getTime())/1000;
	    System.out.println("diff1: " + difference);
	    if(difference <= 0)
		difference = 86400 + difference;
	    System.out.println("diff1: " + difference);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        return String.valueOf(difference);
    }
    
    public long sinceMidnight(){
	Calendar c = Calendar.getInstance();
	long now = c.getTimeInMillis();
	c.set(Calendar.HOUR_OF_DAY, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.MILLISECOND, 0);
	long passed = now - c.getTimeInMillis();
	long secondsPassed = passed / 1000;
	System.out.println("since mid sec: " + secondsPassed);
	return secondsPassed;
    }
    
    public void setSecondsToAlarm(String secondsToAlarm) {
        this.secondsToAlarm = secondsToAlarm;
    }
    public String getDaily() {
        return daily;
    }
    public void setDaily(String daily) {
        this.daily = daily;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }
    
    public void addType(PiControlType type1) {
	String type2 = null;
	if(type1 == PiControlType.LED)
	    type2 = "0";
	if(type1 == PiControlType.SWITCH)
	    type2 = "1";
	if(type1 == PiControlType.DIMMER)
	    type2 = "2";
	if(type1 == PiControlType.GROUP)
	    type2 = "3";
	if(type1 == PiControlType.TASTER)
	    type2 = "4";
	if(type1 == PiControlType.BLINDS)
	    type2 = "5";
        this.type.add(type2);
    }
    
    public void addType1(String type1) {
        this.type.add(type1);
    }

    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
    }
    public void addId(String id1) {
        this.id.add(id1);
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }
    public void addValue(String value1) {
        this.value.add(value1);
    }
    public void setValueAt(int pos, String value1) {
	ArrayList<String> newValue = new ArrayList<String>();
	for (int i = 0; i < getValue().size(); i++) {
	    if(i == pos)
		newValue.add(value1);
	    else
		newValue.add(getValue().get(i));
	}
        setValue(newValue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
