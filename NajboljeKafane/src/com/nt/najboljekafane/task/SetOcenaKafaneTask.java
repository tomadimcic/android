package com.nt.najboljekafane.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.nt.najboljekafane.util.NetworkUtil;

import android.os.AsyncTask;
import android.util.Log;

public class SetOcenaKafaneTask extends AsyncTask<String, String, Boolean> {
    
    private static final String OCENA_KAFANE_URL = "http://www.najboljekafane.rs/android/webapi/ocena_kafane_post.php";

	@Override
    protected Boolean doInBackground(String... arg0) {

	try {
	    
	    String response = null;
	    
	    HttpClient client = NetworkUtil.getDefaultHttpClient();

		HttpPost postReq = new HttpPost();
		postReq.setURI(new URI(OCENA_KAFANE_URL));
		
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(
				"kafana_id", arg0[0]));
		postParameters.add(new BasicNameValuePair(
			"ocena_kafane", arg0[1]));
		postParameters.add(new BasicNameValuePair(
			"ocena_atmosfere", arg0[2]));
		postParameters.add(new BasicNameValuePair(
				"deviceUUID", arg0[3]));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postReq.setEntity(formEntity);
		HttpResponse httpResponse = client.execute(postReq);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			response = NetworkUtil
					.convertStreamToString(httpResponse.getEntity()
							.getContent());
			Log.i("RESP", response);
		}
	    
	    
	    /*OutputStreamWriter request = null;
	    String parameters = "kafana_id=" + arg0[0] + "&ocena_kafane="
		    + arg0[1] + "&ocena_atmosfere=" + arg0[2];

	    URL url = new URL(OCENA_KAFANE_URL);
	    HttpURLConnection connection = (HttpURLConnection) url
		    .openConnection();
	    connection.setDoOutput(true);
	    connection.setRequestProperty("Content-Type",
		    "application/x-www-form-urlencoded");
	    connection.setRequestMethod("POST");

	    request = new OutputStreamWriter(connection.getOutputStream());
	    request.write(parameters);
	    request.flush();
	    request.close();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(
		    connection.getInputStream(), "UTF-8"));
	    String line = null;
	    StringBuilder builder = new StringBuilder();
	    while ((line = rd.readLine()) != null) {
		builder.append(line);
	    }

	    System.out.println("-=-=-=-KAFANA DELETE RESPONSE -=-=-=-=-="
		    + builder.toString());*/

	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;

    }

}
