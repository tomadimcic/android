package com.iotwear.wear.gui;

import org.apache.commons.io.HexDump;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.LampControl;
import com.iotwear.wear.task.SendSingleControlData;
import com.iotwear.wear.task.SendToLamp;
import com.iotwear.wear.task.led.SetColorTask;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;
import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.SVBar;
import com.larswerkman.colorpicker.ColorPicker.OnColorChangedListener;

public class ColorPickerForLampActivity extends BaseActivity implements
	OnColorChangedListener, OnClickListener, ValueSendingInterface {

    private int currentColor;
    private boolean isSendingColor;
    private LampControl controller;

    private boolean isEditMode;

    private Button save, delete;
    SetColorTask skt;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	int intentColor = getIntent().getIntExtra(Constants.EXTRA_COLOR,
		-1692929292);
	if (intentColor == -1692929292) {
	    currentColor = Color.RED;
	    isEditMode = false;
	} else {
	    currentColor = intentColor;
	    isEditMode = true;
	}
	controller = (LampControl) getIntent().getSerializableExtra(
		Constants.EXTRA_CONTROLLER);
	if (controller == null) {
	    Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
	    Intent i = new Intent(this, DeviceListActivity.class);
	    startActivity(i);
	    finish();
	} else {
	    getActionBarHandler().setTitle(controller.getHostDevice().getName());
	}
	
	//skt = new SetColorTask(this, controller);
	startTime = System.currentTimeMillis();
	
	initUI();

    }

    public void initUI() {
	View content = View.inflate(this, R.layout.activity_color_picker, null);
	contentFrame.addView(content);

	final ColorPicker picker = (ColorPicker) content
		.findViewById(R.id.picker);
	SVBar svBar = (SVBar) content.findViewById(R.id.svbar);
	picker.addSVBar(svBar);
	picker.setColor(currentColor);
	// TODO Set buttons save/delete according to existence of color in
	// saved list
	picker.setOnColorChangedListener(this);

	//save = (Button) findViewById(R.id.saveButton);
	//delete = (Button) findViewById(R.id.deleteButton);

	//save.setOnClickListener(this);
	//delete.setOnClickListener(this);

	//if (!isEditMode) {
	  //  delete.setVisibility(View.GONE);
	//}
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.saveButton:
	    if (currentColor != Color.BLACK){
		if(DataManager.getInstance().saveColor(currentColor)){
		    Toast.makeText(this, "Color saved.", Toast.LENGTH_SHORT).show();
		    //finish();
		}
		else{
		    Toast.makeText(this, "Error saving color. Please try again.", Toast.LENGTH_SHORT).show();
		}
	    }
	    else
		Toast.makeText(
			this,
			"Can't save black color. Please use power button instead.",
			Toast.LENGTH_SHORT).show();
	    break;
	case R.id.deleteButton:
	    DataManager.getInstance().deleteColor(currentColor);
	    break;

	}
    }

    @Override
    public void onColorChanged(int color) {
	currentColor = color;
	

	String c = Integer.toHexString(currentColor);
	if (controller != null && (System.currentTimeMillis() - startTime) > 250) {
	    controller.setValue("red=" + c.substring(4, 6) + "&green=" + c.substring(2, 4) + "&blue=" + c.substring(6, 8));
	    new SendToLamp(this, controller, 0).execute();
	    startTime = System.currentTimeMillis();
	}
	//else
	  //  startTime = System.currentTimeMillis();
    }

    @Override
    public void setSending(boolean isSendingColor) {
	this.isSendingColor = isSendingColor;
    }

    @Override
    public int getCurrentValue() {
	return currentColor;
    }

    @Override
    public Activity getActivity() {
	return this;
    }

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this, "Color",
		Mode.NORMAL);
    }

}
