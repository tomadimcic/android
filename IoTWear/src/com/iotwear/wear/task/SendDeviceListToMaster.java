package com.iotwear.wear.task;

import java.io.IOException;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iotwear.wear.gui.DeviceListActivity;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.DataManager;
import com.iotwear.wear.util.NetworkUtil;

public class SendDeviceListToMaster extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog progress;
	Activity activity;
	String ipAddress;

	public SendDeviceListToMaster(Activity activity, String ipAddress) {
		this.activity = activity;
		this.ipAddress = ipAddress;
	    }

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = ProgressDialog.show(activity, "Sync",
				"Sending data to device...");
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {

		HttpClient httpclient = NetworkUtil.getDefaultHttpClient();

		ArrayList<PiDevice> deviceList = DataManager.getInstance()
				.getDeviceListForSending();
		JSONArray devArray = new JSONArray();

		for (PiDevice pi : deviceList) {
			try {
				devArray.put(PiDevice.toJson(pi));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		HttpPost httpPost = new HttpPost("http://" + ipAddress + ":61616/sync");
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
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progress.dismiss();

		if (!result) {
			Toast.makeText(activity, "Error during device list sync",
					Toast.LENGTH_SHORT).show();
		}

		Intent i = new Intent(activity, DeviceListActivity.class);
		activity.startActivity(i);
		activity.finish();
	}
}