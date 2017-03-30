package com.iotwear.wear.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.dialog.CustomizeDialogAddDevice;
import com.iotwear.wear.gui.dialog.CustomizeDialogAddDevice1;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.WiFiNetwork;
import com.iotwear.wear.task.SearchControllersTask;
import com.iotwear.wear.util.DataManager;
import com.iotwear.wear.util.NetworkUtil;

public class AddDeviceActivity extends BaseActivity {

	private TextView status;
	private Spinner spinner;
	private Spinner deviceSpinner;
	private EditText password;
	private Button submit;
	private Button search;
	private String currentSsid;
	LayoutInflater inflater;
	View view;
	TextView textview;
	EditText ipAddressDialog;
	String currentIpAddress;
	WiFiNetwork selectedNetwork;
	PiDevice newPi;
	int sameNetworkDevices;
	String masterIpAddress;
	boolean first = true;
	//RadioButton basic;
	//RadioButton advanced;
	TextView tv1;
	TextView tv2;
	EditText portFrom;
	EditText portTo;
	//RadioGroup group;
	ArrayList<String> controllerList;
	String ssid;
	RelativeLayout searchMultiple;
	

	private ArrayList<WiFiNetwork> networkList;

	private WiFiReceiver wifiReceiver;

	ArrayList<PiDevice> deviceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View content = LayoutInflater.from(this).inflate(
				R.layout.activity_main, null);
		contentFrame.addView(content);

		networkList = new ArrayList<WiFiNetwork>();

		status = (TextView) findViewById(R.id.status);
		submit = (Button) findViewById(R.id.submitBtn);
		search = (Button) findViewById(R.id.searchBtn);
		spinner = (Spinner) findViewById(R.id.networkSpinner);
		deviceSpinner = (Spinner) findViewById(R.id.deviceSpinner);
		password = (EditText) findViewById(R.id.password);
		//basic = (RadioButton) findViewById(R.id.radio0);
		//advanced = (RadioButton) findViewById(R.id.radio1);
		tv1 = (TextView) findViewById(R.id.from_txt);
		tv2 = (TextView) findViewById(R.id.to_txt);
		portFrom = (EditText) findViewById(R.id.from);
		portTo = (EditText) findViewById(R.id.to);
		searchMultiple = (RelativeLayout) findViewById(R.id.searchMultiple1);

		currentSsid = NetworkUtil.getCurrentSsid(getApplicationContext());

		deviceList = DataManager.getInstance().getDeviceList();
		controllerList = new ArrayList<String>();

		if (deviceList.size() > 0) {
			for (PiDevice pi : deviceList) {
				if (pi.getSsid().equals(currentSsid)) {
					break;
				}
			}
		}

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedNetwork = networkList.get(spinner
						.getSelectedItemPosition());
				
				//PiDevice newPi = new PiDevice();
				//newPi.setSsid(spinner.getSelectedItem().toString());
				
