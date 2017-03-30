package dunavnet.com.mojiodzaci.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import dunavnet.com.mojiodzaci.R;
import dunavnet.com.mojiodzaci.interfaces.ResponseHandler;
import dunavnet.com.mojiodzaci.util.SettingsConstants;

/**
 * Created by Tomek on 18.11.2015.
 */
public class LoadTopics extends AsyncTask<Void, Void, String> {

    private static final String AUTHORIZATION_TOKEN = "X-Auth-Token";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final String URL = "http://clips.dunavnet.eu/api/ComunalPolice/";
    private static final String CATEGORY_PATH = "CommunityPoliceOdzaciRetriveTopics";

    private ProgressDialog progressDialog;
    private Context mContext;
    ResponseHandler mHandler;

    public LoadTopics(Context context){
        mContext = context;
        mHandler = (ResponseHandler) mContext;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mContext, null, mContext.getResources().getString(R.string.contacting_server), true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LoadTopics.this.cancel(true);
            }

        });
    }

    @Override
    protected String doInBackground(Void... args) {
        SharedPreferences prefs = mContext.getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);	//Unican - reading the deviceUUID from the sharedpreferences.
        String responseContent = "";
        try {

            int responseCode = -1;

            HttpGet request = new HttpGet(URL + CATEGORY_PATH);

            request.addHeader(AUTHORIZATION_TOKEN, prefs.getString("access_token", ""));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, 60000);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            responseCode = response.getStatusLine().getStatusCode();
            if(entity!=null) {
                responseContent = EntityUtils.toString(entity, "UTF8");
                System.out.println("shebfvjsfjzdsjfdfhbjlzdsbfh+++++++++++++++++++++++++++++++++++");
                System.out.println(responseContent);
                if(responseContent.contains("Error"))
                    responseContent = "";
            }
        }catch(Exception e) {
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
    protected void onPostExecute(String categories) {
        progressDialog.dismiss();
        mHandler.onResponseReceived(categories);
    }


}