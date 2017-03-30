package com.iotwear.wear.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PiDevice implements Serializable {

	private static final long serialVersionUID = 9024202901671800449L;

	// Pi Settings
	public static final String DEVICE_SSID = "IF";
	public static final String DEVICE_AP_IP_ADDRESS = "192.168.2.1:61616";
	public static final String MASTER_AP_IP_ADDRESS = "192.168.2.1";
	public static final String MASTER_IP_ADDRESS = "192.168.1.135";
	public static final int MASTER_PORT_NUMBER = 61616;

	//public static final String NAME = "name";
	public static final String NAME = "n";
	public static final String URL_STATUS = "/status";
	public static final String URL_ALARM = "/alarm";
	public static final String URL = "url";
	//public static final String LOCAL_IP = "localIp";
	public static final String LOCAL_IP = "ip";
	//public static final String PORT = "port";
	//public static final String SSID = "ssid";
	public static final String PORT = "p";
	public static final String SSID = "s";
	//public static final String CONTROLLERS = "capabilities";
	public static final String CONTROLLERS = "c";
	public static final String MAC = "m";
	public static final String TYPE = "t";
	public static final String ID = "id";
	
	/*public static final String NAME = "name";
	//public static final String NAME = "n";
	public static final String URL_STATUS = "/status";
	public static final String URL = "url";
	public static final String LOCAL_IP = "localIp";
	//public static final String LOCAL_IP = "ip";
	public static final String PORT = "port";
	public static final String SSID = "ssid";
	//public static final String PORT = "p";
	//public static final String SSID = "s";
	public static final String CONTROLLERS = "capabilities";
	//public static final String CONTROLLERS = "c";*/

	private String name;
	private String url;
	private String localIp;
	private String mac;
	private int port;
	private String ssid;
	private boolean status;
	private static String masterIpAddress;
	private boolean checked = false;

	private ArrayList<PiControl> controlList;

	public PiDevice() {
		controlList = new ArrayList<PiControl>();
	}

	public PiDevice(String name, String url, String localIp, int port,
			String ssid, String mac) {
		controlList = new ArrayList<PiControl>(0);
		this.name = name;
		this.url = url;
		this.localIp = localIp;
		this.port = port;
		this.ssid = ssid;
		this.mac = mac;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMac() {
	    return mac;
	}

	public void setMac(String mac) {
	    this.mac = mac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public static String getMasterIpAddress() {
	    return masterIpAddress;
	}

	public static void setMasterIpAddress(String masterIpAddress1) {
	    masterIpAddress = masterIpAddress1;
	}

	public boolean isSingleControllerDevice() {
		if (controlList.size() == 1)
			return true;
		return false;
	}

	public boolean isChecked() {
	    return checked;
	}

	public void setChecked(boolean checked) {
	    this.checked = checked;
	}

	public static PiDevice fromJson(JSONObject json) {
	    //String pom = "[{\"p\":61616,\"s\":\"PTT NET\",\"url\":\"controller17.no-ip.biz\",\"ip\":\"192.168.1.135\",\"n\":\"prezentacija\",\"m\":\"00003040F095\",\"c\":[{\"t\":0,\"id\":0,\"n\":\"led0\"},{\"t\":7,\"id\":0,\"n\":\"Irc0\"},{\"t\":8,\"id\":1,\"n\":\"Input1\"},{\"t\":8,\"id\":2,\"n\":\"Input2\"}]}]";
		try {
			PiDevice dev = new PiDevice();
			dev.setName(json.getString(NAME));
			dev.setUrl(json.getString(URL));
			dev.setLocalIp(json.getString(LOCAL_IP));
			dev.setPort(json.getInt(PORT));
			dev.setSsid(json.getString(SSID));
			dev.setMac(json.getString(MAC));

			JSONArray controlsArray = json.optJSONArray(CONTROLLERS);
			ArrayList<PiControl> ctrlArray = new ArrayList<PiControl>();
			dev.setControlList(ctrlArray);
			
			for (int i = 0; i < controlsArray.length(); i++) {
				int type = controlsArray.getJSONObject(i).optInt(TYPE, -1);
				String id = controlsArray.getJSONObject(i).optString(ID);
				if(type != 5 && type != 6 && id.contains("-")){
					int from = Integer.parseInt(id.split("-")[0]);
					int to = Integer.parseInt(id.split("-")[1]);
					for(int j = from; j<=to; j++){
						PiControl ctrl = PiControl.buildFromJsonNew(controlsArray
								.getJSONObject(i), dev, j);
						if(!(ctrl instanceof GroupControl)){
						    if (ctrl != null)
						    	dev.getControlList().add(ctrl);
						}
					}
				}else{
					PiControl ctrl = PiControl.buildFromJson(controlsArray
							.getJSONObject(i), dev, 0);
					if(!(ctrl instanceof GroupControl)){
					    if (ctrl != null)
					    	dev.getControlList().add(ctrl);
					}
				}
			}

			return dev;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static PiDevice fromJsonLocal(JSONObject json) {
		try {
			PiDevice dev = new PiDevice();
			dev.setName(json.getString(NAME));
			dev.setUrl(json.getString(URL));
			dev.setLocalIp(json.getString(LOCAL_IP));
			dev.setPort(json.getInt(PORT));
			dev.setSsid(json.getString(SSID));
			dev.setMac(json.getString(MAC));

			JSONArray controlsArray = json.optJSONArray(CONTROLLERS);
			ArrayList<PiControl> ctrlArray = new ArrayList<PiControl>();
			dev.setControlList(ctrlArray);
			
			for (int i = 0; i < controlsArray.length(); i++) {
				PiControl ctrl = PiControl.buildFromJson(controlsArray
						.getJSONObject(i), dev, 1);
				if (ctrl != null)
					dev.getControlList().add(ctrl);
			}

			return dev;
		} catch (JSONException e) {
			return null;
		}
	}

	public static JSONObject toJson(PiDevice pi) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(NAME, pi.getName());
		json.put(URL, pi.getUrl());
		json.put(LOCAL_IP, pi.getLocalIp());
		json.put(PORT, pi.getPort());
		json.put(SSID, pi.getSsid());
		json.put(MAC, pi.getMac());

		JSONArray ctrls = new JSONArray();
		
		for (PiControl pic : pi.getControlList()) {
			JSONObject picJson = pic.toJson();
			if (picJson != null)
				ctrls.put(picJson);
		}

		json.put(CONTROLLERS, ctrls);

		return json;
	}
	
	public static JSONObject toJsonForSaving(PiDevice pi) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(NAME, pi.getName());
		json.put(URL, pi.getUrl());
		json.put(LOCAL_IP, pi.getLocalIp());
		json.put(PORT, pi.getPort());
		json.put(SSID, pi.getSsid());
		json.put(MAC, pi.getMac());

		JSONArray ctrls = new JSONArray();
		
		for (PiControl pic : pi.getControlList()) {
			JSONObject picJson = pic.toJsonforSaving();
			if (picJson != null)
				ctrls.put(picJson);
		}

		json.put(CONTROLLERS, ctrls);

		return json;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PiDevice) {
			PiDevice another = (PiDevice) o;
			if (name.equals(another.getName())
					&& localIp.equals(another.getLocalIp()))
				return true;
		}
		return false;
	}

	public static String getIpAddressForDevice(int count) {
		try {
		    String[] ipAdd = getMasterIpAddress().split("\\.");
		    int d = Integer.parseInt(ipAdd[3]);
		    d = d + count;
		    StringBuilder builder = new StringBuilder();
		    builder.append(ipAdd[0]);
		    builder.append(".");
		    builder.append(ipAdd[1]);
		    builder.append(".");
		    builder.append(ipAdd[2]);
		    builder.append(".");
		    builder.append(d);

		    return builder.toString();
		} catch (NumberFormatException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return "";
		}
	}

	public boolean isTurnedOn() {
		return status;
	}

	public void setTurnedOn(boolean status) {
		this.status = status;
	}

	public ArrayList<PiControl> getControlList() {
		return controlList;
	}

	public void setControlList(ArrayList<PiControl> controlList) {
		this.controlList = controlList;
	}

	@Override
	public String toString() {
		try {
			return toJson(this).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new JSONObject().toString();
	}
}
