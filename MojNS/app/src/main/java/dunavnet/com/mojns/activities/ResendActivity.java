package dunavnet.com.mojns.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import dunavnet.com.mojns.tasks.ForgotTask;
import dunavnet.com.mojns.tasks.ResendTask;
import dunavnet.com.mojns.util.SettingsConstants;

/**
 * Created by Tomek on 30.3.2016.
 */
public class ResendActivity extends Activity implements ResponseHandler {
    private SharedPreferences prefs;
    EditText email;
    Button send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        email = (EditText) findViewById(R.id.email_edit_text);
        send = (Button) findViewById(R.id.forgot_button);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!email.getText().toString().equals(""))
                    new ResendTask(ResendActivity.this, email.getText().toString()).execute();
                else
                    Toast.makeText(ResendActivity.this, getResources().getString(R.string.error_empty_email), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void finishActivity(){
        prefs.edit().putString("user", "").commit();
        prefs.edit().putString("pass", "").commit();
        prefs.edit().putInt("remember", 0).commit();
        prefs.edit().putInt("signed", 0).commit();
        //Intent i = new Intent(SigninActivity.this, SigninActivity.class);
        //startActivity(i);
        finish();
    }

    @Override
    public void onResponseReceived(String response) {

        //{"message": "Please check your mail for code so you can reset your password!"}

        String message = "";
        String error = "";
        //prefs.edit().putInt("firstTime", 3).commit();

        JSONObject jo;
        try {
            jo = new JSONObject(response);
            message = jo.getString("message");
            error = jo.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ResendActivity.this, getResources().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ResendActivity.this);

        if(!error.equals("") && error.equals("false")){
            prefs.edit().putString("email", email.getText().toString()).commit();
            prefs.edit().putInt("firstTime", 2).commit();
            message = getResources().getString(R.string.succesfully_signin_message);
            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishActivity();
                            dialog.cancel();
                        }
                    })
                    .setTitle(getResources().getString(R.string.forgot_title));
            builder.create();
            builder.show();
        }

        if(!error.equals("") && error.equals("true")){
            //{"error":true,"message":"email not found"}
            if(message.equals("email not found"))
                message = getResources().getString(R.string.error_email_not_found);
            if(message.equals("email already confirmed"))
                message = getResources().getString(R.string.mail_already_confirmed);
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
