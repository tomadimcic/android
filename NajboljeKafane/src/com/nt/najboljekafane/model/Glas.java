package com.nt.najboljekafane.model;

import java.io.Serializable;

public class Glas implements Serializable {

	private static final long serialVersionUID = 1842905081057479082L;

	private int id;
	private String tip;
	private String tipId;
	private String timestamp;
	private String deviceUUID;

	public Glas() {
	}

	Glas(String tip, String tipId, String timestamp, String deviceUUID) {
		this.tip = tip;
		this.tipId = tipId;
		this.timestamp = timestamp;
		this.deviceUUID = deviceUUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getTipId() {
		return tipId;
	}

	public void setTipId(String tipId) {
		this.tipId = tipId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getDeviceUUID() {
		return deviceUUID;
	}

	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}

}