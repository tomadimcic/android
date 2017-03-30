package com.iotwear.wear.gui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.ControllerGridActivity.CustomizeDialogGroup1;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.dialog.CustomizeDialogGetDeviceList;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.GetDeviceListFromMaster;
import com.iotwear.wear.task.SendDataToPi;
import com.iotwear.wear.task.SendDeviceListToMaster;
import com.iotwear.wear.util.NetworkUtil;

public class SettingsActivity extends BaseActivity implements OnClickListener {

    RelativeLayout addNewDevice, scanNetwork, sendDeviceList, setControl;
    public ProgressDialog progress;
    ArrayList<PiDevice> deviceList;
	LayoutInflater inflater;
	View view;
	TextView textview;
	EditText ipAddressDialog;
	private SharedPreferences prefs;
	String user;
	String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	View content = LayoutInflater.from(this).inflate(
		R.layout.activity_settings, null);
	contentFrame.addView(content);
	prefs = PreferenceManager
		.getDefaultSharedPreferences(SettingsActivity.this);

	user = prefs.getString("user", "");
	pass = prefs.getString("pass", "");

	addNewDevice = (RelativeLayout) findViewById(R.id.addNewDevice);
	scanNetwork = (RelativeLayout) findViewById(R.id.scanNetwork);
	sendDeviceList = (RelativeLayout) findViewById(R.id.sendDeviceList);
	setControl = (RelativeLayout) findViewById(R.id.setControl);

	addNewDevice.setOnClickListener(this);
	scanNetwork.setOnClickListener(this);
	sendDeviceList.setOnClickListener(this);
	setControl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.addNewDevice:
	    if(user.equals("admin") && pass.equals("admin")){
		Intent i = new Intent(SettingsActivity.this,
        		    AddDeviceActivity.class);
        	startActivity(i);
	    this.finish();
	    }else {
		    Toast.makeText(
			    this,
			    "You don't have a permission to access here.",
			    Toast.LENGTH_SHORT).show();
		}
	    break;

	case R.id.scanNetwork:
	    if (NetworkUtil.isWiFiEnabled(this)) {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
			.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
		    //getDeviceListFromMaster(PiDevice.MASTER_IP_ADDRESS);
		    Intent intent = new Intent(SettingsActivity.this,
			    ScanNetworkActivity.class);
		    startActivity(intent);
		    this.finish();
		    
		} else {
		    Toast.makeText(
			    this,
			    "You're not connected to any wireless network. Please connect and try again.",
			    Toast.LENGTH_SHORT).show();
		}
	    } else {
		Toast.makeText(
			this,
			"Wireless is disabled. Please enable it and connect to wireless network.",
			Toast.LENGTH_SHORT).show();
	    }
	    break;

	case R.id.sendDeviceList:
	    if (NetworkUtil.isWiFiEnabled(this)) {
		String ssid = NetworkUtil.getCurrentSsid(this);

		if (ssid != null) {
		    sendDeviceListToMaster(PiDevice.MASTER_IP_ADDRESS);
		    //new SendDeviceListToMaster(this).execute();
		} else {
		    Toast.makeText(
			    this,
			    "You're not connected to any wireless network. Please connect and try again.",
			    Toast.LENGTH_SHORT).show();
		}
	    } else {
		Toast.makeText(
			this,
			"Wireless is disabled. Please enable it and connect to wireless network.",
			Toast.LENGTH_SHORT).show();
	    }
	    break;
	    
	case R.id.setControl:
	    Intent i1 = new Intent(SettingsActivity.this,
		    ShowDevicesInfoActivity.class);
	    startActivity(i1);
	    this.finish();
	    break;
	}
    }

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this, "Settings",
		Mode.SLIDING);
    }
    
    public void getDeviceListFromMaster(String master){
	
	CustomizeDialogGetDeviceList customizeDialog = new CustomizeDialogGetDeviceList(this, master, true);  
        customizeDialog.setTitle("Enter IP address");   
        customizeDialog.show();
	    
	    
	}
    
    public void sendDeviceListToMaster(String master){
	
	CustomizeDialogGetDeviceList customizeDialog = new CustomizeDialogGetDeviceList(this, master, false);  
        customizeDialog.setTitle("Enter IP address");   
        customizeDialog.show();
	    
	    
	}
}
