package com.iotwear.wear.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class PiControl implements Serializable {

	private static final long serialVersionUID = 8659330678782227852L;

	public static final String ID = "id";
	//public static final String NAME = "name";
	public static final String NAME = "n";
	//public static final String TYPE = "type";
	public static final String TYPE = "t";
	public static final String VALUE = "value";
	public static final String ICON1 = "icon";
	
	/*public static final String ID = "id";
	public static final String NAME = "name";
	//public static final String NAME = "n";
	public static final String TYPE = "type";
	//public static final String TYPE = "t";
	public static final String VALUE = "value";
	public static final String ICON1 = "icon";*/

	protected String id;
	protected int[] blindsId;
	protected PiControlType piControlType;
	protected String name;
	protected PiDevice hostDevice;
	private int icon;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PiControlType getPiControlType() {
		return piControlType;
	}

	public void setPiControlType(PiControlType piControlType) {
		this.piControlType = piControlType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract int getIcon();

	public abstract void setIcon(int icon);

	public static PiControl buildFromJson(JSONObject json, PiDevice piDevice, int local) {
		try {
			PiControl ctrl = null;

			int type = json.optInt(TYPE, -1);

			if (type == -1) {
			    ArrayList<PiControl> switchList = new ArrayList<PiControl>();
			    for (PiControl piControl : piDevice.getControlList()) {
				if(piControl.getPiControlType() == PiControlType.SWITCH)
				    switchList.add(piControl);
			    }
				for (PiControl pi : switchList) {
					if (type != 5 && type != 6 && pi.getId().equals(json.getString(ID))) {
						ctrl = pi;

						if (ctrl.getPiControlType() == PiControlType.SWITCH) {
							((SwitchControl) ctrl).setTurnedOn(json
									.optBoolean(VALUE));
						}

						if (ctrl.getPiControlType() == PiControlType.DIMMER) {
							int val = json.optInt(VALUE, -1);
							if (val >= 0)
								((DimControl) ctrl).setDimValue(val);
						}
						
						if (ctrl.getPiControlType() == PiControlType.TASTER) {
							((TasterControl) ctrl).setTurnedOn(json
									.optBoolean(VALUE));
						}
						break;
					}
				}
			} else {

				switch (json.getInt(TYPE)) {
				case 0:
					ctrl = new LEDControl();
					break;
				case 1:
					ctrl = new SwitchControl();
					break;
				case 2:
					ctrl = new DimControl();
					break;
				case 3:
					ctrl = new GroupControl();
					break;
				case 4:
					ctrl = new TasterControl();
					break;
				case 5:
					ctrl = new BlindsControl(0);
					break;
				case 6:
					ctrl = new BlindsControl(1);
					break;
				case 7:
				    	ctrl = new ICControl();
					break;
				case 9:
				    	ctrl = new DMXControl();
					break;
				case 10:
				    	ctrl = new LampControl();
					break;
				}

				if(ctrl == null)
				    return null;
				ctrl.setId(json.getString(ID));
				ctrl.setName(json.getString(NAME));
			}

			if (ctrl.getPiControlType() == PiControlType.GROUP) {
				JSONArray groupControlJson = json
						.getJSONArray(GroupControl.CONTROLLERS);
				for (int i = 0; i < groupControlJson.length(); i++) {
					((GroupControl) ctrl).addControl(PiControl.buildFromJson(
							groupControlJson.getJSONObject(i), piDevice, local));
				}
			}
			
			System.out.println(local);
			if(local == 1){
			    try {
				ctrl.setIcon(json.optInt(ICON1, 0));
			    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}
			  

			return ctrl;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static PiControl buildFromJsonNew(JSONObject json, PiDevice piDevice, int number) {
		try {
			PiControl ctrl = null;

			int type = json.optInt(TYPE, -1);

			if (type == -1) {
				
			}else{
				switch (json.getInt(TYPE)) {
				case 0:
					ctrl = new LEDControl();
					break;
				case 1:
					ctrl = new SwitchControl();
					break;
				case 2:
					ctrl = new DimControl();
					break;
				case 3:
					ctrl = new GroupControl();
					break;
				case 4:
					ctrl = new TasterControl();
					break;
				case 5:
					ctrl = new BlindsControl(0);
					break;
				case 6:
					ctrl = new BlindsControl(1);
					break;
				case 7:
				    	ctrl = new ICControl();
					break;
				case 9:
				    	ctrl = new DMXControl();
					break;
				case 10:
				    	ctrl = new LampControl();
					break;
				}
			}
			
			if(ctrl == null)
			    return null;
			ctrl.setId(Integer.toString(number));
			ctrl.setName(json.getString(NAME) + Integer.toString(number));
			
			
			return ctrl;
		} catch (JSONException e) {
			return null;
		}
	}

	public PiDevice getHostDevice() {
		return hostDevice;
	}

	public void setHostDevice(PiDevice hostDevice) {
		this.hostDevice = hostDevice;
	}

	public enum PiControlType {
		LED, SWITCH, DIMMER, GROUP, TASTER, BLINDS, BLINDS2, IC, INPUT, DMX, LAMP
	}

	public abstract String toJsonForSending();
	
	public abstract String toQueryForSending();

	public abstract JSONObject toJson() throws JSONException;
	
	public abstract JSONObject toJsonforSaving() throws JSONException;

}
