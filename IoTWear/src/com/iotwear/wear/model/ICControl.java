package com.iotwear.wear.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.iotwear.wear.R;
import com.iotwear.wear.model.PiControl.PiControlType;
import com.iotwear.wear.model.interfaces.SingleOptionControl;

public class ICControl extends PiControl implements SingleOptionControl {

    private static final long serialVersionUID = 5745351793518786268L;

    public static final String URL_IC_CONTROL = "/irc";
    public static final int ICON = R.drawable.ic_tv;

    int icon;
    int value;
    
    public ICControl() {
	    piControlType = PiControlType.IC;
	}

    public int getValue() {
	return value;
    }

    public void setValue(int value) {
	this.value = value;
    }

    @Override
    public String getUrlSuffix() {
	return URL_IC_CONTROL;
    }

    @Override
    public int getIcon() {
	switch (icon) {
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
    public String toJsonForSending() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String toQueryForSending() {
	// ?t=7&v=(on-turn on, off-turn off, a-program up, b-program down, c-volume up, d-volume down, [1....])
	return "?id=" + id + "&v=" + getValue();
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
	return json;
    }

}
