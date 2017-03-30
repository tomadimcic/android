package com.iotwear.wear.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.iotwear.wear.R;
import com.iotwear.wear.model.interfaces.SingleOptionControl;

public class DimControl extends PiControl implements SingleOptionControl {

	private static final long serialVersionUID = 8610764081640975923L;

	public static final String URL_DIM_CONTROL = "/dim";
	public static final int ICON = R.drawable.ic_dimmer;
	int icon;

	private int dimValue;

	public DimControl() {
		piControlType = PiControlType.DIMMER;
		name = new String();
	}

	@Override
	public String toJsonForSending() {
		JSONObject dim = new JSONObject();
		try {
			dim.put(ID, id);
			dim.put(VALUE, dimValue);
		} catch (JSONException e) {
			return null;
		}
		return dim.toString();
	}

	public int getDimValue() {
		return dimValue;
	}

	public void setDimValue(int dimValue) {
		this.dimValue = dimValue;
	}

	@Override
	public String getUrlSuffix() {
		return URL_DIM_CONTROL;
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
	    int v = (255*dimValue)/100;
	    return "?id="+id+"&v="+v;
	}


}
