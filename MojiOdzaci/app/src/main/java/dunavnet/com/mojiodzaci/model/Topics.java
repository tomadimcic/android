package dunavnet.com.mojiodzaci.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tomek on 18.11.2015.
 */
public class Topics implements Serializable {

    private String topicId;
    private String name;

    public Topics(String topicId, String name){
        this.setTopicId(topicId);
        this.setName(name);
    }


    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<Topics> fromStringJson(String json){
        ArrayList<Topics> cat = new ArrayList<Topics>();

        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                Topics c = new Topics(ja.getJSONObject(i).getString("TopicId"), ja.getJSONObject(i).getString("Name"));
                cat.add(c);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return cat;
    }
}
