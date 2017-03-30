package com.iotwear.wear.task.led;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iotwear.wear.model.Animation;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.util.NetworkUtil;

public class SetAnimationTask extends AsyncTask<Animation, Void, Boolean> {

    private ProgressDialog progress;
    private Activity activity;
    private Animation animation;
    private PiControl control;

    public SetAnimationTask(Activity activity, PiControl control) {
	this.activity = activity;
	this.control = control;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	progress = ProgressDialog.show(activity, "Animation",
		"Sending animation data to controller...");
    }

    @Override
    protected Boolean doInBackground(Animation... params) {
	animation = params[0];

	HttpClient httpclient = NetworkUtil.getDefaultHttpClient();
	HttpPost httpPost = new HttpPost("http://"
		+ NetworkUtil.getDeviceUrl(activity, control.getHostDevice())
		+ LEDControl.URL_SET_ANIMATION
		+ control.toQueryForSending());

	try {
	    StringEntity jsonEnt = new StringEntity(animation
		    .toJsonForSending().toString());
	    jsonEnt.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
		    "application/json"));
	    httpPost.setEntity(jsonEnt);
	    HttpResponse response = httpclient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();
	    Log.i("TAG", "Color changed response status:" + statusCode);
	    Thread.sleep(50);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
	progress.dismiss();

	if (!result) {
	    Toast.makeText(activity, "Error during device list sync",
		    Toast.LENGTH_SHORT).show();
	}

	//Intent i = new Intent(activity, DeviceListActivity.class);
	//activity.startActivity(i);
	//activity.finish();
    }

}
