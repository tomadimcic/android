package dunavnet.com.mojiodzaci.activities;

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

import dunavnet.com.mojiodzaci.R;
import dunavnet.com.mojiodzaci.interfaces.ResponseHandler;
import dunavnet.com.mojiodzaci.tasks.ChangeTask;
import dunavnet.com.mojiodzaci.util.SettingsConstants;

/**
 * Created by Tomek on 25.3.2016.
 */
public class ChangeActivity extends Activity implements ResponseHandler {
    private SharedPreferences prefs;
    EditText old;
    EditText password, confirmPassword;
    Button change;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        old = (EditText) findViewById(R.id.old_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_edit_text);
        change = (Button) findViewById(R.id.change_button);

        token = prefs.getString("access_token","");

        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!old.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    if(password.getText().toString().length() > 5) {
                        if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                            new ChangeTask(ChangeActivity.this, old.getText().toString(), password.getText().toString(), confirmPassword.getText().toString(), token).execute();
                        }else
                            Toast.makeText(ChangeActivity.this, getResources().getString(R.string.error_pass_confirmed), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(ChangeActivity.this, getResources().getString(R.string.error_pass_lenght), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ChangeActivity.this, getResources().getString(R.string.error_empty_user_pass), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void finishActivity(){
        prefs.edit().putString("user", "").commit();
        prefs.edit().putString("pass", "").commit();
        prefs.edit().putInt("remember", 0).commit();
        prefs.edit().putInt("signed", 0).commit();
        Intent i = new Intent(ChangeActivity.this, LoginActivityNew.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResponseReceived(String response) {

        System.out.println(response);

        //{"error":false,"message":"password change ok"}

        String message = "";
        String error = "";

        JSONObject jo;
        try {
            jo = new JSONObject(response);
            message = jo.getString("message");
            error = jo.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ChangeActivity.this, getResources().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeActivity.this);

        if(!error.equals("") && error.equals("false")){

            message  = getResources().getString(R.string.succesfully_change_message);

            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finishActivity();
                        }
                    })
                    .setTitle(getResources().getString(R.string.succesfully_changed_title));
            builder.create();
            builder.show();
        }

        if(!error.equals("") && error.equals("true")){
            message  = getResources().getString(R.string.error_change_message);
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
