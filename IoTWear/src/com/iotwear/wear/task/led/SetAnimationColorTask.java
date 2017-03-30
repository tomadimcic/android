package com.iotwear.wear.task.led;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.iotwear.wear.gui.FavouritesActivity;
import com.iotwear.wear.gui.LEDControllerActivity;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.util.NetworkUtil;

public class SetAnimationColorTask extends AsyncTask<Integer, Void, Boolean> {

    private int sentColor;
    private PiControl control;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader rd;
    Context context;

    public SetAnimationColorTask(Context context, PiControl control) {
	this.control = control;
	this.context = context;
    }
    
    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	/*
	 * if (valueSender == null) progress = ProgressDialog.show(context,
	 * control.getName(), "Sending...");
	 */
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
	sentColor = params[0];

	try {
	    
	    String urlPort = NetworkUtil.getDeviceUrl(
		    context, control.getHostDevice());
	    //clientSocket = null;
	    InetAddress address = InetAddress.getByName(urlPort.split(":")[0]);
	    // clientSocket = new Socket(urlPort.split(":")[0],
	    // Integer.parseInt(urlPort.split(":")[1]));
	    int port = Integer.parseInt(urlPort.split(":")[1]);
	    //long start = System.currentTimeMillis();
	    clientSocket = new Socket(address, port);
	    //System.out.println(System.currentTimeMillis() - start);
	    clientSocket.setSoTimeout(3000);
	    out = new PrintWriter(clientSocket.getOutputStream(),
		    true);
	    // Send first message - Message is being correctly received
	    out.println("POST " + LEDControl.URL_SET_COLOR
		    + control.toQueryForSending() + "&r="
		    + Color.red(sentColor) + "&g=" + Color.green(sentColor)
		    + "&b=" + Color.blue(sentColor) + " HTTP/1.1\r\n");
	    // out.println("id: 1\r\n");
	    // out.println("v: 1\r\n");
	    out.println("Cache-Control: no-cache\r\n");
	    //out.println("Content-Type: text/html\r\n");
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
	    }
	    
	    return false;
	}

	

	return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
	
    }

}