package com.nt.najboljekafane.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.TextView;

import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.util.DataManager;

public class GetAktuelnaDesavanjaTask extends AsyncTask<Void, Void, ArrayList<Desavanje>>{

    
    public static final String ACTUELNA_DESAVANJA_GET = "http://www.najboljekafane.rs/android/webapi/desavanja_get.php";

	private ArrayList<Desavanje> result;
	TextView tv;
	
	public GetAktuelnaDesavanjaTask(TextView tv){
	    this.tv = tv;
	}
    

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	//progress = ProgressDialog.show(getApplicationContext(), "Sync",
		//"Preuzimanje desavanja..");
    }

    @Override
    protected ArrayList<Desavanje> doInBackground(Void... arg0) {
		
	try {
	    result = new ArrayList<Desavanje>();
	    URL url = new URL(ACTUELNA_DESAVANJA_GET);
	    URLConnection conn = url.openConnection();

	    BufferedReader rd = new BufferedReader(new InputStreamReader(
		    conn.getInputStream(), "UTF-8"));
	    String line = null;
	    String response = "";
	    while ((line = rd.readLine()) != null) {
		response += line;
	    }
	    if (response.equals("")) {
		System.out.println(response);
		JSONArray ja = new JSONArray(response);
		
		for (int i = 0; i < ja.length(); i++) {
		    Desavanje desavanja = new Desavanje();
		    desavanja.setId(ja.getJSONObject(i).getString("id"));
		    desavanja.setNaslov(ja.getJSONObject(i).getString("naslov"));
		    desavanja.setGrad(ja.getJSONObject(i).getString("grad"));
		    desavanja.setOpis(ja.getJSONObject(i).getString("opis"));
		    desavanja.setDatum(ja.getJSONObject(i).getString("datum"));
		    desavanja.setKafana(ja.getJSONObject(i).getString("naziv_kafane"));
		    desavanja.setVip(ja.getJSONObject(i).getString("vip"));
		    result.add(desavanja);
		   
		}
		
		DataManager.getInstance().setAktuelnaDesavanjaList(result);

		return result;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    return new ArrayList<Desavanje>();
	}
		return new ArrayList<Desavanje>();


    }
    
    @Override
    protected void onPostExecute(ArrayList<Desavanje> result) {
	//progress.dismiss();
	String text = "";
	if (result != null) {
	    //Toast.makeText(getApplicationContext(), "Error during device list sync",
		    //Toast.LENGTH_SHORT).show();
	    this.result = result;
	    
	    for (Desavanje desavanja : result) {
		text += desavanja.getDatum() + "-" + desavanja.getNaslov() + ": " + desavanja.getOpis() + " " + desavanja.getGrad() + "; ";
	    }
	    
	}
	else{
	    text = "Nema novih dogadjaja!";
	}
	System.out.println("+++++++ " + text);
	tv.setText(text);
    }

}
