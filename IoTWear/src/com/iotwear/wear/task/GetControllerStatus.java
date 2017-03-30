package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iotwear.wear.gui.GetStatusCaller;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.NetworkUtil;

public class GetControllerStatus extends AsyncTask<Void, Void, String> {

    private ProgressDialog progress;
    private GetStatusCaller caller;
    private PiControl control;
    PrintWriter out;
    BufferedReader rd;
    Socket clientSocket;
    int passed;

    public GetControllerStatus(GetStatusCaller caller, PiControl control) {
	this.caller = caller;
	this.control = control;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	passed = 1;
	progress = ProgressDialog.show((Activity) caller, "Sync",
		"Getting data from Controller...");
    }

    @Override
    protected String doInBackground(Void... args) {

	try {
	    String urlPort = NetworkUtil.getDeviceUrl((Activity) caller,
		    control.getHostDevice());
	    InetAddress address = InetAddress.getByName(urlPort.split(":")[0]);
	    int port = Integer.parseInt(urlPort.split(":")[1]);
	    clientSocket = new Socket();
	    clientSocket.connect(new InetSocketAddress(address, port), 4000);
	    clientSocket.setSoTimeout(4000);
	    out = new PrintWriter(clientSocket.getOutputStream(), true);

	    // Send first message - Message is being correctly received
	    out.println("GET " + PiDevice.URL_STATUS + control.toQueryForSending() + " HTTP/1.1\r\n");
	    out.println("Cache-Control: no-cache\r\n");
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
	    if (!response.equals("")) {
		out.close();
		    rd.close();
		    clientSocket.close();
		return response.split(":")[4].trim().substring(response.split(":")[4].trim().length()-1);
	    }
	    out.close();
	    rd.close();
	    clientSocket.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    if(out != null)
		out.close();
	    try {
		if(rd != null)
		    rd.close();
		if(clientSocket != null)
		    clientSocket.close();
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		passed = 0;
		return "0";
	    }
	    passed = 0;
	    return "0";
	}

	/*
	 * try { HttpClient client = NetworkUtil.getDefaultHttpClient();
	 * 
	 * HttpGet req = new HttpGet("http://" +
	 * NetworkUtil.getDeviceUrl((Activity) caller, control.getHostDevice())
	 * + PiDevice.URL_STATUS + control.toQueryForSending()); HttpResponse
	 * response = client.execute(req);
	 * 
	 * int statusCode = response.getStatusLine().getStatusCode(); if
	 * (statusCode == HttpStatus.SC_OK) { return
	 * NetworkUtil.inputStreamToString(
	 * response.getEntity().getContent()).toString(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return "0"; }
	 */

	passed = 0;
	return "0";
    }

    @Override
    protected void onPostExecute(String result) {
	progress.dismiss();
	if(passed == 0)
	    Toast.makeText((Activity) caller, "Device not reachable!", Toast.LENGTH_SHORT).show();
	int res = 0;
	try {
	    res = Integer.parseInt(result);
	} catch (NumberFormatException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	caller.handleReceivedStatus((res == 1) ? true : false);
    }

}
