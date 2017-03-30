package com.dis.fiademo;

import com.dis.fiademo.db.DatabaseHandler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class SettingsActivity extends Activity{
    
    Button finish;
    EditText ipv6_et;
    EditText ipv4_et;
    EditText loc_check;
    RadioButton ipv6_rb;
    RadioButton ipv4_rb;
    DatabaseHandler db;
    String ipv6;
    String ipv4;
    private SharedPreferences prefs;
    String fourorsix;
    String pom;
    CheckBox cb;
    int checkSeconds;
    int isChecked;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);
	
	prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
	db = new DatabaseHandler(this);
	
	String pomipv4 = "";
	String pomipv6 = "";
	try {
	    pomipv4 = db.getIpv4Address();
	    pomipv6 = db.getIpv6Address();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	fourorsix = prefs.getString("ip","");
	checkSeconds = prefs.getInt("sec",10);
	isChecked = prefs.getInt("checked",1);
	
	
	finish = (Button) findViewById(R.id.finish_button);
	ipv6_et = (EditText) findViewById(R.id.ipv6_edit_text);
	ipv4_et = (EditText) findViewById(R.id.ipv4_edit_text);
	loc_check = (EditText) findViewById(R.id.locCheck);
	ipv6_rb = (RadioButton) findViewById(R.id.ipv6_radioButton);
	ipv4_rb = (RadioButton) findViewById(R.id.ipv4_radioButton);
	cb = (CheckBox) findViewById(R.id.checkBox1);
	
	cb.setChecked(false);
	if(isChecked == 1)
	    cb.setChecked(true);
	
	loc_check.setText(String.valueOf(checkSeconds));
	
	if(pomipv4.equals("") && pomipv6.equals(""))
	    db.addIpAddress("cb.distantaccess.com", "2001:620:607:3b10::2");
	else{
	    ipv6_et.setText(pomipv6);
	    ipv4_et.setText(pomipv4);
	}
	
	if(fourorsix.equals("4")){
	    ipv4_rb.setChecked(true);
	    ipv6_rb.setChecked(false);
	}else{
	    ipv4_rb.setChecked(false);
	    ipv6_rb.setChecked(true);
	}
	
	ipv6 = "";
	ipv4 = "";
	
	ipv6 = ipv6_et.getText().toString();
	ipv4 = ipv4_et.getText().toString();
	
	if(ipv6.equals(""))
	    ipv6 = "2001:620:607:3b10::2";
	
	if(ipv4.equals(""))
	    ipv4 = "cb.distantaccess.com";
	
	ipv4_rb.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		ipv4_rb.setChecked(true);
		    ipv6_rb.setChecked(false);
		
	    }
	});
	
	ipv6_rb.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		ipv4_rb.setChecked(false);
		ipv6_rb.setChecked(true);
		
	    }
	});
	
	finish.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		ipv6 = ipv6_et.getText().toString();
		ipv4 = ipv4_et.getText().toString();
		pom = "6";
		if(ipv4_rb.isChecked())
		    pom = "4";
		db.updateIpv4Address(ipv4);
		db.updateIpv6Address(ipv6);
		prefs.edit().putString("ip", pom).commit();
		prefs.edit().putInt("sec", Integer.parseInt(loc_check.getText().toString())).commit();
		isChecked = 0;
		if(cb.isChecked())
		    isChecked = 1;
		prefs.edit().putInt("checked", isChecked).commit();
		finish();
	    }
	});
	
	
	
    }

}
