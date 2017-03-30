package com.iotwear.wear.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.iotwear.wear.R;
import com.iotwear.wear.model.PiControl.PiControlType;
import com.iotwear.wear.model.interfaces.SingleOptionControl;

public class DMXControl extends PiControl implements SingleOptionControl{
    
    private static final long serialVersionUID = -3963143464349786221L;

	public static final String URL_SET_DMX = "/dmx";
	public static final int ICON = R.drawable.ic_dmx;
	int icon;
	int v;
	
	public DMXControl() {
	    piControlType = PiControlType.DMX;
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
	
	

    public int getValue() {
	    return v;
	}

	public void setValue(int v) {
	    this.v = v;
	}

    @Override
    public String toJsonForSending() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String toQueryForSending() {
	return "?id="+Integer.parseInt(id) + "&v=" + v;
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
    public String getUrlSuffix() {
	// TODO Auto-generated method stub
	return URL_SET_DMX;
    }

}
