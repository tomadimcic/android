package dunavnet.com.mojbac.tasks;

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

import java.util.ArrayList;
import java.util.List;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.interfaces.ResponseHandler;
import dunavnet.com.mojbac.util.SettingsConstants;

public class LoadEventsTask extends AsyncTask<Void, Void, String> {

    private static final String AUTHORIZATION_TOKEN = "X-Auth-Token";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final String URL = "http://clips.dunavnet.eu/api/ComunalPolice/";
    private static final String CATEGORY_PATH = "CommunityPoliceGetProblemForEmail";

    private ProgressDialog progressDialog;
    private Context mContext;
    ResponseHandler mHandler;

    public LoadEventsTask(Context context, ResponseHandler handler){
        mContext = context;
        mHandler = handler;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mContext, null, mContext.getResources().getString(R.string.contacting_server), true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LoadEventsTask.this.cancel(true);
            }

        });
    }

    @Override
    protected String doInBackground(Void... args) {
        SharedPreferences prefs = mContext.getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);	//Unican - reading the deviceUUID from the sharedpreferences.
        String responseContent = "";
        String requestJson = "";
        try {
            String email = prefs.getString("email", "");
            if(email.equals("")){
                Toast.makeText(mContext, "No email", Toast.LENGTH_SHORT);
            }
            else{

                requestJson = "{\"Email\":\""+ email +"\"}";

                int responseCode = -1;

                HttpPost request = new HttpPost(URL + CATEGORY_PATH);
                //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " + prefs.getString("access_token", ""));

                request.addHeader(AUTHORIZATION_TOKEN, prefs.getString("access_token", ""));
                request.addHeader("Content-Type", "application/json");
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpParams, 60000);
                HttpClient client = new DefaultHttpClient(httpParams);

                request.setEntity(new StringEntity(requestJson));
                //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                //nameValuePairs.add(new BasicNameValuePair("Email", email));

                //request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                responseCode = response.getStatusLine().getStatusCode();
                if(entity!=null) {
                    responseContent = EntityUtils.toString(entity, "UTF8");
                    System.out.println("--LoadEventTask--");
                    System.out.println(responseContent);
                }

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
    protected void onPostExecute(String events) {
        progressDialog.dismiss();
        mHandler.onResponseReceived(events);
    }


}
