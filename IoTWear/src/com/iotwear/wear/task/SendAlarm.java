package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iotwear.wear.gui.DeviceListActivity;
import com.iotwear.wear.gui.NewAlarmControlActivity;
import com.iotwear.wear.model.Alarm;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.DataManager;
import com.iotwear.wear.util.NetworkUtil;

public class SendAlarm extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog progress;
	Activity activity;
	ArrayList<Alarm> alForSending;
	PiDevice selectedDevice;

	public SendAlarm(Activity activity, ArrayList<Alarm> alForSending, PiDevice selectedDevice) {
		this.activity = activity;
		this.alForSending = alForSending;
		this.selectedDevice = selectedDevice;
	    }

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//progress = ProgressDialog.show(activity, "Alarm",
		//		"Sending alarm data to controller...");
	}
	
	@Override
	    protected Boolean doInBackground(Void... arg0) {
		boolean response = false;
		int number = 0;
		ArrayList<String> diffTypes = new ArrayList<String>();
		String type = "";
		String id = "";
		String value = "";
		boolean flag = false;
		
		for (Alarm alarm : alForSending) {
		    for (int k = 0; k < alarm.getType().size(); k++) {
			id += alarm.getId().get(k) + "+";
			value += alarm.getValue().get(k) + "+";
			type += alarm.getType().get(k) + "+";
		    }
		    
		    if(number < 10){
			    response = request(number, alarm.getSecondsToAlarm(), type.substring(0, type.length()-1), 
				id.substring(0, id.length()-1), value.substring(0, value.length()-1), alarm.getDaily()); 
			    number++;  
				id = "";
				value = "";
				type = "";
			}
		    
		}
		
		/*for (Alarm alarm : alForSending) {
		    for (String types : alarm.getType()) {
			if(diffTypes.size() == 0)
			    diffTypes.add(types);
			else{
			    for (String diff : diffTypes) {
				if(types.equals(diff))
				    flag = true;
			    }
			    if(!flag)
				diffTypes.add(types);
			}
			flag = false;
		    }
		    for (String diff : diffTypes) {
			for (int k = 0; k < alarm.getType().size(); k++) {
			    if(alarm.getType().get(k).equals(diff)){
				id += alarm.getId().get(k) + "+";
				value += alarm.getValue().get(k) + "+";
			    }
			}
			if(number < 10){
			    response = request(number, alarm.getSecondsToAlarm(), diff, 
				id.substring(0, id.length()-1), value.substring(0, value.length()-1), alarm.getDaily()); 
			    number++;  
				id = "";
				value = "";
			}
		    }
			
			
			try {
			    Thread.sleep(500);
			} catch (InterruptedException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    
		}*/
		return response;
	    }
	
	public boolean request(int number, String sec, String type, String id, String value, String daily){
	    URL url;
		String response = "";
		
		System.out.println("http://"
				+ NetworkUtil.getDeviceUrl(activity, selectedDevice)
				+ "/alarm?n="+ number + "&sec=" + sec + "&t=" + type + "&id=" + id + "&do=" + value + "&d=" + daily);
		
		try {
		    url = new URL("http://"
				+ NetworkUtil.getDeviceUrl(activity, selectedDevice)
				+ "/alarm?n="+ number + "&sec=" + sec + "&t=" + type + "&id=" + id + "&do=" + value + "&d=" + daily);
		
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(150);
			conn.setConnectTimeout(200);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Connection", "close");
			//conn.getInputStream().close();
			conn.setDoInput(true);
			conn.setDoOutput(true);

		    BufferedReader rd = new BufferedReader(new InputStreamReader(
			    conn.getInputStream(), "UTF-8"));
		    String line = null;
		    
		    while ((line = rd.readLine()) != null) {
			response += line;
		    }
		    
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return false;
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return false;
		}
		return true;
	}

	/*@Override
	protected Boolean doInBackground(Void... arg0) {

		HttpClient httpclient = NetworkUtil.getDefaultHttpClient();

		JSONObject devArray = Alarm.getJsonListForSending(alForSending);

		HttpPost httpPost = new HttpPost("http://"
			+ NetworkUtil.getDeviceUrl(activity, selectedDevice)
			+ "/alarm");
		try {
			StringEntity jsonEnt = new StringEntity(devArray.toString());
			jsonEnt.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(jsonEnt);
			HttpResponse response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return true;
			}

		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return false;
	}*/

	@Override
	protected void onPostExecute(Boolean result) {
		//progress.dismiss();

		if (!result) {
			Toast.makeText(activity, "Error during sending alarm",
					Toast.LENGTH_SHORT).show();
		}
		else
		    Toast.makeText(
			    activity,
			    "Alarm succesfully set!",
			    Toast.LENGTH_SHORT).show();

		//Intent i = new Intent(activity, DeviceListActivity.class);
		//activity.startActivity(i);
		activity.finish();
	}
}