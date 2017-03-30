package dunavnet.com.mojns.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import dunavnet.com.mojns.interfaces.ResponseHandler;

public class LoginSiginTask extends AsyncTask<Void, Void, String> {

    public static final String SYNC_URL = "http://idm.dunavnet.eu/";
    public static final int HTTP_POST_REQUEST = 1;
    public static final int HTTP_GET_REQUEST = 2;
    private ResponseHandler mActivity;
    private String mPath;
    private String mCodeOrToken;
    private int mRequestType;
    public PrintWriter out;
    public Socket clientSocket;
    public BufferedReader rd;
    public ProgressDialog progress;

    public LoginSiginTask(Activity activity, String path, String codeOrToken, int requestType) {
        mActivity = (ResponseHandler) activity;
        mPath = path;
        mCodeOrToken = codeOrToken;
        mRequestType = requestType;
    }

    protected void onPreExecute() {
        String message = "";
        if(mRequestType == HTTP_POST_REQUEST)
            message  = "Validating username/pass";
        if(mRequestType == HTTP_GET_REQUEST)
            message = "Validating access token";
        progress = ProgressDialog.show((Context) mActivity,"Validating credentials", message);
    }

    protected String doInBackground(Void... arg0) {

        HttpResponse response = null;
        String responseBody = "";

        HttpClient httpClient = new DefaultHttpClient();

        try {

            if(mRequestType == HTTP_POST_REQUEST){
                HttpPost httpRequest = getPostRequest();
                response = httpClient.execute(httpRequest);
            }
            if(mRequestType == HTTP_GET_REQUEST){
                HttpGet httpRequest = getGetRequest();
                response = httpClient.execute(httpRequest);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            }
            System.out.println("Http Post Response: " + responseBody);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        if (responseBody != null && responseBody != "") {
            return responseBody;
        }

        return "";

    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        mActivity.onResponseReceived(result);

    }

    private String getAuth() {
        String client_id = "e26d50fe443e49b584c3e0ffb713c950";
        String client_secret = "556974ae53414ed2ab1ade3c8640b8a4";
        String auth = client_id + ":" + client_secret;
        String enc = Base64.encodeToString(auth.getBytes(), Base64.URL_SAFE
                | Base64.NO_WRAP);

        return enc;
    }

    public HttpPost getPostRequest() throws UnsupportedEncodingException{
        HttpPost httpPost = new HttpPost(SYNC_URL + mPath);
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("grant_type",
                "authorization_code"));
        nameValuePair.add(new BasicNameValuePair("code", mCodeOrToken));
        nameValuePair.add(new BasicNameValuePair("redirect_uri",
                "http://localhost/Callback"));

        StringEntity se = new StringEntity(
                "grant_type=authorization_code&code=" + mCodeOrToken
                        + "&redirect_uri=http://localhost/Callback");
        // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        httpPost.setEntity(se);
        httpPost.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded");
        httpPost.addHeader("Authorization", "Basic " + getAuth());
        return httpPost;
    }

    public HttpGet getGetRequest() throws UnsupportedEncodingException{
        HttpGet httpGet = new HttpGet(SYNC_URL + mPath + mCodeOrToken);
        return httpGet;
    }
}