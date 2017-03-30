package com.dis.fiademo;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.PUTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.model.Beacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddBeaconActivity extends Activity {

    Button myGPSButton;
    Button createButton;
    EditText gpsPositionEditText;
    EditText titleEditText;
    EditText distanceEditText;
    EditText emailEditText;
    EditText smsEditText;
    EditText urlEditText;
    EditText ipv4EditText;
    EditText ipv6EditText;
    TextView title;
    LocationManager mlocManager;
    LocationListener mlocListener;
    Beacon beacon;
    DatabaseHandler db;
    String ipAddress;
    String fourorsix;;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_beacon);
	
	db = new DatabaseHandler(this);
	prefs = PreferenceManager.getDefaultSharedPreferences(AddBeaconActivity.this);
	
	fourorsix = prefs.getString("ip","");
	
	if(fourorsix.equals("4"))
	    ipAddress = db.getIpv4Address();
	else
	    ipAddress = "[" + db.getIpv6Address() + "]";

	myGPSButton = (Button) findViewById(R.id.my_gps_button);
	createButton = (Button) findViewById(R.id.create_button);
	gpsPositionEditText = (EditText) findViewById(R.id.my_gps_edit_text);
	titleEditText = (EditText) findViewById(R.id.title_edit_text);
	distanceEditText = (EditText) findViewById(R.id.distance_edit_text);
	emailEditText = (EditText) findViewById(R.id.email_edit_text);
	smsEditText = (EditText) findViewById(R.id.sms_edit_text);
	urlEditText = (EditText) findViewById(R.id.url_cms_edit_text);
	ipv4EditText = (EditText) findViewById(R.id.ipv4_edit_text);
	ipv6EditText = (EditText) findViewById(R.id.ipv6_edit_text);
	
	title = (TextView) findViewById(R.id.textView2);
	
	Intent iin = getIntent();
	Bundle b = iin.getExtras();
	try {
	    beacon = (Beacon) b.getSerializable("beacon");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	myGPSButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		mlocListener = new MyLocationListener();

		mlocManager.requestLocationUpdates(
			LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

	    }
	});
	
	createButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if(beacon != null)
		    updateBeacon(beacon);
		else
		    createBeacon();

	    }
	});
	
	if(beacon != null){
	    title.setText("Beacon");
	    titleEditText.setText(beacon.getTitle());
	    gpsPositionEditText.setText(beacon.getGps());
	    distanceEditText.setText(Integer.toString(beacon.getDistance()));
	    emailEditText.setText(beacon.getEmail());
	    smsEditText.setText(beacon.getSms());
	    urlEditText.setText(beacon.getUrlCMS());
	    ipv4EditText.setText(beacon.getIPv4CMS());
	    ipv6EditText.setText(beacon.getIPv6CMS());
	    createButton.setText("Update");
	}
    }
    
    public void createBeacon(){
	String result = "";
	Beacon b = new Beacon();
	b.setId("0");
	b.setTitle(titleEditText.getText().toString());
	b.setGps(gpsPositionEditText.getText().toString());
	b.setDistance(Integer.parseInt(distanceEditText.getText().toString()));
	b.setEmail(emailEditText.getText().toString());
	b.setSms(smsEditText.getText().toString());
	b.setUrlCMS(urlEditText.getText().toString());
	b.setIPv4CMS(ipv4EditText.getText().toString());
	b.setIPv6CMS(ipv6EditText.getText().toString());
	
	CreateBeaconTask crb = new CreateBeaconTask();
	prefs = PreferenceManager
		.getDefaultSharedPreferences(AddBeaconActivity.this);

	String user = prefs.getString("user", "");
	TelephonyManager telephonyManager =
		(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
	
	try {
	    //crb.setJson(b.toJson());
	    crb.setJson(b.toJsonFoRCMS(user,imei));
	    result = crb.execute("").get();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	if (!result.equals(null) && !result.equals("")){
	    b.setId(result);
	    db.addBeacon(b);
	    finish();
	}
	
	
    }
    
    public void updateBeacon(Beacon beacon){
	String result = "";
	Beacon b = new Beacon();
	b.setId(beacon.getId());
	b.setTitle(titleEditText.getText().toString());
	b.setGps(gpsPositionEditText.getText().toString());
	b.setDistance(Integer.parseInt(distanceEditText.getText().toString()));
	b.setEmail(emailEditText.getText().toString());
	b.setSms(smsEditText.getText().toString());
	b.setUrlCMS(urlEditText.getText().toString());
	b.setIPv4CMS(ipv4EditText.getText().toString());
	b.setIPv6CMS(ipv6EditText.getText().toString());
	
	UpdateBeaconTask upb = new UpdateBeaconTask();
	prefs = PreferenceManager
		.getDefaultSharedPreferences(AddBeaconActivity.this);

	String user = prefs.getString("user", "");
	TelephonyManager telephonyManager =
		(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
	try {
	    upb.setJson(b.toJson());
	    upb.setJson(b.toJsonFoRCMS(user,imei));
	    result = upb.execute("").get();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	if (!result.equals(null) && !result.equals("")){
	    db.updateBeacon(b);
	}
    }
    
    public  void onDestroy()
    {
        super.onDestroy();
        if(mlocManager != null)
            mlocManager.removeUpdates(mlocListener);
    }

    public class MyLocationListener implements LocationListener {
	boolean first = true;

	@Override
	public void onLocationChanged(Location loc) {
	    loc.getLatitude();
	    loc.getLongitude();
	    String text = "My current location is: " + "Latitud = " + loc.getLatitude() + "Longitud = " + loc.getLongitude();
	    if(first){
		first = false;
		Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
		try {
		    gpsPositionEditText.setText(loc.getLatitude() + ";" + loc.getLongitude());
		    mlocManager.removeUpdates(mlocListener);
		} catch (Exception e) {
		    // TODO: handle exception
		}
		
	    }
	}

	@Override
	public void onProviderDisabled(String provider) {
	    buildAlertMessageNoGps();
	}

	@Override
	public void onProviderEnabled(String provider) {
	    Toast.makeText(getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){

	}
	
	private void buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AddBeaconActivity.this);
		alertDialogBuilder
				.setMessage("If you wish to turn on the GPS go to settings!")
				.setCancelable(false)
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

    }
    
    private class CreateBeaconTask extends AsyncTask<String, Void, String> {
	JSONObject json;
	ProgressDialog progress;
	
	@Override
	    protected void onPreExecute() {
		super.onPreExecute();
		progress = ProgressDialog.show(AddBeaconActivity.this, "Sending data",
			"Registring Beacon...");
	    }
	
	@Override
	protected String doInBackground(String... urls) {
	    System.out.println(urls[0]);
	    String url = ipAddress + ":5683/beacon";
	    if(!url.contains("coap"))
		url = "coap://" + url;
	    Request req = new POSTRequest();
 		req.setURI(url);
 		//req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
 		System.out.println(json.toString());
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
 		String response = "";
		try {
			res = req.receiveResponse();
			System.out.println(res.getCode());
			response = res.getPayloadString();
			//if(res.getCode() == 68 || res.getCode() == 69
				// || res.getCode() == 65 || res.getCode() == 67)
			    //response = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
        return response;
	}

	public void setJson(JSONObject json) {
	    this.json = json;
	    
	}

	@Override
	protected void onPostExecute(String result) {
	    progress.dismiss();
		System.out.println("usaoooooooooooooooooooooooooooooo");
		if (!result.equals(null) && !result.equals("")){
		    Toast.makeText(getApplicationContext(),"Beacon created",Toast.LENGTH_SHORT).show();
		}
		else
		    Toast.makeText(getApplicationContext(),"Beacon not created. Please try again!",Toast.LENGTH_SHORT).show();

	}
    }
    
    private class UpdateBeaconTask extends AsyncTask<String, Void, String> {
	JSONObject json;
	ProgressDialog progress;
	
	@Override
	    protected void onPreExecute() {
		super.onPreExecute();
		progress = ProgressDialog.show(AddBeaconActivity.this, "Sending data",
			"Updating Beacon...");
	    }
	
	@Override
	protected String doInBackground(String... urls) {
	    System.out.println(urls[0]);
	    String url = ipAddress + ":5683/beacon";
	    if(!url.contains("coap"))
		url = "coap://" + url;
	    Request req = new PUTRequest();
 		req.setURI(url);
 		//req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
 		req.setPayload(json.toString());
 		System.out.println(json.toString());
 		req.enableResponseQueue(true);
 		
 		try {
 			req.execute();
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			return "";
 		}
 		Response res = null;
 		String response = "";
		try {
			res = req.receiveResponse();
			System.out.println(res.getCode());
			response = res.getPayloadString();
			if(res.getCode() == 68 || res.getCode() == 69
				 || res.getCode() == 65 || res.getCode() == 67)
			    response = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
        return response;
	}
	
	public void setJson(JSONObject json) {
	    this.json = json;
	    
	}

	@Override
	protected void onPostExecute(String result) {
	    progress.dismiss();
		System.out.println("usaoooooooooooooooooooooooooooooo");
		if (!result.equals(null) && !result.equals("")){
		    Toast.makeText(getApplicationContext(),"Beacon updated",Toast.LENGTH_SHORT).show();
		}
		else
		    Toast.makeText(getApplicationContext(),"Beacon not updated. Please try again!",Toast.LENGTH_SHORT).show();

	}
    }

}
