package com.nt.najboljekafane.model;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.bugsense.trace.BugSenseHandler;

public class Pesma implements Serializable {

	private static final long serialVersionUID = 2273391808157238145L;

	private int id;
	private String pesma_id;
	private String naziv;
	private String izvodjac;
	private int broj_glasova;
	private String tekst;
	private String timestamp;
	private String omiljena;
	private String yt;

	public Pesma() {
	}

	Pesma(String pesma_id, String naziv, String izvodjac, int broj_glasova,
			String tekst, String timestamp, String omiljena, String yt) {
		this.pesma_id = pesma_id;
		this.naziv = naziv;
		this.izvodjac = izvodjac;
		this.broj_glasova = broj_glasova;
		this.tekst = tekst;
		this.timestamp = timestamp;
		this.omiljena = omiljena;
		this.yt = yt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPesmaId() {
		return pesma_id;
	}

	public void setPesmaId(String pesma_id) {
		this.pesma_id = pesma_id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getIzvodjac() {
		return izvodjac;
	}

	public void setIzvodjac(String izvodjac) {
		this.izvodjac = izvodjac;
	}

	public int getBrojglasova() {
		return broj_glasova;
	}

	public void setBrojglasova(int broj_glasova) {
		this.broj_glasova = broj_glasova;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOmiljena() {
		return omiljena;
	}

	public void setOmiljena(String omiljena) {
		this.omiljena = omiljena;

	}
	
	public String getYoutube() {
		return yt;
	}

	public void setYoutube(String yt) {
		this.yt = yt;

	}

	public static Pesma fromJson(JSONObject json) {
		Pesma pesma = new Pesma();
		try {
			pesma.setPesmaId(json.getString("pesma_id"));
			pesma.setNaziv(json.getString("pesma_naslov"));
			pesma.setIzvodjac(json.getString("pesma_izvodjac"));
			pesma.setTekst(json.getString("pesma_text"));
			pesma.setBrojglasova(Integer.parseInt(json
					.getString("pesma_broj_glasova")));
			pesma.setTimestamp(json.getString("pesma_timestamp"));
			pesma.setYoutube(json.getString("pesma_youtube_link"));
			return pesma;
		} catch (JSONException e) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("pesma_json", json.toString());
			BugSenseHandler.sendExceptionMap(map, e);
			return null;
		}
	}

	public void setYoutubeLink(String string) {
		// TODO Auto-generated method stub
		
	}

}
