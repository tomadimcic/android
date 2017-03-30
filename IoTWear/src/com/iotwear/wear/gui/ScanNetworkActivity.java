package com.iotwear.wear.gui;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.dialog.CustomizeDialogGetDeviceList;
import com.iotwear.wear.task.GetDeviceListFromMaster;

public class ScanNetworkActivity extends BaseActivity{
    
    CheckBox cb;
    EditText ipadr;
    EditText port1;
    EditText port2;
    EditText port3;
    RelativeLayout portRangeLayout;
    RelativeLayout ipLayout;
    Button scan;
    boolean isItChecked = false;
    ProgressBar progressBar;
    
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View content = LayoutInflater.from(this).inflate(
				R.layout.activity_scan_network, null);
		contentFrame.addView(content);
		
		scan = (Button) findViewById(R.id.scan_btn);
		cb = (CheckBox) findViewById(R.id.searchMultipleCB);
		ipadr = (EditText) findViewById(R.id.ip_et1);
		port1 = (EditText) findViewById(R.id.ip_et2);
		port2 = (EditText) findViewById(R.id.port_et1);
		port3 = (EditText) findViewById(R.id.port_et2);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		
		portRangeLayout = (RelativeLayout) findViewById(R.id.portRange);
		ipLayout = (RelativeLayout) findViewById(R.id.ipRange);
		
		portRangeLayout.setVisibility(View.GONE);
		
		final AnimationSet setIn = new AnimationSet(true);
		
		Animation fadeIn = new AlphaAnimation(0.0F,  1.0F);
		fadeIn.setDuration(1000);
		
		setIn.addAnimation(fadeIn);

		fadeIn = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
		  );
		fadeIn.setDuration(500);
		setIn.addAnimation(fadeIn);
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			isItChecked = isChecked;
			if(isChecked){
			    portRangeLayout.startAnimation(setIn);
			    portRangeLayout.setVisibility(View.VISIBLE);
			    ipLayout.setVisibility(View.GONE);
			}else{
			    ipLayout.startAnimation(setIn);
			    portRangeLayout.setVisibility(View.GONE);
			    ipLayout.setVisibility(View.VISIBLE);
			}
			
			
			
			
		    }
		});
		
		scan.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			if(!isItChecked)
			    getDeviceListFromMaster(ipadr.getText().toString() + ":" + port1.getText().toString());
			else{
			    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				String ip = Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress());
				System.out.println(ip);
				String from = port2.getText().toString();
				String to = port3.getText().toString();
				new GetDeviceListFromMaster(ScanNetworkActivity.this, from, to, ip).execute();
			}
			
		    }
		});
    }
    
    	public void getDeviceListFromMaster(String master){
	
    	    CustomizeDialogGetDeviceList customizeDialog = new CustomizeDialogGetDeviceList(this, master, true);  
            customizeDialog.setTitle("IP address and port");  
            customizeDialog.setMessage("These are the parameters:");
            customizeDialog.show();
	    
	    
	}

    @Override
	public ActionBarHandler createActionBarHandler() {
		return ActionBarHandlerFactory.createActionBarHandler(this,
				"Scan network", Mode.NORMAL);
	}

}
