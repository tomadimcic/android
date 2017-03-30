package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iotwear.wear.gui.ValueSendingInterface;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.TasterControl;
import com.iotwear.wear.model.interfaces.SingleOptionControl;
import com.iotwear.wear.util.NetworkUtil;

public class SendSingleControlData extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progress;
    private Context context;
    private PiControl control;
    private ValueSendingInterface valueSender;
    long startTime;
    Socket clientSocket = null;
    PrintWriter out;
    BufferedReader rd;

    public SendSingleControlData(Context context, PiControl control) {
	this.control = control;
	this.context = context;
    }

    public SendSingleControlData(Context context, PiControl control,
	    ValueSendingInterface valueSender) {
	this.control = control;
	this.context = context;
	this.valueSender = valueSender;
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
    protected Boolean doInBackground(Void... params) {
	try {
	    String urlPort = NetworkUtil.getDeviceUrl(context,
		    control.getHostDevice());
	    clientSocket = null;
	    InetAddress address = InetAddress.getByName(urlPort.split(":")[0]);
	    // clientSocket = new Socket(urlPort.split(":")[0],
	    // Integer.parseInt(urlPort.split(":")[1]));
	    clientSocket = new Socket(address, Integer.parseInt(urlPort.split(":")[1]));
	    clientSocket.setSoTimeout(3000);
	    out = new PrintWriter(clientSocket.getOutputStream(),
		    true);
	    // BufferedReader br = new BufferedReader(new
	    // InputStreamReader(clientSocket.getInputStream()));

	    // Send first message - Message is being correctly received
	    out.println("POST "
		    + ((SingleOptionControl) control).getUrlSuffix()
		    + control.toQueryForSending() + " HTTP/1.1\r\n");
	    // out.println("id: 1\r\n");
	    // out.println("v: 1\r\n");
	    // out.println(control.toQueryForSending());
	    out.println("Cache-Control: no-cache\r\n");
	    //out.println("Content-Type: text/html\r\n");
	    out.println("\r\n");
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

	URL url;
	HttpURLConnection conn;
	// try {
	/*
	 * url = new URL("http://" + NetworkUtil.getDeviceUrl(context,
	 * control.getHostDevice()) + ((SingleOptionControl)
	 * control).getUrlSuffix() + control.toQueryForSending());
	 */

	// System.setProperty("http.keepAlive", "false");
	/*
	 * conn = (HttpURLConnection) url.openConnection();
	 * conn.setReadTimeout(650); conn.setConnectTimeout(700);
	 * conn.setRequestMethod("POST"); conn.setRequestProperty("Connection",
	 * "close");
	 */
	// conn.setRequestProperty("Connection", "keep-alive");
	// conn.setRequestProperty("Accept",
	// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	// conn.setRequestProperty("User-Agent",
	// "Mozilla/5.0 (Linux; U; Android 5.0.1; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
	/*
	 * conn.setRequestProperty("Accept-Language",
	 * "en-US,en;q=0.8,hr;q=0.6,sr;q=0.4");
	 * conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
	 * conn.setRequestProperty("Cache-Control", "no-cache");
	 */
	// conn.getInputStream().close();
	/*
	 * conn.setDoInput(true); conn.setDoOutput(true);
	 * 
	 * BufferedReader rd = new BufferedReader(new InputStreamReader(
	 * conn.getInputStream(), "UTF-8")); String line = null; String response
	 * = ""; while ((line = rd.readLine()) != null) { response += line; }
	 * System.out.println(response); Thread.sleep(200); rd.close();
	 * conn.disconnect();
	 */
	/*
	 * if (valueSender != null) { Thread.sleep(200);
	 * valueSender.setSending(false); }
	 */
	/*
	 * } catch (MalformedURLException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); return false;
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return false; } catch (InterruptedException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); return false;
	 * }
	 */

	/*
	 * HttpClient httpclient = NetworkUtil.getDefaultHttpClient(); HttpPost
	 * httpPost = new HttpPost("http://" + NetworkUtil.getDeviceUrl(context,
	 * control.getHostDevice()) + ((SingleOptionControl)
	 * control).getUrlSuffix() + control.toJsonForSending());
	 * 
	 * try { HttpResponse response = httpclient.execute(httpPost); int
	 * statusCode = response.getStatusLine().getStatusCode(); Log.i("TAG",
	 * "Color changed response status:" + statusCode); Thread.sleep(100); }
	 * catch (UnsupportedEncodingException e) { e.printStackTrace(); } catch
	 * (ClientProtocolException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } catch (InterruptedException
	 * e) { e.printStackTrace(); }
	 */

	/*
	 * HttpClient httpclient = NetworkUtil.getDefaultHttpClient(); HttpPost
	 * httpPost = new HttpPost("http://" + NetworkUtil.getDeviceUrl(context,
	 * control.getHostDevice()) + ((SingleOptionControl)
	 * control).getUrlSuffix() + control.toQueryForSending());
	 */
	// httpPost.setHeader("Connection", "close");
	// httpPost.setHeader("Accept",
	// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	// httpPost.setHeader("User-Agent",
	// "Mozilla/5.0 (Linux; U; Android 5.0.1; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
	// httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
	// httpPost.setHeader("keep-alive", "false");

	// try {
	/*
	 * StringEntity jsonEnt = new StringEntity(control.toJsonForSending());
	 * jsonEnt.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
	 * "application/json")); httpPost.setEntity(jsonEnt);
	 */
	/*
	 * startTime = System.currentTimeMillis(); HttpResponse response =
	 * httpclient.execute(httpPost); //System.out.println("tuuuuuu " +
	 * ((TasterControl) control).isTurnedOn());
	 * 
	 * String res = ""; int statusCode =
	 * response.getStatusLine().getStatusCode(); if (statusCode ==
	 * HttpStatus.SC_OK) { res = NetworkUtil.inputStreamToString(
	 * response.getEntity().getContent()).toString(); }
	 */
	/*
	 * System.out.println(res); Log.i("TAG",
	 * control.getClass().getSimpleName() + " " + control.getName() +
	 * " response status:" + statusCode);
	 */
	// System.out.println(System.currentTimeMillis() - startTime);
	/*
	 * if (valueSender != null) { Thread.sleep(200);
	 * valueSender.setSending(false); }
	 */

	/*
	 * } catch (UnsupportedEncodingException e) { e.printStackTrace();
	 * return false; } catch (ClientProtocolException e) {
	 * e.printStackTrace(); return false; } catch (IOException e) {
	 * e.printStackTrace(); return false; } catch (InterruptedException e) {
	 * e.printStackTrace(); return false; }
	 */

	return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
	super.onPostExecute(result);
	/*
	 * if (valueSender == null) progress.dismiss();
	 */

	if (!result) {
	    if (valueSender != null) {
		valueSender.setSending(false);
	    }
	    Toast.makeText(context, "Problem occcured, please try again.",
		    Toast.LENGTH_SHORT).show();
	}
    }

}
