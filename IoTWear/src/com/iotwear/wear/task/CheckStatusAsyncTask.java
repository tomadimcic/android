package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.DeviceListActivity;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.NetworkUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;

public class CheckStatusAsyncTask extends AsyncTask<Void, Void, Integer> {

    Activity activity;
    PiDevice pi;
    ImageView arrow;
    String user;
    String pass;
    int position;
    Socket clientSocket = null;
    PrintWriter out;
    BufferedReader rd;

    public CheckStatusAsyncTask(Activity activity, PiDevice pi,
	    ImageView arrow, String user, String pass, int position) {
	this.activity = activity;
	this.pi = pi;
	this.arrow = arrow;
	this.user = user;
	this.pass = pass;
	this.position = position;

    }

    @Override
    protected Integer doInBackground(Void... params) {
	int pom = 10;
	try {
	    String query = "default";

	    if (!user.equals("") && !pass.equals(""))
		query = user + pass;
	    String urlPort = NetworkUtil.getDeviceUrl(activity, pi);
	    InetAddress address = InetAddress.getByName(urlPort.split(":")[0]);
	    // clientSocket = new Socket(urlPort.split(":")[0],
	    // Integer.parseInt(urlPort.split(":")[1]));
	    clientSocket = new Socket(address, Integer.parseInt(urlPort
		    .split(":")[1]));
	    clientSocket.setSoTimeout(3000);
	    out = new PrintWriter(clientSocket.getOutputStream(),
		    true);
	    // BufferedReader br = new BufferedReader(new
	    // InputStreamReader(clientSocket.getInputStream()));

	    // Send first message - Message is being correctly received
	    out.println("POST /check?su=" + query + " HTTP/1.1\r\n");
	    // out.println("id: 1\r\n");
	    // out.println("v: 1\r\n");
	    // out.println("su:" + query);
	    out.println("Cache-Control: no-cache\r\n");
	    out.println("Content-Type: text/html\r\n");
	    out.println("\r\n\r\n");
	    out.flush();

	    rd = new BufferedReader(new InputStreamReader(
		    clientSocket.getInputStream(), "UTF-8"));
	    String line = null;
	    String response = "";
	    while ((line = rd.readLine()) != null) {
		response += line;
	    }
	    System.out.println(response);
	    pom = Integer.parseInt(response);
	    out.close();
	    rd.close();
	    clientSocket.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    out.close();
	    try {
		rd.close();
		clientSocket.close();
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	    
	    return 10;
	}

	/*
	 * URL url; String response = ""; int pom = 10; try { String query =
	 * "default"; if(!user.equals("") && !pass.equals("")) query = user +
	 * pass; url = new URL("http://" + NetworkUtil.getDeviceUrl(activity,
	 * pi) + "/check?su=" + query);
	 * 
	 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	 * conn.setReadTimeout(150); conn.setConnectTimeout(200);
	 * conn.setRequestMethod("GET"); //conn.setRequestProperty("Connection",
	 * "close"); //conn.getInputStream().close(); conn.setDoInput(true);
	 * conn.setDoOutput(true);
	 * 
	 * BufferedReader rd = new BufferedReader(new InputStreamReader(
	 * conn.getInputStream(), "UTF-8")); String line = null;
	 * 
	 * while ((line = rd.readLine()) != null) { response += line; }
	 * 
	 * pom = Integer.parseInt(response); } catch (MalformedURLException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); return 10; }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return 10; }
	 */
	// DeviceListActivity.changeChecked(position);
	return pom;
    }

    @Override
    protected void onPostExecute(Integer result) {
	if (result == 1)
	    arrow.setBackgroundResource(R.drawable.online1);
	else {
	    if (result == 0)
		arrow.setBackgroundResource(R.drawable.offline1);
	    else
		arrow.setBackgroundResource(R.drawable.offline2);
	}

    }

}
