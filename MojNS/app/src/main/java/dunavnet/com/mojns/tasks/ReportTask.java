package dunavnet.com.mojns.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dunavnet.com.mojns.R;
import dunavnet.com.mojns.interfaces.ResponseHandler;
import dunavnet.com.mojns.model.Event;
import dunavnet.com.mojns.util.SettingsConstants;

/**
 * Created by Tomek on 6.9.2015.
 */
public class ReportTask extends AsyncTask<Void, Void, String> {

    //public static String SERVICE_PUBLISH_URL = "http://services.nsinfo.co.rs/TEST/Api/v1/CommunityPoliceSubmitIssueMojNS";
    private static final String URL = "http://services.nsinfo.co.rs/rest/api/v1/";
    private static final String CATEGORY_PATH = "CommunityPoliceSubmitIssueMojNS";
    private static final String AUTHORIZATION_TOKEN = "X-Auth-Token";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final String CONTENT_TYPE = "Content-Type";

    private ProgressDialog progressDialog;
    private Context mContext;
    ResponseHandler mHandler;
    private Event mEvent;
    private String mEmail, mName, mSurname, mToken;

    public ReportTask(Context context, String token, Event event, String email, String name, String surname){
        mContext = context;
        mHandler = (ResponseHandler) mContext;
        mEvent = event;
        mEmail = email;
        mName = name;
        mSurname = surname;
        mToken = token;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mContext, null, mContext.getResources().getString(R.string.contacting_server), true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ReportTask.this.cancel(true);
            }

        });
    }

    @Override
    protected String doInBackground(Void... args) {
        int responseCode = -1;
        String responseContent = null;
        String requestJson = "";
        HttpPost request = null;
        //System.out.println(json.toString());
        try {

            request = new HttpPost(URL + CATEGORY_PATH);
            String base64Image = mEvent.getEventData().getImageData();
            String imageFormat = mEvent.getEventData().getImageFormat();
            String type = mEvent.getTitle();
            String gpsx = Double.toString(mEvent.getEventData().getEventLocation().getLatitude());
            String gpsy = Double.toString(mEvent.getEventData().getEventLocation().getLongitude());
            String desc = mEvent.getEventData().getText();
            String address = mEvent.getEventData().getAddress();
            requestJson = "{\"Name\":\""+mName+"\"," +
                    "\"Surname\":\"" + mSurname + "\"," +
                    "\"IssueCategoryID\":" + type + "," +
                    "\"GPSX\":" + gpsx + "," +
                    "\"GPSY\":" + gpsy + "," +
                    "\"Description\": \"" + desc + "\"," +
                    "\"ProblemAddress\":\"" + address + "\"," +
                    "\"EMail\": \"" + mEmail + "\"," +
                    "\"PhotoExt\": \"" + imageFormat + "\"," +
                    "\"Photo\":\"" + base64Image + "\"}";
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println(requestJson);
        request.addHeader(AUTHORIZATION_TOKEN, mToken);
        request.addHeader(CONTENT_TYPE, "application/json");
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, 60000);
        HttpClient client = new DefaultHttpClient(httpParams);

        try {
            request.setEntity(new StringEntity(requestJson));

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            responseCode = response.getStatusLine().getStatusCode();
            if(entity!=null) {
                responseContent = EntityUtils.toString(entity,"UTF8");
                System.out.println(responseContent);
            }

        }catch(IOException e) {
            return "";
        }
        return responseContent;
    }

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String response) {
        progressDialog.dismiss();
        mHandler.onResponseReceived(response);
    }


}
