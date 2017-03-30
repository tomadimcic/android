package dunavnet.com.mojbac.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.interfaces.ResponseHandler;

/**
 * Created by Tomek on 26.3.2016.
 */
public class ForgotTask extends AsyncTask<Void, Void, String> {

    private static final String URL = "http://idm-dnet.dunavnet.eu/webapi/api/Account/";
    private static final String CATEGORY_PATH = "ForgotPassword";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final String CONTENT_TYPE = "Content-Type";

    private ProgressDialog progressDialog;
    private Context mContext;
    ResponseHandler mHandler;
    private String mEmail;

    public ForgotTask(Context context, String email){
        mContext = context;
        mHandler = (ResponseHandler) mContext;
        mEmail = email;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mContext, null, mContext.getResources().getString(R.string.contacting_server), true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ForgotTask.this.cancel(true);
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
            String origin = "android";
            requestJson = "{\"email\":\""+ mEmail +"\"," +
                    "\"RequestOrigin\":\"android\"}";
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println(requestJson);
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
                responseContent = EntityUtils.toString(entity, "UTF8");
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
