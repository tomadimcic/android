package com.iotwear.wear.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.model.ICControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.SendSingleControlData;
import com.iotwear.wear.util.Constants;

public class ICControlActivity extends BaseActivity implements OnClickListener{
    
    ICControl controller;
    Button progUp, progDown, volUp, volDown, on, off, middle, six, seven, ten;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View content = LayoutInflater.from(this).inflate(
				R.layout.ir_tv_layout, null);
		contentFrame.addView(content);

		controller = (ICControl) getIntent().getSerializableExtra(
				Constants.EXTRA_CONTROLLER);
		
		progUp = (Button) findViewById(R.id.progUp);
		progDown = (Button) findViewById(R.id.progDown);
		volUp = (Button) findViewById(R.id.volUp);
		volDown = (Button) findViewById(R.id.volDown);
		middle = (Button) findViewById(R.id.middle_btn);
		six = (Button) findViewById(R.id.button6);
		seven = (Button) findViewById(R.id.button7);
		on = (Button) findViewById(R.id.OnBtn);
		off = (Button) findViewById(R.id.OffBtn);
		ten = (Button) findViewById(R.id.button10);
		
		progUp.setOnClickListener(this);
		progDown.setOnClickListener(this);
		volUp.setOnClickListener(this);
		volDown.setOnClickListener(this);
		middle.setOnClickListener(this);
		six.setOnClickListener(this);
		seven.setOnClickListener(this);
		on.setOnClickListener(this);
		off.setOnClickListener(this);
		ten.setOnClickListener(this);
	}

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this, "Device",
		Mode.SLIDING);
    }

    @Override
    public void onClick(View v) {
	int value = 0;
	if(v == progUp){
	    value = 0;
	    //progUp.setBackgroundResource(R.drawable.roletne_strelica_na_gore_pritisnuto);
	}
	if(v == progDown){
	    value = 1;
	}
	if(v == volUp){
	    value = 2;
	}
	if(v == volDown){
	    value = 3;
	}
	if(v == middle){
	    value = 4;
	}
	if(v == six){
	    value = 5;
	}
	if(v == seven){
	    value = 6;
	}
	if(v == on){
	    value = 7;
	}
	if(v == off){
	    value = 8;
	}
	if(v == ten){
	    value = 9;
	}
	controller.setValue(value);
	new SendSingleControlData(this, controller).execute();
	
    }

}
