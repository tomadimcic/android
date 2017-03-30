package dunavnet.com.mojbac.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.interfaces.ResponseHandler;
import dunavnet.com.mojbac.tasks.VerifyCodeTask;
import dunavnet.com.mojbac.util.SettingsConstants;
import dunavnet.com.mojbac.util.Util;

public class SplashActivity extends Activity implements ResponseHandler {

    private SharedPreferences prefs;
    int firstTime = 1;
    String code;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        firstTime = prefs.getInt("firstTime", 1);
        email = prefs.getString("email", "");

        Handler handler = new Handler();
        handler.postDelayed(getRunnableStartApp(), SettingsConstants.SPLASH_DISPLAY_LENGTH);

    }

    private Runnable getRunnableStartApp() {
        Runnable runnable = new Runnable() {
            public void run() {
                if(firstTime == 0  || firstTime == 1) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivityNew.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    String q = "";
                    try{
                        q = SplashActivity.this.getIntent().getData().getQuery();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (q.contains("code")) {
                        if (firstTime == 2) {
                            Uri data = SplashActivity.this.getIntent().getData();
                            code = data.getQuery();
                            System.out.println(code);
                            new VerifyCodeTask(SplashActivity.this, email, code.split("=")[1]).execute();
                        }

                        if (firstTime == 3) {
                            Uri data = SplashActivity.this.getIntent().getData();
                            code = data.getQuery();
                            System.out.println(code);
                            Intent intent = new Intent(SplashActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("code", code.split("=")[1]);
                            startActivity(intent);
                            finish();

                        }
                    }
                    else{
                        Intent intent = new Intent(SplashActivity.this, LoginActivityNew.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        return runnable;
    }

    @Override
    public void onResponseReceived(String response) {
        //{"error":false,"message":"mail confirmed"}
        //{"error":true,"message":"error, mail not confirmed"}

        String message = "";
        String error = "";

        JSONObject jo;
        try {
            jo = new JSONObject(response);
            message = jo.getString("message");
            error = jo.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(error.equals("true")) {
            if(message.equals("error, mail not confirmed"))
                message = getResources().getString(R.string.error_email_not_confirmed);
        }
        else{
            if(message.equals("mail confirmed"))
                message = getResources().getString(R.string.mail_confirmed);
            prefs.edit().putInt("firstTime", 0).commit();
            prefs.edit().putInt("resend", 0).commit();
        }

        Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(SplashActivity.this, LoginActivityNew.class);
        startActivity(intent);
        finish();
    }
}
