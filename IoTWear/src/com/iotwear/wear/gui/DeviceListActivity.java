package com.iotwear.wear.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.dialog.CustomizeDialogLogout;
import com.iotwear.wear.gui.dialog.CustomizeDialogSave;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiControl.PiControlType;
import com.iotwear.wear.model.interfaces.SingleOptionControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;
import com.iotwear.wear.util.NetworkUtil;

public class DeviceListActivity extends BaseActivity implements
	OnItemClickListener, ValueSendingInterface {

    private ArrayList<PiDevice> deviceList;
    private HashMap<String, ArrayList<PiDevice>> wifiDeviceMap;
    private DeviceListAdapter deviceListAdapter;

    private ListView deviceListView;
    // private Button addDevice;
    private TextView emptyList;
    private SharedPreferences prefs;
    String user;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	View content = LayoutInflater.from(this).inflate(
		R.layout.activity_device_list, null);
	contentFrame.addView(content);
	prefs = PreferenceManager
		.getDefaultSharedPreferences(DeviceListActivity.this);

	user = prefs.getString("user", "");
	pass = prefs.getString("pass", "");

	setMenu();

	deviceListView = (ListView) findViewById(R.id.device_list);
	emptyList = (TextView) findViewById(R.id.empty_list);

	deviceList = new ArrayList<PiDevice>();
	deviceList = DataManager.getInstance().getDeviceList();

	if (deviceList.size() == 0) {
	    deviceListView.setVisibility(View.GONE);
	    emptyList.setVisibility(View.VISIBLE);
	} else {
	    initWiFiDeviceMap();
	}
	deviceListAdapter = new DeviceListAdapter();
	deviceListView.setAdapter(deviceListAdapter);
	deviceListView.setOnItemClickListener(this);
	// }
    }

    @Override
    protected void onResume() {
	super.onResume();
	deviceList = DataManager.getInstance().getDeviceList();
	if (deviceListAdapter != null) {
	    if (deviceList.size() > 0) {
		deviceListView.setVisibility(View.VISIBLE);
		emptyList.setVisibility(View.GONE);
	    } else {
		deviceListView.setVisibility(View.GONE);
		emptyList.setVisibility(View.VISIBLE);
	    }
	    deviceListAdapter.notifyDataSetChanged();
	    deviceListAdapter.notifyDataSetInvalidated();
	}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position,
	    long id) {
	PiDevice selectedDevice = deviceList.get(position);
	if (selectedDevice.isSingleControllerDevice()
		&& !selectedDevice.getControlList().get(0).getName()
			.contains("Dmx0")) {
	    if (selectedDevice.getControlList().get(0).getPiControlType()
		    .equals(PiControlType.LED)) {
		Intent i = new Intent(DeviceListActivity.this,
			LEDControllerActivity.class);
		PiControl selectedControl = selectedDevice.getControlList()
			.get(0);
		selectedControl.setHostDevice(selectedDevice);
		i.putExtra(Constants.EXTRA_CONTROLLER, selectedControl);
		startActivity(i);
	    }
	    if (selectedDevice.getControlList().get(0).getPiControlType()
		    .equals(PiControlType.LAMP)) {
		Intent i = new Intent(DeviceListActivity.this,
			LAMPControllerActivity.class);
		PiControl selectedControl = selectedDevice.getControlList()
			.get(0);
		selectedControl.setHostDevice(selectedDevice);
		i.putExtra(Constants.EXTRA_CONTROLLER, selectedControl);
		startActivity(i);
	    }
	    if (selectedDevice.getControlList().get(0).getPiControlType()
		    .equals(PiControlType.DMX)) {
		Intent i = new Intent(DeviceListActivity.this,
			DMXControlActivity.class);
		PiControl selectedControl = selectedDevice.getControlList()
			.get(0);
		selectedControl.setHostDevice(selectedDevice);
		i.putExtra(Constants.EXTRA_CONTROLLER, selectedControl);
		startActivity(i);
	    }
	    if (selectedDevice.getControlList().get(0).getPiControlType()
		    .equals(PiControlType.IC)) {
		Intent i = new Intent(DeviceListActivity.this,
			ICControlActivity.class);
		PiControl selectedControl = selectedDevice.getControlList()
			.get(0);
		selectedControl.setHostDevice(selectedDevice);
		i.putExtra(Constants.EXTRA_CONTROLLER, selectedControl);
		startActivity(i);
	    }
	} else {
	    Intent i = new Intent(DeviceListActivity.this,
		    ControllerGridActivity.class);
	    i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
	    startActivity(i);
	}
    }

    // TODO For future release when device list should be segmented by SSID as
    // ExpandableListView
    private void initWiFiDeviceMap() {
	wifiDeviceMap = new HashMap<String, ArrayList<PiDevice>>();
	for (PiDevice pi : deviceList) {
	    if (wifiDeviceMap.containsKey(pi.getSsid())) {
		wifiDeviceMap.get(pi.getSsid()).add(pi);
	    } else {
		ArrayList<PiDevice> newList = new ArrayList<PiDevice>();
		newList.add(pi);
		wifiDeviceMap.put(pi.getSsid(), newList);
	    }
	}
    }

    private class DeviceListAdapter extends BaseAdapter {

	@Override
	public int getCount() {
	    return deviceList.size();
	}

	@Override
	public Object getItem(int position) {
	    return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    final PiDevice item = deviceList.get(position);
	    if (convertView == null)
		convertView = View.inflate(DeviceListActivity.this,
			R.layout.adapter_device_item_new, null);

	    TextView deviceName = (TextView) convertView
		    .findViewById(R.id.device_name);
	    ImageView arrow = (ImageView) convertView.findViewById(R.id.arrow);
	    if (!item.isChecked()){
		new CheckStatusAsyncTask(DeviceListActivity.this, item, arrow,
			user, pass, position, 1).execute();
		item.setChecked(true);
	    }

	    deviceName.setText(item.getName());

	    /*
	     * ToggleButton powerButton = (ToggleButton)
	     * convertView.findViewById(R.id.powerButton);
	     * powerButton.setChecked(item.isTurnedOn());
	     * powerButton.setOnCheckedChangeListener(new
	     * OnCheckedChangeListener() {
	     * 
	     * @Override public void onCheckedChanged(CompoundButton buttonView,
	     * boolean isChecked) { new
	     * SendColorToDevice(DeviceListActivity.this,
	     * item).execute(Color.WHITE); } });
	     */

	    return convertView;
	}
	
	public void notifyAdapter(){
	    notifyDataSetChanged();
	}
		

	public class CheckStatusAsyncTask extends
		AsyncTask<Void, Void, Integer> {

	    Activity activity;
	    PiDevice pi;
	    ImageView arrow;
	    String user;
	    String pass;
	    int position;
	    int attempts;
	    Socket clientSocket = null;
	    PrintWriter out;
	    BufferedReader rd;

	    public CheckStatusAsyncTask(Activity activity, PiDevice pi,
		    ImageView arrow, String user, String pass, int position,
		    int attempts) {
		this.activity = activity;
		this.pi = pi;
		this.arrow = arrow;
		this.user = user;
		this.pass = pass;
		this.position = position;
		this.attempts = attempts;

	    }

	    @Override
	    protected Integer doInBackground(Void... params) {
		URL url;
		String response = "";
		int pom = 10;

		try {
		    String query = "default";
		    if (!user.equals("") && !pass.equals(""))
			query = user; // + pass;
		    String urlPort = NetworkUtil.getDeviceUrl(activity, pi);
		    clientSocket = null;
		    InetAddress address = InetAddress.getByName(urlPort
			    .split(":")[0]);
		    clientSocket = new Socket();
		    clientSocket.connect(new InetSocketAddress(address, Integer.parseInt(urlPort
			    .split(":")[1])), 2000);
		    //clientSocket.setSoTimeout(1200);
		    out = new PrintWriter(clientSocket.getOutputStream(), true);

		    // Send first message - Message is being correctly received
		    out.println("GET " + "/check?su=" + query + " HTTP/1.1\r\n");
		    out.println("Cache-Control: no-cache\r\n");
		    out.println("\r\n");
		    out.flush();

		    rd = new BufferedReader(new InputStreamReader(
			    clientSocket.getInputStream(), "UTF-8"));
		    String line = null;
		    while ((line = rd.readLine()) != null) {
			response += line;
		    }
		    System.out.println(response);
		    //pom = Integer.parseInt(response);
		    if (!response.equals("")) {
			out.close();
			    rd.close();
			    clientSocket.close();
			return Integer.parseInt(response.split(":")[4].trim().substring(response.split(":")[4].trim().length()-1));
		    }
		    out.close();
		    rd.close();
		    clientSocket.close();
		} catch (Exception e) {
		    e.printStackTrace();
		    //pi.setChecked(true);
		    deviceList.get(position).setChecked(true);
		    this.cancel(true);
		    //notifyAdapter();
		    //this.cancel(true);
		    if(out != null)
			out.close();
		    try {
			if(rd != null)
			    rd.close();
			if(clientSocket != null)
			    clientSocket.close();
			return 10;
		    } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return 10;
		    }
		}

		/*
		 * try { String query = "default"; if(!user.equals("") &&
		 * !pass.equals("")) query = user; //+ pass; url = new
		 * URL("http://" + NetworkUtil.getDeviceUrl(activity, pi) +
		 * "/check?su=" + query);
		 * 
		 * HttpURLConnection conn = (HttpURLConnection)
		 * url.openConnection(); conn.setReadTimeout(150);
		 * conn.setConnectTimeout(200); conn.setRequestMethod("GET");
		 * conn.setRequestProperty("Connection", "close");
		 * //conn.getInputStream().close(); conn.setDoInput(true);
		 * conn.setDoOutput(true);
		 * 
		 * BufferedReader rd = new BufferedReader(new InputStreamReader(
		 * conn.getInputStream(), "UTF-8")); String line = null;
		 * 
		 * while ((line = rd.readLine()) != null) { response += line; }
		 * 
		 * pom = Integer.parseInt(response); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); return 10; } catch (IOException e)
		 * { // TODO Auto-generated catch block e.printStackTrace();
		 * return 10; }
		 */
		return 10;
	    }

	    @Override
	    protected void onPostExecute(Integer result) {
		if (result == 1) {
		    // arrow.setBackgroundResource(R.drawable.online1);
		    // pi.setChecked(true);
		} else {
		    //pi.setChecked(true);
		    deviceList.get(position).setChecked(true);
		    //notifyAdapter();
		    this.cancel(true);
		    if (result == 0) {
			if (attempts == 1) {
			    // arrow.setBackgroundResource(R.drawable.offline1);
			    // notifyDataSetChanged();

			    new CheckStatusAsyncTask(DeviceListActivity.this,
				    pi, arrow, user, pass, position, 2)
				    .execute();
			}
			if (attempts == 2) {
			    try {
				if (deviceList.get(position) != null)
				    deviceList.remove(position);
				DataManager.getInstance().deleteDevice(pi);
				notifyAdapter();
			    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}

		    }
		    /*
		     * else{
		     * 
		     * arrow.setBackgroundResource(R.drawable.offline2); }
		     */
		}

	    }

	}

    }

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this, "Devices",
		Mode.SLIDING);
    }

    @Override
    public void setSending(boolean isSending) {
	// TODO Auto-generated method stub

    }

    @Override
    public int getCurrentValue() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public Activity getActivity() {
	// TODO Auto-generated method stub
	return this;
    }

    public void setMenu() {
	TextView save = (TextView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item1, null);
	save.setId(R.id.actionbar_save);
	save.setText("Save");

	save.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showSaveDialog();

	    }
	});

	TextView status = (TextView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item1, null);
	status.setId(R.id.actionbar_status);
	if (!user.equals(""))
	    status.setText("Loged in as " + user);
	else
	    status.setText("Login");

	status.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (!user.equals(""))
		    showLogoutDialog();
		else {
		    prefs.edit().putString("user", "").commit();
		    prefs.edit().putString("pass", "").commit();
		    prefs.edit().putInt("remember", 0).commit();
		    prefs.edit().putInt("signed", 0).commit();
		    Intent i = new Intent(DeviceListActivity.this,
			    LoginActivity.class);
		    startActivity(i);
		    finish();
		}
	    }
	});
	getActionBarHandler().addMenuItem(save);
	getActionBarHandler().addMenuItem(status);
    }

    public void showLogoutDialog() {
	CustomizeDialogLogout customizeDialog = new CustomizeDialogLogout(
		DeviceListActivity.this, prefs);
	customizeDialog.setTitle("Logout");
	customizeDialog.setMessage("Are you sure you want to logout?");
	customizeDialog.show();
    }

    public void showSaveDialog() {
	CustomizeDialogSave customizeDialog = new CustomizeDialogSave(
		DeviceListActivity.this, deviceList);
	customizeDialog.setTitle("Save state");
	customizeDialog
		.setMessage("Check the devices tou want to save current state!");
	customizeDialog.show();
    }

}
