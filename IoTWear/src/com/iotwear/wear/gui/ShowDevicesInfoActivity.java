package com.iotwear.wear.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.DataManager;
import com.iotwear.wear.util.NetworkUtil;

public class ShowDevicesInfoActivity extends BaseActivity{
    
    private ArrayList<PiDevice> deviceList;
    private DeviceInfoListAdapter deviceInfoListAdapter;

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
		R.layout.activity_device_info_list, null);
	contentFrame.addView(content);
	prefs = PreferenceManager
		.getDefaultSharedPreferences(ShowDevicesInfoActivity.this);
	
	user = prefs.getString("user", "");
	pass = prefs.getString("pass", "");
	
	deviceListView = (ListView) findViewById(R.id.device_list1);
	emptyList = (TextView) findViewById(R.id.empty_list1);

	deviceList = new ArrayList<PiDevice>();
	deviceList = DataManager.getInstance().getDeviceList();

	if (deviceList.size() == 0) {
	    deviceListView.setVisibility(View.GONE);
	    emptyList.setVisibility(View.VISIBLE);
	}
	    deviceInfoListAdapter = new DeviceInfoListAdapter();
	    deviceListView.setAdapter(deviceInfoListAdapter);
	//}
    }
    
    private class DeviceInfoListAdapter extends BaseAdapter {

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
		convertView = View.inflate(ShowDevicesInfoActivity.this,
			R.layout.adapter_device_info_item_new, null);

	    TextView deviceName = (TextView) convertView
		    .findViewById(R.id.device_info_name);
	    TextView url = (TextView) convertView
		    .findViewById(R.id.textView2);
	    TextView localIP = (TextView) convertView
		    .findViewById(R.id.textView4);
	    TextView port = (TextView) convertView
		    .findViewById(R.id.textView6);
	    TextView ssid = (TextView) convertView
		    .findViewById(R.id.textView8);
	    TextView mac = (TextView) convertView
		    .findViewById(R.id.textView10);
	    ImageView arrow = (ImageView) convertView.findViewById(R.id.arrow);
	    if(!item.isChecked())
		new CheckStatusAsyncTask(ShowDevicesInfoActivity.this, item, arrow, user, pass, position, 1).execute();
	    
	    deviceName.setText(item.getName());
	    url.setText(item.getUrl());
	    localIP.setText(item.getLocalIp());
	    port.setText(Integer.toString(item.getPort()));
	    ssid.setText(item.getSsid());
	    mac.setText(item.getMac());
	    
	    return convertView;
	}
	
	public class CheckStatusAsyncTask extends AsyncTask<Void, Void, Integer> {
	    
	    Activity activity;
	    PiDevice pi;
	    ImageView arrow;
	    String user;
	    String pass;
	    int position;
	    int attempts;
	    
	    public CheckStatusAsyncTask(Activity activity, PiDevice pi, ImageView arrow, String user, String pass, int position, int attempts){
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
		    if(!user.equals("") && !pass.equals(""))
			query = user + pass;
		    url = new URL("http://" + NetworkUtil.getDeviceUrl(activity, pi) + "/check?su=" + query);
		
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(150);
			conn.setConnectTimeout(200);
			conn.setRequestMethod("GET");
			//conn.setRequestProperty("Connection", "close");
			//conn.getInputStream().close();
			conn.setDoInput(true);
			conn.setDoOutput(true);

		    BufferedReader rd = new BufferedReader(new InputStreamReader(
			    conn.getInputStream(), "UTF-8"));
		    String line = null;
		    
		    while ((line = rd.readLine()) != null) {
			response += line;
		    }
		    
		    pom = Integer.parseInt(response);
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return 10;
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return 10;
		}
		return pom;
	    }
	    
	    @Override
	    protected void onPostExecute(Integer result) {
	        if(result == 1){
	            arrow.setBackgroundResource(R.drawable.online1);
	            pi.setChecked(true);
	        }
	        else{
	            if(result == 0){
	        	if(attempts == 1){
	        	    arrow.setBackgroundResource(R.drawable.offline1);
	        	    notifyDataSetChanged();
		        	
	        	    new CheckStatusAsyncTask(ShowDevicesInfoActivity.this, pi, arrow, user, pass, position, 2).execute();
	        	}
	        	if(attempts == 2){
	        	    arrow.setBackgroundResource(R.drawable.offline1);
	        	    notifyDataSetChanged();
	        	}
	        	
	            }
	            else{
	        	
	        	arrow.setBackgroundResource(R.drawable.offline2);
	            }
	        }
	            
	    }

	}

    }

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this,
		"Info", Mode.SLIDING);
    }

}
