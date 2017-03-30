package com.dis.fiademo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dis.fiademo.db.DatabaseHandler;

public class LoginActivity extends Activity {

    protected int splashTime = 100;
    DatabaseHandler db;
    boolean prvoPokretanje;
    boolean isFinished;
    AlertDialog.Builder builder;
    String ipAddress;
    String user;;
    private SharedPreferences prefs;
    EditText username;
    Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);

	username = (EditText) findViewById(R.id.username_edit_text);
	login = (Button) findViewById(R.id.login_button);
	db = new DatabaseHandler(this);
	prefs = PreferenceManager
		.getDefaultSharedPreferences(LoginActivity.this);

	user = prefs.getString("user", "");

	if (!user.equals(""))
	    username.setText(user);

	login.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		if(!username.getText().toString().equals("")){
		    prefs.edit().putString("user", username.getText().toString()).commit();
		    Intent i = new Intent(LoginActivity.this, SplashScreenActivity.class);
		    i.putExtra("username", username.getText().toString());
		    startActivity(i);
		    finish();
		}else
		    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
		
	    }
	});
	

    }
}
