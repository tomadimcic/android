package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.DeviceListActivity;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.DataManager;

public class GetDeviceListFromMaster extends AsyncTask<Void, Integer, Boolean> {

    Activity activity;
    ProgressDialog progress;
    String ipAddress;
    String portFrom;
    String portTo;
    String ip;
    ArrayList<String> ipList;
    private SharedPreferences prefs;
    String user;
    String pass;
    //ProgressBar progressBar;

    public GetDeviceListFromMaster(Activity activity, String ipAddress) {
	this.activity = activity;
	this.ipAddress = ipAddress;
    }
    
    public GetDeviceListFromMaster(Activity activity, String portFrom, String portTo, String ip) {
	this.activity = activity;
	this.portFrom = portFrom;
	this.portTo = portTo;
	this.ip = ip;
	
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	/*progressBar = (ProgressBar) activity.findViewById(R.id.progressBar1);
	progressBar.setVisibility(ProgressBar.VISIBLE);
	progress = new ProgressDialog(activity);
	progress.setTitle("Sync");
	progress.setMessage("Getting data from controllers...");*/
	progress = ProgressDialog.show(activity, "Sync",
		"Getting data from controllers...");
	prefs = PreferenceManager
		.getDefaultSharedPreferences(activity);
	
	user = prefs.getString("user", "");
	pass = prefs.getString("pass", "");
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {

	if(portFrom == null && portTo == null){
	    communicate(ipAddress);
	    //progress.show();
	}
	else{
	    int f = Integer.parseInt(portFrom);
		int t = Integer.parseInt(portTo);
		//ArrayList<String> ipList = new ArrayList<String>();
		String ipadr = "";
		doScan(getIpAddress(ip, 0));
		while (f <= t) {
	        	/*for (int i = 0; i < 256; i++) {
	        	    ipadr = getIpAddress(ip, i);
	        	    comunicate(ipadr + ":" + Integer.toString(f));
	        	}*/
		    //progress.show();
		    System.out.println("Ovde " + (100/(t-f+1)));
		    //publishProgress((100/(t-f+1)));
	        	for (String ipItem : ipList) {
	        	    communicate(ipItem + ":" + Integer.toString(f));
			}
	        	f++;
		}
	}

	return true;
    }
    
    /*@Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
    }*/

    @Override
    protected void onPostExecute(Boolean result) {
	progress.dismiss();
	//progressBar.setVisibility(ProgressBar.INVISIBLE);

	if (!result) {
	    Toast.makeText(activity, "Error during device list sync",
		    Toast.LENGTH_SHORT).show();
	}

	Intent i = new Intent(activity, DeviceListActivity.class);
	activity.startActivity(i);
	activity.finish();
    }
    
    public boolean communicate(String ip){
	try {
	    boolean alreadyIn;

	    // TODO Presaltati na HTTPClient i HTTPGet kao u drugim
	    // taskovima. I pocistiti komentare i ostale nepotrebne
	    // stvari...
	    String query = "default";
	    if(!user.equals("") && !pass.equals(""))
		query = user + pass;
	    //URL url = new URL("http://" + ip + "/sync");
	    URL url = new URL("http://" + ip + "/sync?su=" + query);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(2550);
		conn.setConnectTimeout(2600);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Connection", "close");
		//conn.getInputStream().close();
		conn.setDoInput(true);
		conn.setDoOutput(true);

	    BufferedReader rd = new BufferedReader(new InputStreamReader(
		    conn.getInputStream(), "UTF-8"));
	    String line = null;
	    String response = "";
	    while ((line = rd.readLine()) != null) {
		response += line;
	    }
	    if (response != "") {
		System.out.println(response);
		PiDevice pi;
		JSONArray ja = new JSONArray(response);
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
	    }
	    Thread.sleep(10);

	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }
    
    public static String getIpAddress(String ip, int count) {
	try {
	    String[] ipAdd = ip.split("\\.");
	    int d = Integer.parseInt(ipAdd[3]);
	    //d = d + count;
	    StringBuilder builder = new StringBuilder();
	    builder.append(ipAdd[0]);
	    builder.append(".");
	    builder.append(ipAdd[1]);
	    builder.append(".");
	    builder.append(ipAdd[2]);
	    builder.append(".");
	    //builder.append(count);

	    return builder.toString();
	} catch (NumberFormatException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return "";
	}
    }
    
    private static final int NB_THREADS = 25;

    public void doScan(String ip) {
	ipList = new ArrayList<String>();
	Log.i("Nesto", "Start scanning");

	ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
	for (int dest = 0; dest < 255; dest++) {
	    String host = ip + dest;
	    executor.execute(pingRunnable(host));
	    //System.out.println(((dest * 100) / 256)+1);
	    //publishProgress(((dest * 100) / 256)+1);
	}

	Log.i("Nesto", "Waiting for executor to terminate...");
	
	try {
	    executor.awaitTermination(35 * 1000, TimeUnit.MILLISECONDS);
	    executor.shutdown();
	} catch (InterruptedException ignored) {
	}

	Log.i("Nesto", "Scan finished");
    }

    private Runnable pingRunnable(final String host) {
        return new Runnable() {
            public void run() {
                //Log.d("Nesto", "Pinging " + host + "...");
                try {
                    //System.out.println(host);
                    Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 " + host);
                    int returnVal = p1.waitFor();
                    boolean reachable = (returnVal==0);
                    if(reachable){
                	ipList.add(host);
                	System.out.println(host);
                    }
                    //Log.d("Nesto", "=> Result: " + (reachable ? "reachable" : "not reachable"));
                } catch (UnknownHostException e) {
                    Log.e("Nesto", "Not found", e);
                } catch (IOException e) {
                    Log.e("Nesto", "IO Error", e);
                } catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
            }
        };
    }

}
