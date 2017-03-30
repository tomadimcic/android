package com.iotwear.wear.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.iotwear.wear.model.Alarm;
import com.iotwear.wear.model.Animation;
import com.iotwear.wear.model.PiDevice;

public class DataManager {
	public static final String FIRST_RUN = "firstRun";
	public static final String FIRST_SYNC = "isFirstSync";
	public static final String DEVICE_LIST = "deviceList";
	public static final String ALARM_LIST = "alarmList";
	public static final String COLOR_LIST = "colorList";
	public static final String ANIMATION_LIST = "animationList";

	private static DataManager instance;
	private static SharedPreferences prefs;

	public static void initSettingsManager(Context ctx) {
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		instance = new DataManager();
	}

	public static DataManager getInstance() {
		return instance;
	}

	private DataManager() {

	}

	public void clearLists() {
		prefs.edit().remove(ANIMATION_LIST).remove(COLOR_LIST)
				.remove(DEVICE_LIST).commit();
	}

	public boolean isFirstRun() {
		return prefs.getBoolean(FIRST_RUN, true);
	}

	public void setFirstRun(boolean firstRun) {
		prefs.edit().putBoolean(FIRST_RUN, firstRun).commit();
	}

	public boolean isFirstSync() {
		return prefs.getBoolean(FIRST_SYNC, true);
	}

	public void setFirstDevice(boolean isFirstSync) {
		prefs.edit().putBoolean(FIRST_SYNC, isFirstSync).commit();
	}

	public ArrayList<PiDevice> getDeviceList() {
		ArrayList<PiDevice> deviceList = new ArrayList<PiDevice>();
		try {
			JSONArray devArray = getDeviceListJson();
			for (int i = 0; i < devArray.length(); i++) {
				PiDevice pi = PiDevice.fromJsonLocal(devArray.getJSONObject(i));
				if (pi != null)
					deviceList.add(pi);

			}
		} catch (JSONException e) {
			return deviceList;
		}
		return deviceList;
	}
	
	public ArrayList<PiDevice> getDeviceListForSending() {
		ArrayList<PiDevice> deviceList = new ArrayList<PiDevice>();
		try {
			JSONArray devArray = getDeviceListJson();
			for (int i = 0; i < devArray.length(); i++) {
				PiDevice pi = PiDevice.fromJson(devArray.getJSONObject(i));
				if (pi != null)
					deviceList.add(pi);

			}
		} catch (JSONException e) {
			return deviceList;
		}
		return deviceList;
	}

	private JSONArray getDeviceListJson() throws JSONException {
		String devString = prefs.getString(DEVICE_LIST,
				new JSONArray().toString());
		return new JSONArray(devString);
	}

	public boolean saveDevice(PiDevice device) {
		ArrayList<PiDevice> deviceList = getDeviceList();
		JSONArray devListJson = new JSONArray();
		try {
			for (PiDevice pi : deviceList) {
				if (device.getLocalIp().equals(pi.getLocalIp())
						&& device.getPort() == pi.getPort()
						&& device.getSsid().equals(pi.getSsid())) {
					devListJson.put(PiDevice.toJsonForSaving(device));
				} else {
					devListJson.put(PiDevice.toJsonForSaving(pi));
				}

			}
			return saveDeviceList(devListJson.toString());
		} catch (JSONException e) {
			return false;
		}

	}

	private boolean saveDeviceList(String json) {
		return prefs.edit().putString(DEVICE_LIST, json).commit();
	}

	public boolean addDevice(PiDevice pi) {
		try {
			JSONArray devices = getDeviceListJson();
			JSONObject dev = PiDevice.toJsonForSaving(pi);
			if (dev != null) {
				devices.put(dev);
				saveDeviceList(devices.toString());
				return true;
			}
			return false;
		} catch (JSONException e) {
			return false;
		}
	}

	public boolean deleteDevice(PiDevice pi) {
		ArrayList<PiDevice> piList = new ArrayList<PiDevice>();
		piList = getDeviceList();
		JSONArray devListJson = new JSONArray();

		for (PiDevice pid : piList) {
			if (!pid.equals(pi)) {
				try {
					devListJson.put(PiDevice.toJson(pid));
				} catch (JSONException e) {
					e.getStackTrace();
				}
			}
		}
		return saveDeviceList(devListJson.toString());
	}

	public void clearDeviceList() {
		prefs.edit().putBoolean(FIRST_SYNC, true).remove(DEVICE_LIST).commit();
	}

	public ArrayList<Integer> getColors() {
		ArrayList<Integer> colorList = new ArrayList<Integer>();
		String colorsCSV = prefs.getString(COLOR_LIST, "");
		String[] colors = colorsCSV.split(",");
		for (int i = 0; i < colors.length; i++) {
			if (!colors[i].equals(""))
				colorList.add(Integer.valueOf(colors[i]));
		}
		return colorList;
	}

	public boolean saveColor(int color) {
		ArrayList<Integer> colorList = DataManager.getInstance().getColors();
		for (Integer col : colorList) {
			if (col == color)
				return true;
		}

		colorList.add(color);

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < colorList.size(); i++) {
			if (i != 0)
				builder.append(",");
			builder.append(String.valueOf(colorList.get(i)));
		}

