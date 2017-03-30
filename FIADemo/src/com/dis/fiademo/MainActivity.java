package com.dis.fiademo;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.dis.fiademo.AddBeaconActivity.MyLocationListener;
import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.dialog.CustomizeDialog;
import com.dis.fiademo.dialog.CustomizeDialogInfo;
import com.dis.fiademo.model.Beacon;
import com.dis.fiademo.services.LocationService;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button nfcButton;
    Button qrButton;
    Button beacons;
    Button addBeacon;
    Button settingsButton;
    LocationManager mlocManager;
    LocationListener mlocListener;
    int checkSeconds;
    private SharedPreferences prefs;
    int isChecked;
    boolean first = true;
    TextToSpeech ttobj;
    String ipAddress;
    String fourorsix;
    DatabaseHandler db;
    Location l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	// startService(new Intent(this, LocationService.class));
	l = new Location(LocationManager.NETWORK_PROVIDER);
	db = new DatabaseHandler(this);
	prefs = PreferenceManager
		.getDefaultSharedPreferences(MainActivity.this);

	fourorsix = prefs.getString("ip", "");

	if (fourorsix.equals("4"))
	    ipAddress = db.getIpv4Address();
	else
	    ipAddress = "[" + db.getIpv6Address() + "]";

	nfcButton = (Button) findViewById(R.id.nfc_button);
	qrButton = (Button) findViewById(R.id.qr_button);
	beacons = (Button) findViewById(R.id.beacons_button);
	addBeacon = (Button) findViewById(R.id.add_beacon_button);
	settingsButton = (Button) findViewById(R.id.settings_button);

	prefs = PreferenceManager
		.getDefaultSharedPreferences(MainActivity.this);
	checkSeconds = prefs.getInt("sec", 10);
	checkSeconds = checkSeconds * 1000;
	isChecked = prefs.getInt("checked", 1);
	if (mlocManager == null) {
	    mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	    mlocListener = new MyLocationListener();
	}

	if (isChecked == 1) {

	    // mlocManager = (LocationManager)
	    // getSystemService(Context.LOCATION_SERVICE);

	    // mlocListener = new MyLocationListener();

	    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		    0, mlocListener);
	}

	nfcButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MainActivity.this, NFCIoT6Activity.class);
		// i.putExtra("location", l.getLatitude() + ";" + l
		// .getLongitude());
		startActivity(i);

	    }
	});

	qrButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// Intent i = new Intent(MainActivity.this,
		// QRCodeActivity.class);
		// startActivity(i);
		startNewActivity("com.google.zxing.client.android");
		// Intent intent = new
		// Intent("com.google.zxing.client.android.SCAN");
		// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		// startActivityForResult(intent, 0);
		/*
		 * IntentIntegrator integrator = new IntentIntegrator(
		 * MainActivity.this); integrator.initiateScan();
		 */

	    }
	});

	beacons.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MainActivity.this,
			BeaconsListActivity.class);
		startActivity(i);

	    }
	});

	addBeacon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MainActivity.this,
			AddBeaconActivity.class);
		startActivity(i);

	    }
	});

	settingsButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(i);

	    }
	});

	initTextToSpech();
    }

    public void startNewActivity(String packageName) {
	PackageManager pm = getPackageManager();
	ApplicationInfo appInfo = null;
	try {
	    appInfo = pm.getApplicationInfo("com.google.zxing.client.android",
		    0);
	} catch (NameNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	/*
	 * Intent intent = null; try { intent =
	 * this.getPackageManager().getLaunchIntentForPackage(packageName); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 */
	if (appInfo != null) {
	    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    // we found the activity
	    // now start the activity
	    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivityForResult(intent, 0);
	} else {
	    // bring user to the market
	    // or let them choose an app?
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setData(Uri.parse("market://details?id=" + packageName));
	    startActivity(intent);
	}
    }

    private void initTextToSpech() {
	ttobj = new TextToSpeech(getApplicationContext(),
		new TextToSpeech.OnInitListener() {
		    @Override
		    public void onInit(int status) {
			if (status != TextToSpeech.ERROR) {
			    ttobj.setLanguage(Locale.UK);
			}
		    }
		});

    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	checkSeconds = prefs.getInt("sec", 10);
	checkSeconds = checkSeconds * 1000;
	isChecked = prefs.getInt("checked", 1);
	if (mlocManager == null) {
	    mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	    mlocListener = new MyLocationListener();
	}

	if (isChecked == 1) {

	    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		    0, mlocListener);
	} else {
	    mlocManager.removeUpdates(mlocListener);
	}
    }

    public void onDestroy() {
	super.onDestroy();
	if (mlocManager != null)
	    mlocManager.removeUpdates(mlocListener);
	ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
	beaconList = db.getAllBeacons();
	for (Beacon beacon : beaconList) {
	    beacon.setSent("");
	    db.updateBeacon(beacon);
	}
    }

    public String getLocation() {
	return l.getLatitude() + ";" + l.getLongitude();
    }

    public class MyLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location loc) {
	    loc.getLatitude();
	    loc.getLongitude();
	    l = loc;
	    // String text = "My current location is: " + "Latitud = " +
	    // loc.getLatitude() + "Longitud = " + loc.getLongitude();
	    if (first) {
		first = false;
		// Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
		try {
		    new SendInfo().execute(Double.toString(loc.getLatitude()),
			    Double.toString(loc.getLongitude()), "0");
		    checkBeaconsAroundMe(loc);
		    // mlocManager.removeUpdates(mlocListener);
		} catch (Exception e) {
		    // TODO: handle exception
		}

	    }
	}

	private void checkBeaconsAroundMe(Location curLocation) {
	    DatabaseHandler db = new DatabaseHandler(MainActivity.this);
	    ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
	    beaconList = db.getAllBeacons();
	    db.close();
	    for (Beacon b : beaconList) {
		try {
		    double lat = Double.parseDouble(b.getGps().split(";")[0]);
		    double lng = Double.parseDouble(b.getGps().split(";")[1]);
		    Location kLoc = new Location(curLocation);
		    kLoc.setLatitude(lat);
		    kLoc.setLongitude(lng);
		    if (b.getActivated().equals("1")) {
			if (kLoc.distanceTo(curLocation) < b.getDistance()) {
			    if (!b.getSent().equals("in")) {
				b.setSent("in");
				db.updateBeacon(b);
				new SendInfo().execute(b.getId(), "in", "1");
				ttobj.speak(
					"In the area of the " + b.getTitle(),
					TextToSpeech.QUEUE_FLUSH, null);
				Toast.makeText(
					MainActivity.this,
					"In the area\n"
						+ kLoc.distanceTo(curLocation)
						+ " to the " + b.getTitle(),
					Toast.LENGTH_SHORT).show();
			    }
			} else {
			    if (!b.getSent().equals("out")) {
				b.setSent("out");
				db.updateBeacon(b);
				new SendInfo().execute(b.getId(), "out", "1");
				ttobj.speak(
					"Out the area of the " + b.getTitle(),
					TextToSpeech.QUEUE_FLUSH, null);
				Toast.makeText(
					MainActivity.this,
					"Out of the area\n"
						+ kLoc.distanceTo(curLocation)
						+ " to the " + b.getTitle(),
					Toast.LENGTH_SHORT).show();
			    }
			}
		    }

		} catch (NumberFormatException e) {

		}

	    }

	    Thread locThread;

	    locThread = new Thread() {
		@Override
		public void run() {
		    try {
			synchronized (this) {
			    wait(checkSeconds);
			}

		    } catch (InterruptedException e) {
		    } finally {
			first = true;
		    }
		}

	    };

	    locThread.start();

	    /*
	     * try { int j = 10; while (j > 0) { Thread.sleep(1000); j--; if(j
	     * == 1) first = true;
	     * 
	     * }
	     * 
	     * } catch (InterruptedException e) { // TODO Auto-generated catch
	     * block e.printStackTrace(); }
	     */
	}

	@Override
	public void onProviderDisabled(String provider) {
	    buildAlertMessageNoGps();
	}

	@Override
	public void onProviderEnabled(String provider) {
	    Toast.makeText(getApplicationContext(), "Gps Enabled",
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	private void buildAlertMessageNoGps() {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		    MainActivity.this);
	    alertDialogBuilder
		    .setMessage(
			    "If you wish to turn on the GPS go to settings!")
		    .setCancelable(false)
		    .setPositiveButton("Settings",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    private class SendInfo extends AsyncTask<String, Void, String> {
	// ProgressDialog progress;

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    // progress = ProgressDialog.show(MainActivity.this, "Sending data",
	    // "Registring Beacon...");
	}

	@Override
	protected String doInBackground(String... urls) {
	    System.out.println(urls[0]);
	    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    String imei = telephonyManager.getDeviceId();
	    String url = ipAddress + ":5683/beacon";
	    if (!url.contains("coap"))
		url = "coap://" + url;
	    Request req = new POSTRequest();
	    req.setURI(url);
	    // req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
	    JSONObject json = new JSONObject();
	    try {
		json.put("imei", imei);
		if (urls[2].equals("0")) {
		    json.put("lat", urls[0]);
		    json.put("lng", urls[1]);
		} else {
		    json.put("id", urls[0]);
		    json.put("status", urls[1]);
		}
	    } catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	    req.setPayload(json.toString());
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
		if (res.getCode() == 68 || res.getCode() == 69
			|| res.getCode() == 65 || res.getCode() == 67)
		    response = "success";
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    }
	    return response;
	}

	@Override
	protected void onPostExecute(String result) {
	    // progress.dismiss();
	    System.out.println("usaoooooooooooooooooooooooooooooo");
	    if (!result.equals(null) && !result.equals("")) {
		Toast.makeText(getApplicationContext(), "Info posted",
			Toast.LENGTH_SHORT).show();
	    } else
		Toast.makeText(getApplicationContext(), "Info not posted",
			Toast.LENGTH_SHORT).show();

	}
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	if (requestCode == 0) {
	    if (resultCode == RESULT_OK) {

		String contents = intent.getStringExtra("SCAN_RESULT");
		String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		System.out.println("Usaoooooooooooooooo");
		System.out.println(contents);
		Toast.makeText(getApplicationContext(), contents,
			Toast.LENGTH_SHORT).show();

		new SendQRDataTask().execute(contents);

		// Handle successful scan

	    } else if (resultCode == RESULT_CANCELED) {
		// Handle cancel
		Log.i("App", "Scan unsuccessful");
	    }
	}
    }

    String code = "";

    private class SendQRDataTask extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... urls) {
	    System.out.println(urls[0]);
	    code = urls[0];
	    String url = ipAddress + ":5683/qrcode";
	    if (!url.contains("coap"))
		url = "coap://" + url;
	    Request req = new POSTRequest();
	    req.setURI(url);
	    // req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
	    // req.setPayload(urls[0].substring(3));
	    System.out.println(urls[0]);
	    req.setPayload(urls[0]);
	    // req.setPayload("354909050056732");
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
		// response = res.getPayloadString();
		if (res.getCode() == 68 || res.getCode() == 69
			|| res.getCode() == 65 || res.getCode() == 67)
		    response = res.getPayloadString();
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    }
	    return response;
	}

	@Override
	protected void onPostExecute(String result) {
	    System.out.println("usaoooooooooooooooooooooooooooooo");
	    String pomjson = "{\"dname\":\"\",\"dprotocol\":\"\",\"address\":\"www.iot-wear.com\",\"ipv6addr\":\"\",\"vname\":\"VariableName\",\"vvalue\":0,\"vunit\":\"NoUnit\"}";
	    if (!result.equals(null) && !result.equals("")
		    && !result.contains("Bad") && !result.contains("URL")) {
		CustomizeDialogInfo customizeDialog = new CustomizeDialogInfo(
			MainActivity.this, result);
		customizeDialog.setTitle("Info about the device");
		customizeDialog.show();
	    } else {
		// Toast.makeText(getApplicationContext(),"Code not valide, please try again!",Toast.LENGTH_SHORT).show();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		String user = prefs.getString("user", "");
		System.out.println(l.getLatitude() + ";" + l.getLongitude());
		CustomizeDialog customizeDialog = new CustomizeDialog(
			MainActivity.this, imei, ipAddress, user,
			l.getLatitude() + ";" + l.getLongitude(), code, 1);
		customizeDialog.setTitle("Code does not exist");
		customizeDialog
			.setMessage("Would you like to regiter this code to the CMS?");
		customizeDialog.show();
	    }
	    // System.gc();
	}
    }

}
