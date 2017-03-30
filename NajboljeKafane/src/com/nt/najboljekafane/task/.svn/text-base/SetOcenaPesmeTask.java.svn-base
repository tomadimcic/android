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

public class SetOcenaPesmeTask extends AsyncTask<String, String, Boolean> {

	private static final String OCENA_PESME_URL = "http://www.najboljekafane.rs/android/webapi/ocena_pesme_post.php";

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		try {
		    String response = null;
		    
		    HttpClient client = NetworkUtil.getDefaultHttpClient();

			HttpPost postReq = new HttpPost();
			postReq.setURI(new URI(OCENA_PESME_URL));
			//OutputStreamWriter request = null;
			
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair(
					"pesma_id", arg0[0]));
			postParameters.add(new BasicNameValuePair(
					"deviceUUID", arg0[1]));
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
			
			/*URL url = new URL(OCENA_PESME_URL);
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
			}*/

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}

}
