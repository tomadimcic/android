package com.nt.najboljekafane.model;

import java.io.Serializable;

public class Settings implements Serializable {

	private static final long serialVersionUID = -2000084754054771107L;

	private int id;
	private String deviceUUID;
	
	public Settings() {
	}

	Settings(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceUUID() {
		return deviceUUID;
	}

	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}

}
