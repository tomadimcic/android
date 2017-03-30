package com.iotwear.wear.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bugsense.trace.BugSenseHandler;
import com.iotwear.wear.R;
import com.iotwear.wear.model.interfaces.SingleOptionControl;

public class GroupControl extends PiControl implements SingleOptionControl {

	private static final long serialVersionUID = -9096182827532601591L;

	public static final String CONTROLLERS = "controllers";
	public static final String URL_GROUP_CONTROL = "/group";
	public static final int ICON = R.drawable.ic_action_link;
	int icon;

	private ArrayList<PiControl> groupControlList;

	public GroupControl() {
		piControlType = PiControlType.GROUP;
		name = new String();
		groupControlList = new ArrayList<PiControl>();
	}

	public ArrayList<PiControl> getGroupControlList() {
		return groupControlList;
	}

	public void setGroupControlList(ArrayList<PiControl> groupControlList) {
		this.groupControlList = groupControlList;
	}

	public void addControl(PiControl control) {
		groupControlList.add(control);
	}

	public void removeControl(PiControl control) {
		for (int i = 0; i < groupControlList.size(); i++) {
			if (control.getId() == groupControlList.get(i).getId()) {
				groupControlList.remove(i);
				break;
			}
		}
	}

	@Override
	public String getUrlSuffix() {
		return URL_GROUP_CONTROL;
	}

	@Override
	public String toJsonForSending() {
		JSONArray groupJson = new JSONArray();
		for (PiControl pi : groupControlList) {
			PiControl item = null;
			if (pi instanceof SwitchControl) {
				item = (SwitchControl) pi;
			}

			else if (pi instanceof DimControl) {
				item = (DimControl) pi;
			}
			
			else{
				BugSenseHandler.sendException(new Exception("GroupControl item isn't Switch or Dim!"));
			}

			try {
				groupJson.put(new JSONObject(item.toJsonForSending()));
			} catch (JSONException e) {
				return null;
			}
		}
		return groupJson.toString();
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(ID, Integer.parseInt(id));
		json.put(NAME, name);
		json.put(TYPE, piControlType.ordinal());
		json.put(CONTROLLERS, new JSONArray(toJsonForSending()));
		return json;
	}
	
	@Override
	public JSONObject toJsonforSaving() throws JSONException {
	    JSONObject json = new JSONObject();
		json.put(ID, id);
		json.put(NAME, name);
		json.put(TYPE, piControlType.ordinal());
		json.put(CONTROLLERS, new JSONArray(toJsonForSending()));
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
	    String query = "";
	    if (!groupControlList.isEmpty())
		query = "?";
	    for (PiControl pi : groupControlList) {
		PiControl item = null;
		if (pi instanceof SwitchControl) {
			item = (SwitchControl) pi;
		}

		else if (pi instanceof DimControl) {
			item = (DimControl) pi;
		}
		
		else{
			BugSenseHandler.sendException(new Exception("GroupControl item isn't Switch or Dim!"));
		}

		query += item.getPiControlType() + item.toQueryForSending();
	}
	    return query;
	}

	

}
