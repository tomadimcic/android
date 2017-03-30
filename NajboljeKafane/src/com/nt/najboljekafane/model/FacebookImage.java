package com.nt.najboljekafane.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bugsense.trace.BugSenseHandler;

public class FacebookImage implements Serializable {

	private static final long serialVersionUID = 4108172900158424381L;

	public long id;
	public String icon;
	public String picture;
	public String name;
	public String date;
	public String source;
	public String link;
	public HashMap<Integer, String> sizeImageMap;
	public ArrayList<Long> likesIdList;
	public int likesCount;
	public boolean isLiked;
	public String comment;

	public FacebookImage() {

	}

	public static FacebookImage fromJson(JSONObject json) {
		FacebookImage fbImg = new FacebookImage();
		try {
			fbImg.id = json.getLong("id");
			fbImg.picture = json.getString("picture");
			fbImg.source = json.getString("source");
			fbImg.link = json.getString("link");
			fbImg.sizeImageMap = new HashMap<Integer, String>(3);
			JSONArray imageArray = json.getJSONArray("images");
			int min = imageArray.length() > 3 ? 3 : imageArray.length();
			for (int i = 0; i < min; i++) {
				JSONObject img = imageArray.getJSONObject(i);
				fbImg.sizeImageMap.put(i, img.getString("source"));
			}
			try {
				JSONObject likesJson = json.getJSONObject("likes");
				JSONArray likesData = likesJson.getJSONArray("data");
				fbImg.likesCount = likesData.length();
				fbImg.likesIdList = new ArrayList<Long>(fbImg.likesCount);
				for (int i = 0; i < fbImg.likesCount; i++) {
					JSONObject like = likesData.getJSONObject(i);
					fbImg.likesIdList.add(like.getLong("id"));
				}
			} catch (JSONException e) {
				fbImg.likesCount = 0;
				fbImg.isLiked = false;
			}

			return fbImg;
		} catch (JSONException e) {
			BugSenseHandler.sendException(e);
			return null;
		}
	}

	public static FacebookImage fromPost(JSONObject json) {
		FacebookImage fbImg = new FacebookImage();
		try {
			fbImg.comment = json.getString("message");
			JSONObject attachment = json.getJSONObject("attachment");
			JSONArray media = attachment.getJSONArray("media");
			JSONObject inf = media.getJSONObject(0);
			fbImg.link = inf.getString("href");
			fbImg.source = inf.getString("src");

			fbImg.sizeImageMap = new HashMap<Integer, String>(3);
			fbImg.sizeImageMap.put(0, inf.getString("src"));

			String date = json.getString("created_time");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(date)*1000L);
			StringBuilder dateBuilder = new StringBuilder();
			dateBuilder.append(cal.get(Calendar.DAY_OF_MONTH)).append(".")
					.append(cal.get(Calendar.MONTH)).append(".")
					.append(cal.get(Calendar.YEAR)).append(".");
			fbImg.date = dateBuilder.toString();

			String comment = json.getString("message");
			if (comment != null)
				fbImg.comment = comment;
			else
				fbImg.comment = new String();

			return fbImg;
		} catch (JSONException e) {
			return null;
		}
	}
}
