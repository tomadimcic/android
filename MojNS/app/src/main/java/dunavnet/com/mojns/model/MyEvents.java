package dunavnet.com.mojns.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomek on 5.9.2015.
 */
public class MyEvents {
    private String date;
    private String time;
    private String description;
    private String id;
    private String status;

    public MyEvents() {
    }

    public MyEvents(String date, String time, String description, String id,
                    String status) {
        super();
        this.date = date;
        this.time = time;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public static ArrayList<MyEvents> toJson(String json){
        ArrayList<MyEvents> enl = new ArrayList<MyEvents>();

        try {
            JSONObject jo = new JSONObject(json);
            //{"Data":[{"Date":"4.8.2015 0:00:00","Time":"18:31","Description":": dfsg","RequestId":"38/2015","ResponseId":"","Status":"EVIDENTIRANA"}],"IsError":false,"ErrorMessage":null}

            JSONArray ja = new JSONArray(jo.getString("Data"));
            for (int i = 0; i < ja.length(); i++) {
                MyEvents en = new MyEvents();
                en.setDate(ja.getJSONObject(i).getString("Date"));
                en.setTime(ja.getJSONObject(i).getString("Time"));
                en.setDescription(ja.getJSONObject(i).getString("Description"));
                en.setId(ja.getJSONObject(i).getString("RequestId"));
                en.setStatus(ja.getJSONObject(i).getString("Status"));
                enl.add(en);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return enl;
    }
}
