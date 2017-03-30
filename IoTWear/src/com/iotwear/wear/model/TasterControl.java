package com.iotwear.wear.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.iotwear.wear.R;
import com.iotwear.wear.model.interfaces.SingleOptionControl;

public class TasterControl extends PiControl implements SingleOptionControl {

	private static final long serialVersionUID = 5745351793518786268L;

	public static final String URL_SWITCH_CONTROL = "/taster";
	public static final int ICON = R.drawable.ic_sijalica;
	int icon;

	protected boolean isTurnedOn;

	public TasterControl() {
		piControlType = PiControlType.TASTER;
		name = new String();
	}

	@Override
	public String toJsonForSending() {
		JSONObject tasterJson = new JSONObject();
		try {
		    tasterJson.put(ID, id);
		    tasterJson.put(VALUE, isTurnedOn);
		} catch (JSONException e) {
			return null;
		}
		return tasterJson.toString();
	}

	public void setTurnedOn(boolean isTurnedOn) {
		this.isTurnedOn = isTurnedOn;
	}

	public boolean isTurnedOn() {
		return isTurnedOn;
	}

	@Override
	public String getUrlSuffix() {
		return URL_SWITCH_CONTROL;
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

	@Override
	public String toQueryForSending() {
	    int v = 0;
	    if (isTurnedOn)
		v=1;
	    return "?id="+id+"&v="+v;
	}

	


}
