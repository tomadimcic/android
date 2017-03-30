package dunavnet.com.mojiodzaci.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomek on 6.9.2015.
 */
public class Categories {

    private String categoryId;
    private String name;

    public Categories(String categoryId, String name){
        this.setCategoryId(categoryId);
        this.setName(name);
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<Categories> fromStringJson(String json){
        ArrayList<Categories> cat = new ArrayList<Categories>();

        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                Categories c = new Categories(ja.getJSONObject(i).getString("CategoryId"), ja.getJSONObject(i).getString("Name"));
                cat.add(c);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return cat;
    }
}
