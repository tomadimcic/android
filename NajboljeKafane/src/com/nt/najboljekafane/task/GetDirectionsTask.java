package com.nt.najboljekafane.task;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import com.nt.najboljekafane.util.NetworkUtil;

import android.os.AsyncTask;

public class GetDirectionsTask extends AsyncTask<String, String, Document>{

	@Override
	protected Document doInBackground(String... params) {
	    // TODO Auto-generated method stub
	    try {
	    HttpClient httpClient = NetworkUtil.getDefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse response = httpClient.execute(httpPost, localContext);
        InputStream in = response.getEntity().getContent();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(in);
        return doc;
	    } catch (Exception e) {
	            e.printStackTrace();
	        }
	    return null;
	}
	
}
