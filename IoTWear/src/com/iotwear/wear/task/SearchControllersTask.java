package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.iotwear.wear.gui.AddDeviceActivity;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.util.NetworkUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

public class SearchControllersTask extends AsyncTask<String, Void, ArrayList<String>>{
    
    AddDeviceActivity activity;
    private ProgressDialog progress;
    String from;
    String to;
    ArrayList<String> ipList;
    ArrayList<String> ipList1;
    
    public SearchControllersTask(AddDeviceActivity activity, String from, String to){
	this.activity = activity;
	this.from = from;
	this.to = to;
	ipList = new ArrayList<String>();
	ipList1 = new ArrayList<String>();
    }
    
    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	progress = ProgressDialog.show(activity, "Wait please",
		"Searching for devices...");
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
	String ip = params[0];
	int f = Integer.parseInt(from);
	int t = Integer.parseInt(to);
	
	doScan(getIpAddress(ip, 0));
	while (f <= t) {
        	/*for (int i = 0; i < 256; i++) {
        	    ipadr = getIpAddress(ip, i);
        	    comunicate(ipadr + ":" + Integer.toString(f));
        	}*/
        	for (String ipItem : ipList) {
        	    communicate(ipItem + ":" + Integer.toString(f));
		}
        	f++;
	}
	return ipList1;
    }
    
    @Override
    protected void onPostExecute(ArrayList<String> result) {
	progress.dismiss();
    }
    
    public void communicate(String ip){
	    URL url;
	    HttpURLConnection conn;
		
		try {
		    url = new URL("http://" + ip + "/status");
		    	//+ NetworkUtil.getColorUrlString(sentColor));
		
		    
		    conn = (HttpURLConnection) url.openConnection();
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
		    String response = "";
		    while ((line = rd.readLine()) != null) {
			response += line;
		    }
		    if(response != "")
			ipList1.add(ip);
		    Thread.sleep(200);
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
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
        for(int dest=0; dest<255; dest++) {
            String host = ip + dest;
            executor.execute(pingRunnable(host));
        }

        Log.i("Nesto", "Waiting for executor to terminate...");
        executor.shutdown();
        try { executor.awaitTermination(35 * 1000, TimeUnit.MILLISECONDS); } catch (InterruptedException ignored) { }

        Log.i("Nesto", "Scan finished");
    }

    private Runnable pingRunnable(final String host) {
        return new Runnable() {
            public void run() {
                //Log.d("Nesto", "Pinging " + host + "...");
                try {
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
