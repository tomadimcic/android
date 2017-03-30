package com.iotwear.wear.task.led;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.iotwear.wear.gui.AnimationActivity;
import com.iotwear.wear.gui.FavouritesActivity;
import com.iotwear.wear.gui.LEDControllerActivity;
import com.iotwear.wear.gui.ValueSendingInterface;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.interfaces.SingleOptionControl;
import com.iotwear.wear.util.NetworkUtil;

public class SetColorTask extends AsyncTask<Integer, Void, Boolean> {

    private int sentColor;
    private PiControl control;
    private ValueSendingInterface colorSender;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader rd;

    public SetColorTask(ValueSendingInterface colorSender, PiControl control) {
	this.colorSender = colorSender;
	this.control = control;
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
	colorSender.setSending(true);

	try {
	    
	    String urlPort = NetworkUtil.getDeviceUrl(
		    colorSender.getActivity(), control.getHostDevice());
	    //clientSocket = null;
	    InetAddress address = InetAddress.getByName(urlPort.split(":")[0]);
	    // clientSocket = new Socket(urlPort.split(":")[0],
	    // Integer.parseInt(urlPort.split(":")[1]));
	    int port = Integer.parseInt(urlPort.split(":")[1]);
	    //long start = System.currentTimeMillis();
	    clientSocket = new Socket();
	    clientSocket.connect(new InetSocketAddress(address, port), 500);
	    //System.out.println(System.currentTimeMillis() - start);
	    clientSocket.setSoTimeout(3000);
	    out = new PrintWriter(clientSocket.getOutputStream(),
		    true);
	    // BufferedReader br = new BufferedReader(new
	    // InputStreamReader(clientSocket.getInputStream()));

	    /*System.out.println("POST " + LEDControl.URL_SET_COLOR
		    + control.toQueryForSending() + "&r="
		    + Color.red(sentColor) + "&g=" + Color.green(sentColor)
		    + "&b=" + Color.blue(sentColor) + " HTTP/1.1\r\n"
		    + "Cache-Control: no-cache\r\n"
		    + "Content-Type: text/html\r\n" + "\r\n\r\n");*/
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

	/*
	 * URL url; HttpURLConnection conn;
	 * 
	 * try { url = new URL("http://" +
	 * NetworkUtil.getDeviceUrl(colorSender.getActivity(),
	 * control.getHostDevice()) + LEDControl.URL_SET_COLOR +
	 * control.toQueryForSending() + "&r=" + Color.red(sentColor) + "&g=" +
	 * Color.green(sentColor) + "&b=" + Color.blue(sentColor)); //+
	 * NetworkUtil.getColorUrlString(sentColor));
	 * 
	 * 
	 * conn = (HttpURLConnection) url.openConnection();
	 * conn.setReadTimeout(150); conn.setConnectTimeout(200);
	 * conn.setRequestMethod("POST"); conn.setRequestProperty("Connection",
	 * "close"); //conn.getInputStream().close(); conn.setDoInput(true);
	 * conn.setDoOutput(true);
	 * 
	 * BufferedReader rd = new BufferedReader(new InputStreamReader(
	 * conn.getInputStream(), "UTF-8")); String line = null; String response
	 * = ""; while ((line = rd.readLine()) != null) { response += line; }
	 * Thread.sleep(200); } catch (MalformedURLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 */

	/*
	 * HttpClient httpclient = NetworkUtil.getDefaultHttpClient(); HttpPost
	 * httpPost = new HttpPost("http://" +
	 * NetworkUtil.getDeviceUrl(colorSender.getActivity(),
	 * control.getHostDevice()) + LEDControl.URL_SET_COLOR +
	 * control.toQueryForSending() + "&r=" + Color.red(sentColor) + "&g=" +
	 * Color.green(sentColor) + "&b=" + Color.blue(sentColor)); //+
	 * NetworkUtil.getColorUrlString(sentColor));
	 * 
	 * try { HttpResponse response = httpclient.execute(httpPost); int
	 * statusCode = response.getStatusLine().getStatusCode(); Log.i("TAG",
	 * "Color changed response status:" + statusCode); Thread.sleep(1000); }
	 * catch (UnsupportedEncodingException e) { e.printStackTrace(); } catch
	 * (ClientProtocolException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } catch (InterruptedException
	 * e) { e.printStackTrace(); }
	 */

	return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
	// FIXME One day, some day, remove dirty instanceof
	boolean isDeviceActivity = (colorSender instanceof LEDControllerActivity)
		|| (colorSender instanceof FavouritesActivity);

	if (colorSender.getCurrentValue() != sentColor && !isDeviceActivity)
	    new SetColorTask(colorSender, control).execute(colorSender
		    .getCurrentValue());
	else
	    colorSender.setSending(false);

	if (isDeviceActivity) {
	    boolean isTurnedOn;
	    if (sentColor == Color.BLACK)
		isTurnedOn = false;
	    else
		isTurnedOn = true;
	    if (colorSender instanceof LEDControllerActivity)
		((LEDControllerActivity) colorSender)
			.handleReceivedStatus(isTurnedOn);
	}
    }

}
