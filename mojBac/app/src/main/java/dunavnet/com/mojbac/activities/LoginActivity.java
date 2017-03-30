package dunavnet.com.mojbac.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieManager;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.interfaces.ResponseHandler;
import dunavnet.com.mojbac.model.LogData;
import dunavnet.com.mojbac.tasks.LogTask;
import dunavnet.com.mojbac.tasks.LoginSiginTask;
import dunavnet.com.mojbac.util.SettingsConstants;
import dunavnet.com.mojbac.util.Util;

public class LoginActivity extends Activity implements ResponseHandler {

    public static final int HTTP_POST_REQUEST = 1;
    public static final int HTTP_GET_REQUEST = 2;

    public static final String LOGIN_URL = "http://idm.dunavnet.eu/oauth2/authorize/?response_type=code&client_id=e26d50fe443e49b584c3e0ffb713c950&redirect_uri=http://localhost/Callback";
    public int flag = 0;


    private SharedPreferences prefs;
    String user;
    String pass;
    int rememberMe;
    int signed;
    String token;
    WebView webview;
    private int mCurrentRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webview = new WebView(this);
        setContentView(webview);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        token = prefs.getString("access_token", "");

        if (!token.equals(""))
            validate();
        else{
            openLoginForm();
        }

    }

    public void validate() {
        setCurrentRequest(HTTP_GET_REQUEST);
        new LoginSiginTask(LoginActivity.this, "user?access_token=", token, HTTP_GET_REQUEST).execute();
    }

    public void login() {
        sendLogData();
        Intent i = new Intent(LoginActivity.this, BaseMainActivity.class);
        startActivity(i);
        finish();
    }

    public void openLoginForm(){
        webview.clearCache(true);
        webview.clearHistory();
        android.webkit.CookieSyncManager.createInstance(this);
        android.webkit.CookieManager cookieManager=android.webkit.CookieManager.getInstance();
        cookieManager.removeAllCookie();
        String url = LOGIN_URL;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebViewClient(new MyBrowser());
        webview.loadUrl(url);
    }

    @Override
    public void onResponseReceived(String response) {
        System.out.println("Oooooooooooooooooooooooooodgovor");
        System.out.println(response);
        if(mCurrentRequest == HTTP_POST_REQUEST){
            JSONObject json;
            try {
                json = new JSONObject(response);
                if (response.contains("access_token")) {// {"access_token":"m5RzdpubWu6eXk9UEZEduFXPt1yXkL","expires_in": 3600, "token_type": "Bearer","state": "None", "scope":"all_info","refresh_token":"g2cXiBMMtvVOrSSY8UAC6wLy4tB4JG"}
                    prefs.edit().putString("access_token", json.getString("access_token")).commit();
                    token = json.getString("access_token");
                    validate();
                }
                else {// {"error":{"message":"No Consumer found with id: e26d50fe443e49b584c3e0ffb713c950","code":404,"title": "Not Found"}}
                    JSONObject newJson = new JSONObject(json.getString("error"));
                    String message = "";
                    if (newJson.getString("message").contains(
                            "No Consumer found with id"))
                        message = "Consumer not found!";
                    else
                        message = "Token not received";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(mCurrentRequest == HTTP_GET_REQUEST){
            //{"organizations": [], "displayName": "Tomislav1234", "roles": [], "app_id": "e26d50fe443e49b584c3e0ffb713c950", "email": "toma.dimcic@gmail.com", "id": "tomislav1234"}
            JSONObject json;
            try {
                json = new JSONObject(response);
                if (response.contains("organizations")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.welocome) + " " + json.getString("displayName"), Toast.LENGTH_SHORT).show();
                    prefs.edit().putString("email", json.getString("email")).commit();
                    login();
                }
                else {
                    if(flag == 0) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.access_credentials), Toast.LENGTH_SHORT)
                                .show();
                        openLoginForm();
                    }

                }
            } catch (JSONException e) {
                if(response.contains("<p>")){
                    String message = response.split("<p>")[1].split("</p>")[0];
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT)
                            .show();
                }
                finish();
                e.printStackTrace();
            }
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.clearCache(true);
            view.clearHistory();

            System.out.println("tuuuuuuuuuuu");
            System.out.println(url);
            //if (!url.contains("http://idm.dunavnet.eu/oauth2/authorize/")) {
            if (url.contains("http://localhost/Callback?state=None&code=") && flag == 0) {
                flag = 1;
				/*if(url.contains("http://idm.dunavnet.eu/sign_up/") || url.contains("http://idm.dunavnet.eu/password/request/") || url.contains("http://idm.dunavnet.eu/confirmation/") || url.contains("http://idm.dunavnet.eu/auth/login/") || url.contains("http://idm.dunavnet.eu/"))
					view.loadUrl(url);
				else{*/
                // http://localhost/Callback?state=None&code=hxmI6eJ91XMlhavzkBc4fi5OgP1vhz
                System.out.println("Usaoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                String code = url.split("=")[2];
                setCurrentRequest(HTTP_POST_REQUEST);
                new LoginSiginTask(LoginActivity.this, "oauth2/token", code, HTTP_POST_REQUEST)
                        .execute();
                //}
            } else {
                view.loadUrl(url);
            }

            return true;
        }

    }

    public int getCurrentRequest(){
        return mCurrentRequest;
    }

    public void setCurrentRequest(int currentRequest){
        mCurrentRequest = currentRequest;
    }

    public void sendLogData(){
        String email = prefs.getString("email", "");
        LogData log = LogData.createLog(1, "User " + email + " login.", "login()", "NOMp02", Util.getipv4Address(this), "NOMs08");
        System.out.println("+++++++++*************+++++++++++++ " + Util.getipv6Address());
        new LogTask(this, log).execute();
    }

}
