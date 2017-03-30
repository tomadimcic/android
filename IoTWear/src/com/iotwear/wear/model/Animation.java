package com.iotwear.wear.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

public class Animation {
	public static final String NAME = "name";
	public static final String LIGHT_ON_DURATION = "lo";
	public static final String LIGHT_OFF_DURATION = "lo";
	public static final String COLOR_LIST = "colorList";
	//public static final String CROSSFADE_DURATION = "crossfade";
	public static final String CROSSFADE_DURATION = "c";

	public static final String LIGHT_ON = "lo";
	public static final String RED = "r";
	public static final String GREEN = "g";
	public static final String BLUE = "b";

	private String name;

	private ArrayList<Integer> colorList;
	private ArrayList<Double> onList;
	private ArrayList<Double> crossfadeList;

	private double lightOnDuration;
	private double lightOffDuration;

	private int crossfadeDuration;

	public Animation() {
		colorList = new ArrayList<Integer>();
		onList = new ArrayList<Double>();
		crossfadeList = new ArrayList<Double>();
		name = new String();
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(NAME, name);

			//json.put(CROSSFADE_DURATION, crossfadeDuration);
			//json.put(LIGHT_ON_DURATION, lightOnDuration);
			//json.put(LIGHT_OFF_DURATION, lightOffDuration);

			StringBuilder builder = new StringBuilder();
			StringBuilder builder1 = new StringBuilder();
			StringBuilder builder2 = new StringBuilder();
			for (int i = 0; i < getColorList().size(); i++) {
				if (i != 0){
					builder.append(",");
					builder1.append(",");
					builder2.append(",");
				}

				builder.append(getColorList().get(i));
				builder1.append(String.valueOf(getOnList().get(i)));
				builder2.append(String.valueOf(getCrossfadeList().get(i)));
			}
			json.put(COLOR_LIST, builder.toString());
			json.put(LIGHT_ON_DURATION, builder1.toString());
			json.put(CROSSFADE_DURATION, builder2.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	public String toJsonForSending() {
		JSONArray jsonArray = new JSONArray();
		try {
			for (int i = 0; i < getColorList().size(); i++) {
				JSONObject json = new JSONObject();
				json.put(CROSSFADE_DURATION, getCrossfadeList().get(i));
				json.put(RED, Color.red(getColorList().get(i)));
				json.put(GREEN, Color.green(getColorList().get(i)));
				json.put(BLUE, Color.blue(getColorList().get(i)));
				json.put(LIGHT_ON_DURATION, getOnList().get(i)*10);
				jsonArray.put(json);

				if (lightOffDuration > 0) {
					JSONObject jsonOff = new JSONObject();
					jsonOff.put(CROSSFADE_DURATION, crossfadeDuration);
					jsonOff.put(RED, 0);
					jsonOff.put(GREEN, 0);
					jsonOff.put(BLUE, 0);
					jsonOff.put(LIGHT_OFF_DURATION, lightOffDuration);
					jsonArray.put(jsonOff);
				}
			}
		} catch (JSONException e) {
			e.getStackTrace();
		}

		return jsonArray.toString();
	}

	// json.put(NAME, name);

	/*
	 * Evo ti primer animation fajla (ovo se nalazi na Raspberry-u):
	 * 
	 * crossfade=2,red=255,green=0,blue=0,light_on=5
	 * crossfade=2,red=0,green=0,blue=0,light_on=3
	 * crossfade=2,red=0,green=255,blue=0,light_on=4
	 * crossfade=1,red=0,green=0,blue=0,light_on=2
	 * crossfade=3,red=0,green=0,blue=255,light_on=6
	 * crossfade=1,red=0,green=0,blue=0,light_on=3
	 * 
	 * Ovo ce paliti i gasiti redom crvenu, zelenu i plavu sa gasenjem gde su
	 * definisani crossfade-ovi i trajanje upaljene i ugasene boje
	 */

	/*
	 * json.put(HAS_FADE_IN, hasFadeIn); if (hasFadeIn) {
	 * json.put(FADE_IN_DURATION, fadeInDuration); }
	 * 
	 * json.put(HAS_FADE_OUT, hasFadeOut); if (hasFadeOut) {
	 * json.put(FADE_OUT_DURATION, fadeOutDuration); }
	 * 
	 * json.put(ACTIVE_DURATION, activeDuration); json.put(PASSIVE_DURATION,
	 * passiveDuration);
	 */

	/*
	 * if (passiveDuration > 0) {
	 * 
	 * for (int i = 0; i < colorList.size(); i++) {
	 * 
	 * redColor = Color.red(colorList.get(i)); greenColor =
	 * Color.green(colorList.get(i)); blueColor = Color.blue(colorList.get(i));
	 * 
	 * json.put(CROSSFADE_DURATION, crossfadeDuration);
	 * 
	 * json.put(RED, redColor); json.put(GREEN, greenColor); json.put(BLUE,
	 * blueColor); json.put(ACTIVE_DURATION, activeDuration); json.put(RED,
	 * "0"); json.put(GREEN, "0"); json.put(BLUE, "0");
	 * json.put(PASSIVE_DURATION, passiveDuration);
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * for (int i = 0; i < colorList.size(); i++) {
	 * 
	 * redColor = Color.red(colorList.get(i)); greenColor =
	 * Color.green(colorList.get(i)); blueColor = Color.blue(colorList.get(i));
	 * 
	 * json.put(CROSSFADE_DURATION, crossfadeDuration);
	 * 
	 * json.put(RED, redColor); json.put(GREEN, greenColor); json.put(BLUE,
	 * blueColor); json.put(ACTIVE_DURATION, activeDuration);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); }
	 * 
	 * return json; }
	 */

	public static Animation fromJson(JSONObject json) {
		Animation a = new Animation();
		try {
			a.setName(json.getString(NAME));
			//a.lightOnDuration = json.getInt(LIGHT_ON_DURATION);
			//a.lightOffDuration = json.getInt(LIGHT_OFF_DURATION);
			//a.crossfadeDuration = json.getInt(CROSSFADE_DURATION);

			String colorString = json.getString(COLOR_LIST);
			String[] colors = colorString.split(",");
			
			String onString = json.getString(LIGHT_ON_DURATION);
			String[] on = onString.split(",");
			
			String crossString = json.getString(CROSSFADE_DURATION);
			String[] crossfade = crossString.split(",");

			for (int i = 0; i < colors.length; i++) {
			    a.onList.add(Double.parseDouble(on[i]));
			    a.crossfadeList.add(Double.parseDouble(crossfade[i]));
				a.colorList.add(Integer.valueOf(colors[i]));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return a;
	}

	public String colorToHex(int color) {
		return String.format("#%06X", (0xFFFFFF & color));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLightOnDuration() {
		return lightOnDuration*10;
	}

	public void setLightOnDuration(double lightOnDuration) {
		this.lightOnDuration = lightOnDuration*10;
	}

	public double getLightOffDuration() {
		return lightOffDuration*10;
	}

	public void setLightOffDuration(double lightOffDuration) {
		this.lightOffDuration = lightOffDuration*10;
	}

	public int getCrossfadeDuration() {
		return crossfadeDuration;
	}

	public void setCrossfadeDuration(int crossfadeDuration) {
		this.crossfadeDuration = crossfadeDuration;
	}

	public ArrayList<Integer> getColorList() {
		return colorList;
	}

	public void setColorList(ArrayList<Integer> colorList) {
		this.colorList = colorList;
	}

	public ArrayList<Double> getOnList() {
	    return onList;
	}

	public void setOnList(ArrayList<Double> onList) {
	    this.onList = onList;
	}

	public ArrayList<Double> getCrossfadeList() {
	    return crossfadeList;
	}

	public void setCrossfadeList(ArrayList<Double> crossfadeList) {
	    this.crossfadeList = crossfadeList;
	}
	
	
}
