package dunavnet.com.mojbac.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.interfaces.ResponseHandler;
import dunavnet.com.mojbac.tasks.CheckTokenTask;
import dunavnet.com.mojbac.tasks.LoginTask;
import dunavnet.com.mojbac.util.SettingsConstants;

public class LoginActivityNew extends Activity implements ResponseHandler{

    String user;
    String pass;
    int rememberMe;
    int signed;
    private SharedPreferences prefs;
    EditText username;
    EditText password;
    Button login;
    TextView signin, change, forgot, resend, about;
    CheckBox remember;
    CheckBox staySigned;
    int flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        username = (EditText) findViewById(R.id.username_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        login = (Button) findViewById(R.id.login_button);
        signin = (TextView) findViewById(R.id.signin_button);
        about = (TextView) findViewById(R.id.about_button);
        change = (TextView) findViewById(R.id.change_button);
        forgot = (TextView) findViewById(R.id.forgot_button);
        resend = (TextView) findViewById(R.id.resend_button);
        remember = (CheckBox) findViewById(R.id.checkBox1);
        staySigned = (CheckBox) findViewById(R.id.checkBox2);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        int res = prefs.getInt("resend", 2);

        if (res == 0 || res == 2)
            resend.setVisibility(View.GONE);

        user = prefs.getString("user", "");
        pass = prefs.getString("pass", "");
        rememberMe = prefs.getInt("remember", 10);
        signed = prefs.getInt("signed", 10);

        if(rememberMe == 0)
            remember.setChecked(false);
        if(signed == 0)
            staySigned.setChecked(false);

        if(rememberMe == 1){
            if(signed == 1 && !user.equals("") && !pass.equals(""))
                loginWithCheck();
            else{
                if (!user.equals(""))
                    username.setText(user);
                if (!pass.equals(""))
                    password.setText(pass);

            }
        }



        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){
                    rememberMe = 0;
                    signed = 0;
                    if(remember.isChecked())
                        rememberMe = 1;
                    if(staySigned.isChecked())
                        signed = 1;

                    prefs.edit().putString("user", username.getText().toString()).commit();
                    prefs.edit().putString("pass", password.getText().toString()).commit();
                    prefs.edit().putInt("remember", rememberMe).commit();
                    prefs.edit().putInt("signed", signed).commit();

                    login();
                }else
                    Toast.makeText(LoginActivityNew.this, getResources().getString(R.string.error_empty_user_pass), Toast.LENGTH_SHORT).show();
            }
        });

        signin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                signin();

            }
        });

        about.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popup();

            }
        });

        change.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                change();

            }
        });

        forgot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                forgot();

            }
        });

        resend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                    resend();
            }
        });


    }

    public void login(){

        /*
        {
          "Username": "knezevic.petar@gmail.com",
          "Password": "password"
         }
         */

        /*
        {"Token": "219gEkFcQYeWVsZcZS2ClChV2NuNnIif-B72GkdSa2m9adzyxObC1tsk86Vfx-E5cqMq8GNFr-yB9MCcyF8_JLUnLONVrwN7NkfpQVZD-g6axlBjFD2gd-egNjzhSgHsvzrV1LuF9yYrwRQA_Ylis2YlwbKmPLXSiJatoRFZtNoLXwdF8qcGgV-H3pxQyqah_SpVDKOVTYgl_NtXwNp_eH1sf6jn9RYuB3hDZz2L_6TxGnRceheavtG7bFHH7Uog0AlEsWMY-Hn_lG_RCV-3tBkv92ph_g2CUYbVBbgVDqO6W_eY2nTUWwLxXEXWbRWnxMIDRc7Z3m--ezMBNA7ZFKVyNh_vbM6drDbWsCNNhLA",
         "UserId": "984dd20c-1766-4747-8afb-1acdb6c403ea",
          "RoleId": null}
        */
        flag = 0;
        new LoginTask(LoginActivityNew.this, username.getText().toString(), password.getText().toString()).execute();
        //Intent i = new Intent(LoginActivityNew.this, MainActivityList.class);
        //startActivity(i);
        //finish();
    }

    public void loginWithCheck(){
        /*
        {
          "accessToken": "nTodV50w9P0a1siN_gFrdRFGgKnFv8OMQZ6KH0aMWQv91iMT7C9vWFScIYzeGX6KEUM4JYpOWc5d1I1yCViSVYvfSJjxJ-87V7daRYBLiRscg-LOSE2RViH43E-69HPjBXFhuf8sD6TPK7WgNqbFq5HcpdasIN2rkM2BAGGro5T1IeCHx8tIkUFADk2j_hZATEN7oizPyn5pMUTf8Gm-7oeZA3yUjVVjf5NHVAt3Z_3-qgGEpstXEjYVLhXs-I80hIUmfDt9DKkBhkiWCr76pu-CqMbLIX7i-0zamMVHGNl6zgsMtMWOcOErfIVtvgpXNrcJcjuZ-Q4hNa9FuH0dQclNK7Wce8BafH4tD82LxI8"
        }
        */
        flag = 1;
        String token = prefs.getString("access_token", "");
        new CheckTokenTask(LoginActivityNew.this, token).execute();
    }

    public void signin(){
        Intent i = new Intent(LoginActivityNew.this, SigninActivity.class);
        startActivity(i);
        finish();
    }

    public void popup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivityNew.this);

        builder.setMessage(getResources().getString(R.string.about_txt))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setTitle(getResources().getString(R.string.about));
        builder.create();
        builder.show();
    }

    public void change(){
        String token = prefs.getString("access_token","");
        if(!token.equals("")) {
            Intent i = new Intent(LoginActivityNew.this, ChangeActivity.class);
            startActivity(i);
            finish();
        }else
            Toast.makeText(LoginActivityNew.this, getResources().getString(R.string.not_logged), Toast.LENGTH_SHORT).show();
    }

    public void forgot(){
        Intent i = new Intent(LoginActivityNew.this, ForgotActivity.class);
        startActivity(i);
        finish();
    }

    public void resend(){
        int res = prefs.getInt("resend", 2);
        if(res == 1) {
            Intent i = new Intent(LoginActivityNew.this, ResendActivity.class);
            startActivity(i);
            finish();
        }
        if(res == 0)
            Toast.makeText(LoginActivityNew.this, getResources().getString(R.string.mail_already_confirmed), Toast.LENGTH_SHORT).show();
        if(res == 2)
            Toast.makeText(LoginActivityNew.this, getResources().getString(R.string.not_registered), Toast.LENGTH_SHORT).show();


    }


    public void startActivity(String contents){
        prefs.edit().putString("user", contents.split("&")[1].split("=")[1]).commit();
        prefs.edit().putString("pass", contents.split("&")[2].split("=")[1]).commit();
        prefs.edit().putInt("remember", 1).commit();
        prefs.edit().putInt("signed", 1).commit();
        login();
    }

    @Override
    public void onResponseReceived(String response) {
        //ako je token validan
        //{"email": "knezevic.petar@gmail.com}

        String email = "";
        String token = "";
        String message = "";

        JSONObject jo;
        try {
            jo = new JSONObject(response);

            if(flag == 1){
                email = jo.getString("email");
                if(email.equals(user)) {
                    Intent i = new Intent(LoginActivityNew.this, MainActivityList.class);
                    startActivity(i);
                    finish();
                }
            }
            if(flag == 0){
                String error = jo.getString("error");
                //{"error":true,"message":"no user found"}
                //{"error":true,"message":"email not confirmed"}
                if(error.equals("true")) {
                    if(jo.getString("message").equals("no user found"))
                        message = getResources().getString(R.string.error_no_user);
                    if(jo.getString("message").equals("email not confirmed"))
                        message = getResources().getString(R.string.error_email_not_confirmed);
                    Toast.makeText(LoginActivityNew.this, message, Toast.LENGTH_SHORT).show();
                }
                else{
                    token = jo.getString("Token");
                    prefs.edit().putString("access_token", token).commit();
                    prefs.edit().putString("email", username.getText().toString()).commit();
                    Intent i = new Intent(LoginActivityNew.this, MainActivityList.class);
                    startActivity(i);
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivityNew.this, getResources().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
        }

        //{"Token":"OPUNChmfBCqfvK-lFzMgPkoj2KtJet-1UfsbwngfyiMJmKBj6i3lv4v0rb6FauZb6VlhmACGgJYdlfQXtnZLZf9BvlgMeP8GoJFrfjIln4NhN-ZbFBEGr9xPUF7_6ho5O85w2hHItqIck0w0NuJtyqKjrSKORtv5BVFoS4udpm4vo_Qvbi_t1EM5O1L8PAmbzmLmB3XqSdLXmZHnGbvUzSCVfF8FgixgPBV8p4mUmY4c5pRp52vi1Lhz203_zJ4m2LgQCU-nYeynAoz3LecNySbwcnnA2LqlfclhCbKKT0uQNY4AZfGEhYctycTnQwT8kvOhs4a4YbtyD9GWSljajiclG7BHIHcv9DLaEYaDkkQ","UserId":"810e4382-64d3-4473-8757-86497f3d0d94","RoleId":null}

    }
}
