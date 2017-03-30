package com.nt.najboljekafane.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.bugsense.trace.BugSenseHandler;

public class Kafana implements Serializable {

	private static final long serialVersionUID = 5370469371307064906L;

	public static final String KAFANA_IMAGE_PREFIX = "http://www.najboljekafane.rs/android/images/certificates/";
	private int id;
	private String naziv;
	private String grad;
	private String adresa;
	private String telefon;
	private String websajt;
	private String opis;
	private String email;
	private float ocenaKafane;
	private float ocenaAtmosfere;
	private String slika1;
	private String slika2;
	private String slika3;
	private String slika4;
	private String slika5;
	private String lat;
	private String lng;
	private String verzijaBaze;
	private String vip;
	private String kafana_id;
	private String timestamp;
	private String redosled;
	private String thumb;
	private String vrsta;
	private String brojGlasova;
	private String radnoVreme;
	
	private ArrayList<String> slikeList;

	public Kafana() {
	};

	public Kafana(String kafana_id, String naziv, String grad, String adresa,
			String telefon, String websajt, String email, float ocenaAtmosfere,
			float ocenaKafane, String opis, String lat, String lng, String vip,
			String verzijaBaze, String slika1, String slika2, String slika3,
			String slika4, String slika5, String timestamp, String thumb, String vrsta, String brojGlasova, String radnoVreme) {

		this.kafana_id = kafana_id;
		this.naziv = naziv;
		this.grad = grad;
		this.adresa = adresa;
		this.telefon = telefon;
		this.websajt = websajt;
		this.email = email;
		this.ocenaAtmosfere = ocenaAtmosfere;
		this.ocenaKafane = ocenaKafane;
		this.opis = opis;
		this.lat = lat;
		this.lng = lng;
		this.verzijaBaze = verzijaBaze;
		this.vip = vip;
		this.slika1 = slika1;
		this.slika2 = slika2;
		this.slika3 = slika3;
		this.slika4 = slika4;
		this.slika5 = slika5;
		this.timestamp = timestamp;
		this.thumb = thumb;
		this.vrsta = vrsta;
		this.brojGlasova = brojGlasova;
		this.radnoVreme = radnoVreme;

	}

	public void addSlike() {
		this.slikeList = new ArrayList<String>();
		if (!this.slika1.equals("no_photo"))
			this.slikeList.add(KAFANA_IMAGE_PREFIX + this.slika1);
		if (!this.slika2.equals("no_photo"))
			this.slikeList.add(KAFANA_IMAGE_PREFIX + this.slika2);
		if (!this.slika3.equals("no_photo"))
			this.slikeList.add(KAFANA_IMAGE_PREFIX + this.slika3);
		if (!this.slika4.equals("no_photo"))
			this.slikeList.add(KAFANA_IMAGE_PREFIX + this.slika4);
		if (!this.slika5.equals("no_photo"))
			this.slikeList.add(KAFANA_IMAGE_PREFIX + this.slika5);

	}

	public ArrayList<String> getSlikeList() {
		addSlike();
		return slikeList;
	}

	public String getKafanaId() {
		return kafana_id;
	}

	public void setKafanaId(String kafana_id) {
		this.kafana_id = kafana_id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getWebsajt() {
		return websajt;
	}

	public void setWebsajt(String websajt) {
		this.websajt = websajt;
	}

	public float getOcenaAtmosfere() {
		return ocenaAtmosfere;
	}

	public void setOcenaAtmosfere(float ocenaAtmosfere) {
		this.ocenaAtmosfere = ocenaAtmosfere;
	}

	public float getOcenaKafane() {
		return ocenaKafane;
	}

	public void setOcenaKafane(float ocenaKafane) {
		this.ocenaKafane = ocenaKafane;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getVerzijaBaze() {
		return verzijaBaze;
	}

	public void setVerzijaBaze(String verzijaBaze) {
		this.verzijaBaze = verzijaBaze;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int i) {
		this.id = i;
	}

	public String getSlika1() {
		return slika1;
	}

	public void setSlika1(String slika1) {
		this.slika1 = slika1;
	}

	public String getSlika2() {
		return slika2;
	}

	public void setSlika2(String slika2) {
		this.slika2 = slika2;
	}

	public String getSlika3() {
		return slika3;
	}

	public void setSlika3(String slika3) {
		this.slika3 = slika3;
	}

	public String getSlika4() {
		return slika4;
	}

	public void setSlika4(String slika4) {
		this.slika4 = slika4;
	}

	public String getSlika5() {
		return slika5;
	}

	public void setSlika5(String slika5) {
		this.slika5 = slika5;
	}

	public String getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRedosled() {
		// TODO Auto-generated method stub
		return redosled;
	}

	public void setRedosled(String redosled) {
		this.redosled = redosled;
	}

	public String getThumb() {
		// TODO Auto-generated method stub
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
	public String getVrsta() {
		// TODO Auto-generated method stub
		return vrsta;
	}

	public void setVrsta(String vrsta) {
		this.vrsta = vrsta;
	}
	
	public String getBrojGlasova() {
		// TODO Auto-generated method stub
		return brojGlasova;
	}

	public void setBrojGlasova(String brojGlasova) {
		this.brojGlasova = brojGlasova;
	}
	
	public String getRadnoVreme() {
		// TODO Auto-generated method stub
		return radnoVreme;
	}

	public void setRadnoVreme(String radnoVreme) {
		this.radnoVreme = radnoVreme;
	}

	
	
	public static Kafana fromJson(JSONObject json) {
		Kafana kafana = new Kafana();
		try{
		kafana.setKafanaId(json.getString("kafana_id"));
		kafana.setNaziv(json.getString("kafana_naziv"));
		kafana.setGrad(json.getString("kafana_grad"));
		kafana.setAdresa(json.getString("kafana_adresa"));
		kafana.setWebsajt(json.getString("kafana_websajt"));
		kafana.setEmail(json.getString("kafana_email"));
		kafana.setOpis(json.getString("kafana_opis"));
		kafana.setTelefon(json.getString("kafana_broj_telefona"));
		kafana.setOcenaKafane(Float.valueOf(json
				.getString("kafana_ocena_kafane")));
		kafana.setOcenaAtmosfere(Float.valueOf(json
				.getString("kafana_ocena_atmosfere")));
		kafana.setSlika1(json.getString("kafana_slika1"));
		kafana.setSlika2(json.getString("kafana_slika2"));
		kafana.setSlika3(json.getString("kafana_slika3"));
		kafana.setSlika4(json.getString("kafana_slika4"));
		kafana.setSlika5(json.getString("kafana_slika5"));
		kafana.setVip(json.getString("kafana_vip"));
		kafana.setLat(json.getString("kafana_lat"));
		kafana.setLng(json.getString("kafana_lng"));
		kafana.setVerzijaBaze(json.getString("kafana_verzija"));
		kafana.setTimestamp(json.getString("kafana_timestamp"));
		kafana.setRedosled(json.getString("vip_redosled"));
		kafana.setThumb(json.getString("slika_thumb"));
		kafana.setVrsta(json.getString("vrsta"));
		kafana.setBrojGlasova(json.getString("broj_glasova"));
		//kafana.setBrojGlasova("0");
		if(!json.getString("radno_vreme").equals(null))
		    kafana.setRadnoVreme(json.getString("radno_vreme"));
		
		
		return kafana;
		}
		catch(JSONException e){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("kafana_json", json.toString());
			BugSenseHandler.sendExceptionMap(map, e);
			return null;
		}
	}


}