		return prefs.edit().putString(COLOR_LIST, builder.toString()).commit();
	}

	public void deleteColor(int color) {
		ArrayList<Integer> colorList = DataManager.getInstance().getColors();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < colorList.size(); i++) {
			if (color != colorList.get(i)) {
				if (i != 0)
					builder.append(",");
				builder.append(String.valueOf(colorList.get(i)));
			}
		}

		prefs.edit().putString(COLOR_LIST, builder.toString()).commit();
	}

	public boolean isSavedColor(int color) {
		ArrayList<Integer> colorList = DataManager.getInstance().getColors();
		for (Integer c : colorList) {
			if (c == color)
				return true;
		}

		return false;
	}

	public ArrayList<Animation> getAnimationList() {
		ArrayList<Animation> animationList = new ArrayList<Animation>();
		try {
			JSONArray devArray = getAnimationListJson();
			for (int i = 0; i < devArray.length(); i++) {
				Animation a = Animation.fromJson(devArray.getJSONObject(i));
				if (a != null)
					animationList.add(a);
			}
		} catch (JSONException e) {
			return animationList;
		}
		return animationList;
	}

	private JSONArray getAnimationListJson() throws JSONException {
		String animString = prefs.getString(ANIMATION_LIST,
				new JSONArray().toString());
		return new JSONArray(animString);
	}

	public boolean addAnimation(Animation a) {
		try {
			JSONArray animations = getAnimationListJson();
			JSONObject anim = a.toJson();
			if (anim != null) {
				animations.put(anim);
				saveAnimationList(animations.toString());
				return true;
			}
			return false;
		} catch (JSONException e) {
			return false;
		}
	}

	private boolean saveAnimationList(String json) {
		return prefs.edit().putString(ANIMATION_LIST, json).commit();
	}

	public boolean deleteAnimation(Animation animation) {
		ArrayList<Animation> animationList = new ArrayList<Animation>();
		animationList = getAnimationList();
		JSONArray animationListJson = new JSONArray();
		for (Animation a : animationList) {
			if (!a.getName().equals(animation.getName()))
				animationListJson.put(a.toJson());
		}
		return saveAnimationList(animationListJson.toString());
	}
	
	public boolean addAlarmList(Alarm alarm) {
	    try {
		JSONArray alarms = getAlarmListJson();
		JSONObject ala = Alarm.toJsonForSaving(alarm);
		if (ala != null) {
		    alarms.put(ala);
		    saveAlarmList(alarms.toString());
			return true;
		}
		return false;
	} catch (JSONException e) {
		return false;
	}
	}
	
	public ArrayList<Alarm> getAlarmList() {
		ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
		try {
			JSONArray alArray = getAlarmListJson();
			for (int i = 0; i < alArray.length(); i++) {
				Alarm alarm = Alarm.fromJson(alArray.getJSONObject(i));
				if (alarm != null)
					alarmList.add(alarm);

			}
		} catch (JSONException e) {
			return alarmList;
		}
		return alarmList;
	}
	
	public ArrayList<Alarm> getAlarmListForDevice(PiDevice selectedDevice) {
		ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
		try {
			JSONArray alArray = getAlarmListJson();
			for (int i = 0; i < alArray.length(); i++) {
				Alarm alarm = Alarm.fromJson(alArray.getJSONObject(i));
				if (alarm != null){
				    if(alarm.getDeviceName().equals(selectedDevice.getName()))
					alarmList.add(alarm);
				}

			}
		} catch (JSONException e) {
			return alarmList;
		}
		return alarmList;
	}
	
	private JSONArray getAlarmListJson() throws JSONException {
		String alarmString = prefs.getString(ALARM_LIST,
				new JSONArray().toString());
		return new JSONArray(alarmString);
	}
	
	private boolean saveAlarmList(String json) {
		return prefs.edit().putString(ALARM_LIST, json).commit();
	}
	
	public boolean saveAlarm(Alarm alarm) throws JSONException {
		ArrayList<Alarm> alarmList = getAlarmList();
		JSONArray alarmListJson = new JSONArray();
		for (Alarm a : alarmList) {
		    if(alarm.getDeviceName().equals(a.getDeviceName())){
			if (alarm.getName().equals(a.getName())) {
			    alarmListJson.put(Alarm.toJsonForSaving(alarm));
			} 
		    }else {
			alarmListJson.put(Alarm.toJsonForSaving(a));
		    }

		}
		return saveAlarmList(alarmListJson.toString());

	}
	
    public boolean saveAlarmList(ArrayList<Alarm> alarms) throws JSONException {
	ArrayList<Alarm> alarmList = getAlarmList();
	JSONArray alarmListJson = new JSONArray();
	for (Alarm a : alarmList) {
	    for (Alarm alarm : alarms) {
		if (alarm.getDeviceName().equals(a.getDeviceName())) {
		    if (alarm.getName().equals(a.getName())) {
			alarmListJson.put(Alarm.toJsonForSaving(alarm));
		    }
		} else {
		    alarmListJson.put(Alarm.toJsonForSaving(a));
		}
	    }

	}
	return saveAlarmList(alarmListJson.toString());

    }
	
	public boolean deleteAlarm(Alarm alarm) {
		ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
		alarmList = getAlarmList();
		JSONArray alarmListJson = new JSONArray();

		for (Alarm a : alarmList) {
			if (!a.equals(alarm)) {
				    alarmListJson.put(Alarm.toJsonForSaving(a));
			}
		}
		return saveAlarmList(alarmListJson.toString());
	}
}
