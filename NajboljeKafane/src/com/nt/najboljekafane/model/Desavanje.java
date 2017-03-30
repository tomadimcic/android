package com.nt.najboljekafane.model;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.bugsense.trace.BugSenseHandler;

public class Desavanje implements Serializable{

	private static final long serialVersionUID = 6833885799133492302L;

	private String id;
    private String desavanjeId;
    private String naslov;
    private String grad;
    private String opis;
    private String datum;
    private String dan;
    private String kafana;
    private String kafanaId;
    private String vip;
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }
    
    public String getKafana() {
        return kafana;
    }

    public void setKafana(String kafana) {
        this.kafana = kafana;
    }
    
    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }
    
    public String getDesavanjeId() {
        return desavanjeId;
    }

    public void setDesavanjeId(String desavanjeId) {
        this.desavanjeId = desavanjeId;
    }
    
    public String getKafanaId() {
        return kafanaId;
    }

    public void setKafanaId(String kafanaId) {
        this.kafanaId = kafanaId;
    }
    
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public static Desavanje fromJson(JSONObject json) {
	Desavanje desavanja = new Desavanje();
	try {
		desavanja.setDesavanjeId(json.getString("id"));
		desavanja.setNaslov(json.getString("naslov"));
		desavanja.setGrad(json.getString("grad"));
		desavanja.setOpis(json.getString("opis"));
		desavanja.setDatum(json.getString("datum"));
		desavanja.setDan(json.getString("dan"));
		desavanja.setKafana(json.getString("naziv_kafane"));
		desavanja.setKafanaId(json.getString("kafana_id"));
		desavanja.setVip(json.getString("vip"));
		desavanja.setTimestamp(json.getString("timestamp"));
		return desavanja;
	} catch (JSONException e) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("desavanja_json", json.toString());
		BugSenseHandler.sendExceptionMap(map, e);
		return null;
	}
}


}
