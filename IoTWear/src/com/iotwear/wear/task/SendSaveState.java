package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iotwear.wear.gui.ValueSendingInterface;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.interfaces.SingleOptionControl;
import com.iotwear.wear.util.NetworkUtil;

public class SendSaveState extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progress;
    private Context context;
    private PiDevice device;
    private ValueSendingInterface valueSender;

    public SendSaveState(Context context, PiDevice device) {
	this.device = device;
	this.context = context;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
	
	URL url;
	HttpURLConnection conn;
	try {
	   url = new URL("http://"
			+ NetworkUtil.getDeviceUrl(context, device) + "/save");
	
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
