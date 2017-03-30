package dunavnet.com.mojiodzaci.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tomek on 24.11.2015.
 */
public class MyEvents implements Serializable {
    private String date;
    private String time;
    private String adminDescription;
    private String description;
    private String id;
    private String responseId;
    private String status;
    private String gpsx;
    private String gpsy;
    private String imagePath;
    private String issueCategoryName;

    public MyEvents() {
    }

    public MyEvents(String date, String time, String description, String adminDescription, String id, String responseId,
                    String status, String gpsx, String gpsy, String imagePath, String issueCategoryName) {
        super();
        this.date = date;
        this.time = time;
        this.adminDescription = adminDescription;
        this.description = description;
        this.id = id;
        this.responseId = responseId;
        this.status = status;
        this.gpsx = gpsx;
        this.gpsy = gpsy;
        this.imagePath = imagePath;
        this.issueCategoryName = issueCategoryName;
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
    public String getAdminDescription() {
        return adminDescription;
    }
    public void setAdminDescription(String adminDescription) {
        this.adminDescription = adminDescription;
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
    public String getResponseId() {
        return responseId;
    }
    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssueCategoryName() {
        return issueCategoryName;
    }
    public void setIssueCategoryName(String issueCategoryName) {
        this.issueCategoryName = issueCategoryName;
    }

    public String getGpsx() {
        return gpsx;
    }

    public void setGpsx(String gpsx) {
        this.gpsx = gpsx;
    }

    public String getGpsy() {
        return gpsy;
    }

    public void setGpsy(String gpsy) {
        this.gpsy = gpsy;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static ArrayList<MyEvents> toJson(String json){
        ArrayList<MyEvents> enl = new ArrayList<MyEvents>();

        try {
            JSONObject jo = new JSONObject(json);
            //{
            // "IsError":false,
            // "ErrorMessage":"",
            // "Data":
            // [{"Date":"11/19/2015","Time":"14:08","AdminDescription":null,"UserDescription":"komentar","RequestId":null,"ResponseId":null,"Status":"Nepročitan","PhotoUrl":"http://clips.dunavnet.eu/Files/CommunalPoliceFiles/bac/Photo/9052a520-bf8b-44b2-8f1c-0b325ecfb8c5.JPEG","AudioUrl":null,"VideoUrl":null,"GPSX":"44.8773044","GPSY":"20.6575387"}]}
            JSONArray ja = new JSONArray(jo.getString("Data"));
            for (int i = 0; i < ja.length(); i++) {
                MyEvents en = new MyEvents();
                en.setDate(ja.getJSONObject(i).getString("Date"));
                en.setTime(ja.getJSONObject(i).getString("Time"));
                en.setAdminDescription(ja.getJSONObject(i).getString("AdminDescription"));
                en.setDescription(ja.getJSONObject(i).getString("UserDescription"));
                en.setId(ja.getJSONObject(i).getString("RequestId"));
                en.setResponseId(ja.getJSONObject(i).getString("ResponseId"));
                en.setStatus(ja.getJSONObject(i).getString("Status"));
                en.setImagePath(ja.getJSONObject(i).getString("PhotoUrl"));
                en.setGpsx(ja.getJSONObject(i).getString("GPSX"));
                en.setGpsy(ja.getJSONObject(i).getString("GPSY"));
                en.setIssueCategoryName(ja.getJSONObject(i).getString("IssueCategoryName"));

                enl.add(en);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return enl;
    }
}
