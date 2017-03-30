package com.iotwear.wear.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.iotwear.wear.R;
import com.iotwear.wear.model.PiControl.PiControlType;

public class LampControl extends PiControl {

	private static final long serialVersionUID = -3963143464349786220L;

	public static final String URL_SET_COLOR = "/wholeLamp";
	public static final String URL_SET_ANIMATION = "/getFavorites";
	public static final String URL_SET_ANIM = "/anim";
	public static final int ICON = R.drawable.ic_launcher;
	int icon;
	String value;

	public LampControl() {
		piControlType = PiControlType.LAMP;
		name = new String();
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(ID, Integer.parseInt(id));
		json.put(NAME, name);
		json.put(TYPE, piControlType.ordinal());
		return json;
	}

	@Override
	public String toJsonForSending() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIcon() {
	    switch(icon){
	    case 0: 
		return ICON;
	    case 1: 
		return R.drawable.ic_launcher;
	    case 2: 
		return R.drawable.ic_switch;
	    case 3: 
		return R.drawable.ic_dimmer;
	    case 4: 
		return R.drawable.ic_taster;
	    case 5: 
		return R.drawable.ic_sijalica;
	    case 6: 
		return R.drawable.ic_roletne;
	    case 7:
		return R.drawable.ic_tv;
	    case 9: 
		return R.drawable.ic_dmx;
	    }
	    
	    return icon;
	}

	@Override
	public void setIcon(int icon) {
	    this.icon = icon;
	    
	}

	@Override
	public JSONObject toJsonforSaving() throws JSONException {
	    JSONObject json = new JSONObject();
		json.put(ID, id);
		json.put(NAME, name);
		json.put(TYPE, piControlType.ordinal());
		json.put(ICON1, getIcon());
		return json;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toQueryForSending() {
	    return "?"+value;
	}
	
	public String toQueryForSending(int color) {
	    return "?id="+Integer.parseInt(id);
	}
}
