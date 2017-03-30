package dunavnet.com.mojns.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dunavnet.com.mojns.R;
import dunavnet.com.mojns.interfaces.ResponseHandler;
import dunavnet.com.mojns.tasks.RegisterTask;
import dunavnet.com.mojns.tasks.ResetTask;
import dunavnet.com.mojns.util.SettingsConstants;

/**
 * Created by Tomek on 26.3.2016.
 */
public class ResetPasswordActivity extends Activity implements ResponseHandler {

    private SharedPreferences prefs;
    EditText username;
    EditText password, confirmPassword;
    Button reset;
    String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        Intent intent = getIntent();

        code = intent.getStringExtra("code");

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        username = (EditText) findViewById(R.id.username_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_edit_text);
        reset = (Button) findViewById(R.id.reset_button);

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    if(password.getText().toString().length() > 5) {
                        if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                            new ResetTask(ResetPasswordActivity.this, username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString(), code).execute();
                        }else
                            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.error_pass_confirmed), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.error_pass_lenght), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.error_empty_user_pass), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void finishActivity(){
        Intent i = new Intent(ResetPasswordActivity.this, LoginActivityNew.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResponseReceived(String response) {

        String message = "";
        String error = "";

        JSONObject jo;
        try {
            jo = new JSONObject(response);
            message = jo.getString("message");
            error = jo.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);

        if(!error.equals("") && error.equals("false")){
            /*
            {
                "error": false,
                "message": "password updated succesfully"
             }
             */

            prefs.edit().putInt("firstTime", 0).commit();

            message = getResources().getString(R.string.succesfully_reset_message);

            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishActivity();
                            dialog.cancel();
                        }
                    })
                    .setTitle(getResources().getString(R.string.succesfully_reset_title));
            builder.create();
            builder.show();
        }

        if(!error.equals("") && error.equals("true")){
            //{"Username":"tomislav.dimcic@dunavnet.eu","Code":"ziAiVN","Password":"password","ConfirmPassword":"password"}
            //{"error":true,"message":"bad model state"}
            message = getResources().getString(R.string.error_reset_message);
            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //finish();
                            dialog.cancel();
                        }
                    })
                    .setTitle(getResources().getString(R.string.error));
            builder.create();
            builder.show();
        }

    }
}
