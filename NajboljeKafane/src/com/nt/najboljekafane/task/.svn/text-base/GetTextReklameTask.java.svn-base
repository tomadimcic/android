package com.nt.najboljekafane.task;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.nt.najboljekafane.util.NetworkUtil;

import android.os.AsyncTask;
import android.util.Log;

public class GetTextReklameTask extends AsyncTask<Void, Void, String>{

    public static final String REKLAME_TEXT_URL = "http://www.najboljekafane.rs/android/webapi/reklama.php";
    
    @Override
    protected void onPreExecute() {
	super.onPreExecute();
    }
    
    @Override
    protected String doInBackground(Void... params) {
	String response = null;
	try {
	    HttpClient client = NetworkUtil.getDefaultHttpClient();

	    HttpGet getReq = new HttpGet();
	    getReq.setURI(new URI(REKLAME_TEXT_URL));
	    HttpResponse httpResponse = client.execute(getReq);
	    
	    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		response = NetworkUtil
			.convertStreamToString(httpResponse.getEntity()
				.getContent());
		Log.i("RESP", response);
	    }
	} catch (URISyntaxException e) {
	    e.printStackTrace();
	    return null;
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	    return null;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
	return response;
    }
    
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

}
