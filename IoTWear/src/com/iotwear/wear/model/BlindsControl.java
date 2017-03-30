package com.iotwear.wear.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.iotwear.wear.R;
import com.iotwear.wear.model.interfaces.SingleOptionControl;

public class BlindsControl extends PiControl implements SingleOptionControl {

	private static final long serialVersionUID = 5745351793518786268L;

	public static final String URL_BLINDS_CONTROL = "/blind";
	public static final int ICON = R.drawable.ic_roletne;
	int icon;
	String[] ids;
	int currentId;
	protected boolean isTurnedOn;

	public BlindsControl(int vers) {
	    if(vers == 0)
		piControlType = PiControlType.BLINDS;
	    else
		piControlType = PiControlType.BLINDS2;
		name = new String();
	}

	@Override
	public String toJsonForSending() {
		JSONObject blindsJson = new JSONObject();
		try {
		    blindsJson.put(ID, ids[currentId]);
		    blindsJson.put(VALUE, isTurnedOn);
		} catch (JSONException e) {
			return null;
		}
		return blindsJson.toString();
	}

	public void setTurnedOn(boolean isTurnedOn) {
		this.isTurnedOn = isTurnedOn;
	}

	public boolean isTurnedOn() {
		return isTurnedOn;
	}

	@Override
	public String getUrlSuffix() {
		return URL_BLINDS_CONTROL;
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(ID, id);
		json.put(NAME, name);
		json.put(TYPE, piControlType.ordinal());
		return json;
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
	
	public String[] getIds() {
	    return ids;
	}

	public void setIds(String[] ids) {
	    this.ids = ids;
	}

	public int getCurrentId() {
	    return currentId;
	}

	public void setCurrentId(int currentId) {
	    this.currentId = currentId;
	}

	@Override
	public String toQueryForSending() {
	    int v = 0;
	    if (isTurnedOn)
		v=1;
	    return "?id="+ids[currentId]+"&v="+v;
	}

}
