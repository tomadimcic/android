package com.iotwear.wear.gui;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import com.iotwear.wear.R;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.GetDeviceListFromMaster;
import com.iotwear.wear.util.DataManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    protected int splashTime = 100;
    boolean prvoPokretanje;
    boolean isFinished;
    AlertDialog.Builder builder;
    String ipAddress;
    String user;
    String pass;
    int rememberMe;
    int signed;
    private SharedPreferences prefs;
    EditText username;
    EditText password;
    Button login;
    TextView skip;
    TextView scan;
    CheckBox remember;
    CheckBox staySigned;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);

	username = (EditText) findViewById(R.id.username_edit_text);
	password = (EditText) findViewById(R.id.password_edit_text);
	login = (Button) findViewById(R.id.login_button);
	skip = (TextView) findViewById(R.id.skip_button);
	scan = (TextView) findViewById(R.id.scan_button);
	remember = (CheckBox) findViewById(R.id.checkBox1);
	staySigned = (CheckBox) findViewById(R.id.checkBox2);
	
	prefs = PreferenceManager
		.getDefaultSharedPreferences(LoginActivity.this);

	user = prefs.getString("user", "");
	pass = prefs.getString("pass", "");
	rememberMe = prefs.getInt("remember", 10);
	signed = prefs.getInt("signed", 10);
	
	if(rememberMe == 0)
	    remember.setChecked(false);
	if(signed == 0)
	    staySigned.setChecked(false);
	
	if(rememberMe == 1){
	    if(signed == 1 && !user.equals("") && !pass.equals(""))
		login();
	    else{
		if (!user.equals(""))
		    username.setText(user);
		if (!pass.equals(""))
		    password.setText(pass);
		
	    }
	}

	

	login.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){
		    rememberMe = 0;
		    signed = 0;
		    if(remember.isChecked())	
		    	rememberMe = 1;
		    if(staySigned.isChecked())	
			signed = 1;
		    if(rememberMe == 1){
			prefs.edit().putString("user", username.getText().toString()).commit();
			prefs.edit().putString("pass", password.getText().toString()).commit();
			prefs.edit().putInt("remember", rememberMe).commit();
			prefs.edit().putInt("signed", signed).commit();
		    }else{
			prefs.edit().putString("user", username.getText().toString()).commit();
			prefs.edit().putString("pass", password.getText().toString()).commit();
			prefs.edit().putInt("remember", 0).commit();
			prefs.edit().putInt("signed", 0).commit();
		    }
		    login();
		}else
		    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
		
	    }
	});
	
	skip.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		prefs.edit().putString("user", "").commit();
		prefs.edit().putString("pass", "").commit();
		prefs.edit().putInt("remember", 0).commit();
		prefs.edit().putInt("signed", 0).commit();
		login();
		
	    }
	});
	
	scan.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		startNewActivity("com.google.zxing.client.android");
		
	    }
	});
	

    }
    
    public void login(){
	Intent i = new Intent(LoginActivity.this, DeviceListActivity.class);
	startActivity(i);
	finish();
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
	if (appInfo != null) {
	    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    startActivityForResult(intent, 0);
	} else {
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse("market://details?id=" + packageName));
	    startActivity(intent);
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
		if(contents.contains("&")){
		    if(contents.split("&")[0].contains("id")){
			String id = contents.split("&")[0].split("=")[1];
			if(id.equals("1")){
			    //id=1&user=user1&pass=password121
			    startActivity(contents);
			}
			if(id.equals("2")){
			    //id=2&user=user1&pass=password121&[{"p":61616,"s":"7c3b7e","c":[{"t":0,"id":0,"n":"led"},{"t":1,"id":0,"n":"Switch1"},{"t":5,"id":"1-4","n":"Roletna"},{"t":4,"id":5,"n":"Taster1"},{"t":4,"id":6,"n":"Taster2"},{"t":1,"id":7,"n":"Switch2"},{"t":2,"id":0,"n":"Dimmer1"},{"t":2,"id":1,"n":"Dimmer2"},{"t":2,"id":2,"n":"Dimmer3"}],"url":"controller16.no-ip.biz:61616","ip":"192.168.0.55","n":"prezentacija"},{"p":61617,"s":"7c3b7e","c":[{"t":0,"id":0,"n":"LED"}],"url":"controller3.no-ip.biz:61617","ip":"192.168.0.56","n":"led"}]
			    Boolean isOk = processList(contents.split("&")[3]);
			    if(isOk){
				startActivity(contents);
			    }
			    
			}
			if(id.equals("3")){
			    try {
				//id=3&user=user1&pass=password121
				prefs.edit().putString("user", contents.split("&")[1].split("=")[1]).commit();
				prefs.edit().putString("pass", contents.split("&")[2].split("=")[1]).commit();
				Boolean isOk = new GetDeviceListFromMaster(this, "www.iot-wear.com").execute().get();
				if(isOk){
					startActivity(contents);
				    }
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    } catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}
		    }
		}else
		    Toast.makeText(getApplicationContext(), "Bad QR code",
				Toast.LENGTH_SHORT).show();


		// Handle successful scan

	    } else if (resultCode == RESULT_CANCELED) {
		// Handle cancel
		Log.i("App", "Scan unsuccessful");
	    }
	}
    }
    
    public Boolean processList(String response){
	boolean alreadyIn;
	PiDevice pi;
	JSONArray ja;
	try {
	    ja = new JSONArray(response);
	
	ArrayList<PiDevice> deviceList = DataManager.getInstance()
		.getDeviceList();
	for (int i = 0; i < ja.length(); i++) {
	    pi = PiDevice.fromJson(ja.getJSONObject(i));
	    System.out.println(pi.getLocalIp());
	    alreadyIn = false;
	    for (int j = 0; j < deviceList.size(); j++) {
		if (pi.getLocalIp().equals(
			deviceList.get(j).getLocalIp())
			&& pi.getSsid().equals(
				deviceList.get(j).getSsid())) {
		    alreadyIn = true;
		}
	    }
	    if (!alreadyIn)
		DataManager.getInstance().addDevice(pi);
	}
	return true;
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    Toast.makeText(getApplicationContext(), "Bad QR code",
			Toast.LENGTH_SHORT).show();
	    return false;
	}
    }
    
    public void startActivity(String contents){
	prefs.edit().putString("user", contents.split("&")[1].split("=")[1]).commit();
	prefs.edit().putString("pass", contents.split("&")[2].split("=")[1]).commit();
	prefs.edit().putInt("remember", 1).commit();
	prefs.edit().putInt("signed", 1).commit();
	login();
    }
}
