package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iotwear.wear.gui.ValueSendingInterface;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.LampControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.interfaces.SingleOptionControl;
import com.iotwear.wear.util.NetworkUtil;

public class SendToLamp extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progress;
    private Context context;
    private PiControl control;
    private ValueSendingInterface valueSender;
    int flag = 0;

    public SendToLamp(Context context, PiControl control, int flag) {
	this.control = control;
	this.context = context;
	this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	/*if (valueSender == null)
	    progress = ProgressDialog.show(context, control.getName(),
		    "Sending...");*/
    }

    @Override
    protected Boolean doInBackground(Void... params) {
	
	URL url = null;
	HttpURLConnection conn;
	try {
	    
	    if(flag == 0)
		url = new URL("http://"
		    	+ NetworkUtil.getDeviceUrl(context, control.getHostDevice())
		    	+ LampControl.URL_SET_COLOR
		    	+ control.toQueryForSending());
	    if(flag == 1)
		url = new URL("http://"
		    	+ NetworkUtil.getDeviceUrl(context, control.getHostDevice())
		    	+ LampControl.URL_SET_ANIMATION
		    	+ control.toQueryForSending());
	    if(flag == 2)
		url = new URL("http://"
		    	+ NetworkUtil.getDeviceUrl(context, control.getHostDevice())
		    	+ LampControl.URL_SET_ANIM
		    	+ control.toQueryForSending());
	    
	    System.out.println(url.getHost() + " " + url.getQuery());
	
	   //System.setProperty("http.keepAlive", "false");
	   conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(250);
		conn.setConnectTimeout(300);
		conn.setRequestMethod("POST");
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
		    System.out.println(response);
		    Thread.sleep(200);
		    //conn.disconnect();
	    /*if (valueSender != null) {
		Thread.sleep(200);
		valueSender.setSending(false);
	    }*/
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
	    
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    } catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
	} 
	
	/*HttpClient httpclient = NetworkUtil.getDefaultHttpClient();
	HttpPost httpPost = new HttpPost("http://"
		+ NetworkUtil.getDeviceUrl(context, control.getHostDevice())
		+ ((SingleOptionControl) control).getUrlSuffix() + control.toJsonForSending());

	try {
	    HttpResponse response = httpclient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();
	    Log.i("TAG", "Color changed response status:" + statusCode);
	    Thread.sleep(100);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}*/
	
	/*HttpClient httpclient = NetworkUtil.getDefaultHttpClient();
	HttpPost httpPost = new HttpPost("http://"
		+ NetworkUtil.getDeviceUrl(context, control.getHostDevice())
		+ ((SingleOptionControl) control).getUrlSuffix());

	try {
	    StringEntity jsonEnt = new StringEntity(control.toJsonForSending());
	    jsonEnt.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
		    "application/json"));
	    httpPost.setEntity(jsonEnt);
	    HttpResponse response = httpclient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();
	    Log.i("TAG",
		    control.getClass().getSimpleName() + " "
			    + control.getName() + " response status:"
			    + statusCode);
	    if (valueSender != null) {
		Thread.sleep(100);
		valueSender.setSending(false);
	    }

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return false;
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	    return false;
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    return false;
	}*/

	return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
	super.onPostExecute(result);
	/*if (valueSender == null)
	    progress.dismiss();*/

	if (!result) {
	    if (valueSender != null) {
		valueSender.setSending(false);
	    }
	    Toast.makeText(context, "Problem occcured, please try again.",
		    Toast.LENGTH_SHORT).show();
	}
    }

}
