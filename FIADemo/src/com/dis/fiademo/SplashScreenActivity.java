package com.dis.fiademo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.model.Beacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class SplashScreenActivity extends Activity {

    protected int splashTime = 100;
    DatabaseHandler db;
    boolean prvoPokretanje;
    boolean isFinished;
    AlertDialog.Builder builder;
    String ipAddress;
    String fourorsix;;
    private SharedPreferences prefs;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.splashscreen);
	
	Intent iin = getIntent();
	Bundle b = iin.getExtras();
	username = b.getString("username");

	db = new DatabaseHandler(this);
	prefs = PreferenceManager
		.getDefaultSharedPreferences(SplashScreenActivity.this);

	fourorsix = prefs.getString("ip", "");

	if (fourorsix.equals("4"))
	    ipAddress = db.getIpv4Address();
	else
	    ipAddress = "[" + db.getIpv6Address() + "]";
	

	new DatabaseSyncTask().execute();

    }

    public void startMain() {
	Intent i = new Intent(this, MainActivity.class);
	startActivity(i);
	finish();
    }

    public class DatabaseSyncTask extends AsyncTask<Void, Void, String> {

	protected void onPreExecute() {

	}

	protected String doInBackground(Void... arg0) {

	    TelephonyManager telephonyManager =
		(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
	    String response = null;
	    if (!ipAddress.contains("coap"))
		ipAddress = "coap://" + ipAddress;
	    ipAddress = ipAddress + ":5683/beacon";
	    Request req = new GETRequest();
	    req.setURI(ipAddress);
	    System.out.println("kkkkkkkkkkkkkk " + username);
	    JSONObject json = new JSONObject();
		try {
		    json.put("user", username);
		    json.put("imei", imei);
		} catch (JSONException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		req.setPayload(json.toString());
	    req.enableResponseQueue(true);

	    try {
		req.execute();
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    }
	    Response res = null;
	    try {
		res = req.receiveResponse();
		//System.out.println(res.getCode());
		response = res.getPayloadString();
		// if(res.getCode() == 68 || res.getCode() == 69
		// || res.getCode() == 65 || res.getCode() == 67)
		// response = "success";
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    }
	    return response;

	}

	@Override
	protected void onPostExecute(String result) {
	    //JSONObject myAwway;
	    JSONArray jsonBaconList = null;
	    System.out.println(result);
	    try {
		//String pom = "[{\"id\":\"2\",\"title\":\"test\",\"urlCMS\":\"dfgh\",\"IPv4CMS\":\"ccgh\",\"IPv6CMS\":\"ffgj\",\"gps\":\"44.8773;20.6576\",\"distance\":10,\"imei\":\"352136062655291\",\"user\":\"toma\",\"email\":\"null\",\"sms\":\"null\"},{\"id\":\"5\",\"title\":\"newtest\",\"urlCMS\":\"fgh\",\"IPv4CMS\":\"cgh\",\"IPv6CMS\":\"xch\",\"gps\":\"44.8773;20.6576\",\"distance\":50,\"imei\":\"352136062655291\",\"user\":\"toma\",\"email\":\"null\",\"sms\":\"null\"},{\"id\":\"6\",\"title\":\"new\",\"urlCMS\":\"cvv\",\"IPv4CMS\":\"dfg\",\"IPv6CMS\":\"xgh\",\"gps\":\"44.8773;20.6576\",\"distance\":90,\"imei\":\"352136062655291\",\"user\":\"toma\",\"email\":\"null\",\"sms\":\"null\"}]";
		jsonBaconList = new JSONArray(result);
	    
		//jsonBaconList = myAwway.getJSONArray(pom);
	    

		ArrayList<Beacon> beaconList = db.getAllBeacons();
		db.deleteAllBeacons();
		
		for (int i = 0; i < jsonBaconList.length(); i++) {
		    Beacon beacon = Beacon.fromJson(jsonBaconList.getJSONObject(i));
		    for (Beacon beacon1 : beaconList) {
			if(beacon1.getTitle().equals(beacon.getTitle()))
			    beacon.setActivated(beacon1.getActivated());
		    }
		    db.addBeacon(beacon);
		}
	    } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    // if(prvoPokretanje)
	    // progressDialog.dismiss();
		    db.close();
	    startMain();

	}
    }

}