            				deviceList = DataManager.getInstance().getDeviceList();
            				if (deviceList.size() == 0) {
            					//newPi.setLocalIp(PiDevice.MASTER_IP_ADDRESS);
            					//newPi.setPort(PiDevice.MASTER_PORT_NUMBER);
            				if (!ssid.equals(PiDevice.DEVICE_SSID))
            				    showIpAddressDialog(PiDevice.MASTER_IP_ADDRESS, true);
            				else
            				    showIpAddressDialog(PiDevice.MASTER_IP_ADDRESS, false);
            				} else {
            					sameNetworkDevices = 0;
            
            					PiDevice.setMasterIpAddress(PiDevice.MASTER_IP_ADDRESS);
            					for (PiDevice pi : deviceList) {
            						if (pi.getSsid().equals(selectedNetwork.ssid)){
            						    if(first){
            							
            							masterIpAddress = pi.getLocalIp();
            							PiDevice.setMasterIpAddress(masterIpAddress);
            							first = false;
            						    }
            							
            						    sameNetworkDevices++;
            						}
            					}
            					
            					if (!ssid.equals(PiDevice.DEVICE_SSID))
            					    showIpAddressDialog(PiDevice.getIpAddressForDevice(sameNetworkDevices), true);
            					else
            					    showIpAddressDialog(PiDevice.getIpAddressForDevice(sameNetworkDevices), false);
            					//newPi.setPort(PiDevice.MASTER_PORT_NUMBER
            					//		+ sameNetworkDevices);
            					//newPi.setLocalIp(PiDevice
            					//		.getIpAddressForDevice(sameNetworkDevices));
            				}
				//new SendDataToPi(AddDeviceActivity.this, newPi, password
				//		.getText().toString(), selectedNetwork).execute();
			}
		});
		
		search.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			getAvailableControllers(false);
			
		    }
		});
		
		/*group = (RadioGroup) findViewById(R.id.radioGroup1);
		deviceSpinner.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                portFrom.setVisibility(View.GONE);
                portTo.setVisibility(View.GONE);

	        group.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	           {

	            public void onCheckedChanged(RadioGroup group, int checkedId) 
	               {
	                // TODO Auto-generated method stub
	                if(basic.isChecked())
	                  {

	                            deviceSpinner.setVisibility(View.GONE);
	                            search.setVisibility(View.GONE);
	                            tv1.setVisibility(View.GONE);
	                            tv2.setVisibility(View.GONE);
	                            portFrom.setVisibility(View.GONE);
	                            portTo.setVisibility(View.GONE);
	                  }
	                else if(advanced.isChecked())
	                  {
	                    deviceSpinner.setVisibility(View.VISIBLE);
                            search.setVisibility(View.VISIBLE);
                            tv1.setVisibility(View.VISIBLE);
                            tv2.setVisibility(View.VISIBLE);
                            portFrom.setVisibility(View.VISIBLE);
                            portTo.setVisibility(View.VISIBLE);

	                  }
	             }
	        });*/

	}

	public void showAnotherDialog() {
		AlertDialog.Builder builder = new Builder(AddDeviceActivity.this);
		builder.setTitle("Devices")
				.setMessage(
						"Are you sure that you do not have any other device to add?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Intent i = new Intent(AddDeviceActivity.this,
										DeviceListActivity.class);
								startActivity(i);
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).show();
	}
	
	public void showIpAddressDialog(String master, boolean isDHCP){
	    
	    newPi = new PiDevice();
	    newPi.setSsid(spinner.getSelectedItem().toString());
	    CustomizeDialogAddDevice customizeDialog;
	    CustomizeDialogAddDevice1 customizeDialog1;
            if(!isDHCP){
        	customizeDialog = new CustomizeDialogAddDevice(AddDeviceActivity.this, master, newPi, sameNetworkDevices, password, selectedNetwork, PiDevice.DEVICE_AP_IP_ADDRESS + ":" + PiDevice.MASTER_PORT_NUMBER);  
                customizeDialog.setTitle("Enter IP address");
        	customizeDialog.setMessage("Please set IP address for the device. The proposed address is:"); 
        	customizeDialog.show();
            }
            else{
        	if(controllerList.size() > 0){
        	    customizeDialog1 = new CustomizeDialogAddDevice1(AddDeviceActivity.this, master, newPi, sameNetworkDevices, password, selectedNetwork, controllerList.get(deviceSpinner.getSelectedItemPosition()));  
        	    customizeDialog1.setTitle("Enter IP address");
                
        	    customizeDialog1.setMessage("Current IP address and port of the device is:\n",
        			"\nPlease set IP address for the device. The proposed address is:");
        	}
                else{
                    customizeDialog1 = new CustomizeDialogAddDevice1(AddDeviceActivity.this, master, newPi, sameNetworkDevices, password, selectedNetwork, PiDevice.MASTER_IP_ADDRESS + ":" + PiDevice.MASTER_PORT_NUMBER); 
                    customizeDialog1.setMessage("Current IP address and port of the device is:\n",  
    			"\nPlease set IP address for the device. The proposed address is:");
                }
        	customizeDialog1.show();
            }
            
	    
	    
	}

	@Override
	protected void onResume() {
		super.onResume();
		initWiFiStatusReceiver();
		checkWifiStatus();
		//checkCurrentSSID();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(wifiReceiver);
	}
	

	private void initWiFiStatusReceiver() {
		wifiReceiver = new WiFiReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		registerReceiver(wifiReceiver, intentFilter);
	}

	private void checkWifiStatus() {
		if (NetworkUtil.isWiFiEnabled(this))
		    checkCurrentSSID();
			//getAvailableWiFiNetworks();
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"You need to enable wifi before you can add controller.")
					.setPositiveButton("Go to settings",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(
											Settings.ACTION_WIFI_SETTINGS));
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});
			builder.show();
		}
	}
	
	private void checkCurrentSSID() {
	    ssid = NetworkUtil.getCurrentSsid(this);
		if (ssid.equals(PiDevice.DEVICE_SSID)){
			//getAvailableWiFiNetworks();
		    //search.setClickable(false);
		    //getAvailableControllers(true);
		    /*deviceSpinner.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    tv1.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    portFrom.setVisibility(View.GONE);
                    portTo.setVisibility(View.GONE);*/
                    searchMultiple.setVisibility(View.GONE);
		}
		/*else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"You need to be connected to the IF network!")
					.setPositiveButton("Go to settings",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(
											Settings.ACTION_WIFI_SETTINGS));
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});
			builder.show();
		}*/
		getAvailableWiFiNetworks();
	}

	public void connectToPi() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration wc = new WifiConfiguration();
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wc.SSID = "\"pi\"";

		//WifiManager wifiManag = (WifiManager) this
		//		.getSystemService(WIFI_SERVICE);
		//boolean res = wifiManag.setWifiEnabled(true);
		int networkAdded = wifi.addNetwork(wc);
		Log.d("WifiPreference", "add Network returned " + networkAdded);
		boolean isConfSaved = wifi.saveConfiguration();
		Log.d("WifiPreference", "saveConfiguration returned " + isConfSaved);
		boolean isConnectedToPi = wifi.enableNetwork(networkAdded, true);
		Log.d("WifiPreference", "enableNetwork returned " + isConnectedToPi);
	}

	public void getAvailableWiFiNetworks() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> availableNetworks = wifi.getScanResults();
		ArrayList<String> ssidList = new ArrayList<String>();
		networkList = new ArrayList<WiFiNetwork>();
		boolean flag = false;
		for (ScanResult sr : availableNetworks) {
			ssidList.add(sr.SSID);
			if(sr.SSID.equals(PiDevice.DEVICE_SSID))
			    flag = true;

			WiFiNetwork network = new WiFiNetwork();
			network.ssid = sr.SSID;
			network.capabilities = sr.capabilities;
			networkList.add(network);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ssidList);
		spinner.setAdapter(adapter);
		if(flag){
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Do you want to connect to the IF network!")
					.setPositiveButton("Go to settings",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(
											Settings.ACTION_WIFI_SETTINGS));
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});
			builder.show();
		}
	}
	
	public void getAvailableControllers(boolean isIFNetwork) {
	    
	    if(isIFNetwork){
		
	    }
	    else{
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		String ip = Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress());
		System.out.println(ip);
		String from = portFrom.getText().toString();
		String to = portTo.getText().toString();
		try {
		    controllerList = new SearchControllersTask(AddDeviceActivity.this, from, to).execute(ip).get();
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, controllerList);
		deviceSpinner.setAdapter(adapter);
		
	}

	public void connectToPi(WifiManager wifiManager) {
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration i : list) {
			if (i.SSID != null && i.SSID.equals("\"" + "pi" + "\"")) {

				wifiManager.disconnect();
				wifiManager.enableNetwork(i.networkId, true);
				wifiManager.reconnect();
				status.setText("Pi found. Connecting to pi...");
				break;
			}
		}
	}

	private class WiFiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
				if (intent.getBooleanExtra(
						WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
					WifiManager wm = (WifiManager) AddDeviceActivity.this
							.getSystemService(WIFI_SERVICE);
					status.setText("Connected to "
							+ wm.getConnectionInfo().getSSID());
					currentSsid = wm.getConnectionInfo().getSSID();
					getAvailableWiFiNetworks();
				}
			}
		}
	}
	

	@Override
	public ActionBarHandler createActionBarHandler() {
		return ActionBarHandlerFactory.createActionBarHandler(this,
				"Add new controller", Mode.NORMAL);
	}

	/*
	 * public void showAnotherDialog() { AlertDialog.Builder builder = new
	 * Builder(AddDeviceActivity.this); builder.setTitle("Devices")
	 * .setMessage("Do you want to add more devices?") .setPositiveButton("Yes",
	 * new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss(); } }) .setNegativeButton("No", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss(); Intent i = new Intent(AddDeviceActivity.this,
	 * DeviceListActivity.class); startActivity(i); finish(); } }).show(); }
	 */
}
