package dunavnet.com.mojiodzaci.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import dunavnet.com.mojiodzaci.R;
import dunavnet.com.mojiodzaci.interfaces.ResponseHandler;
import dunavnet.com.mojiodzaci.tasks.RegisterTask;
import dunavnet.com.mojiodzaci.util.SettingsConstants;

/**
 * Created by Tomek on 25.3.2016.
 */
public class SigninActivity extends Activity implements ResponseHandler{

    private SharedPreferences prefs;
    EditText username;
    EditText password, confirmPassword;
    Button login;
    CheckBox terms;
    TextView termsAndConditions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        username = (EditText) findViewById(R.id.username_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_edit_text);
        login = (Button) findViewById(R.id.login_button);
        terms = (CheckBox) findViewById(R.id.checkBox);
        termsAndConditions = (TextView) findViewById(R.id.terms_and_cond_txt);

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    if(password.getText().toString().length() > 5) {
                        if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                            if(terms.isChecked())
                                new RegisterTask(SigninActivity.this, username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString()).execute();
                            else
                                Toast.makeText(SigninActivity.this, getResources().getString(R.string.error_terms), Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(SigninActivity.this, getResources().getString(R.string.error_pass_confirmed), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(SigninActivity.this, getResources().getString(R.string.error_pass_lenght), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(SigninActivity.this, getResources().getString(R.string.error_empty_user_pass), Toast.LENGTH_SHORT).show();
            }
        });

        termsAndConditions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popup();

            }
        });

    }

    public void register(){
        prefs.edit().putString("user", "").commit();
        prefs.edit().putString("pass", "").commit();
        prefs.edit().putInt("remember", 0).commit();
        prefs.edit().putInt("signed", 0).commit();
        //Intent i = new Intent(SigninActivity.this, SigninActivity.class);
        //startActivity(i);
        finish();
    }

    public void popup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);

        builder.setMessage(getResources().getString(R.string.terms_txt))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setTitle(getResources().getString(R.string.terms_and_conditions));
        builder.create();
        builder.show();
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
            Toast.makeText(SigninActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);

        if(!error.equals("") && error.equals("false")){
            //{"message": "Please check your mail for code so you can activate your account!",
            //        "error": false}

            prefs.edit().putString("email", username.getText().toString()).commit();
            prefs.edit().putInt("firstTime", 2).commit();
            prefs.edit().putInt("resend", 1).commit();

            message = getResources().getString(R.string.succesfully_signin_message);

            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            register();
                            dialog.cancel();
                        }
                    })
                    .setTitle(getResources().getString(R.string.succesfully_signin_title));
            builder.create();
            builder.show();
        }

        if(!error.equals("") && error.equals("true")){
            //{"error":true,"message":"email already exists"}
            //{"error":true,"message":"unable to create user"}
            if(message.equals("email already exists"))
                message = getResources().getString(R.string.not_succesfully_signin_message_1);
            if(message.equals("unable to create user"))
                message = getResources().getString(R.string.not_succesfully_signin_message_2);
            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //finish();
                            dialog.cancel();
                        }
                    })
                    .setTitle(getResources().getString(R.string.not_succesfully_signin_title));
            builder.create();
            builder.show();
        }

    }
}
