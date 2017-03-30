package com.iotwear.wear.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.dialog.CustomizeDialogDeleteDevice;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.GetControllerStatus;
import com.iotwear.wear.task.led.SetColorTask;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class LEDControllerActivity extends BaseActivity implements
	OnClickListener, GetStatusCaller, ValueSendingInterface {

    RelativeLayout colors, animations, favourites, rlPower;
    ImageView btnPower;
    public ProgressDialog progress;
    PiControl controller;
    PiDevice selectedDevice;
    int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	View content = LayoutInflater.from(this).inflate(
		R.layout.activity_device, null);
	contentFrame.addView(content);

	controller = (PiControl) getIntent().getSerializableExtra(
		Constants.EXTRA_CONTROLLER);
	

	if (controller == null) {
	    Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
	    Intent i = new Intent(this, DeviceListActivity.class);
	    startActivity(i);
	    finish();
	} else {
	    getActionBarHandler()
		    .setTitle(controller.getHostDevice().getName());
	    selectedDevice = controller.getHostDevice();
	}

	if (controller.getHostDevice().isSingleControllerDevice())
	    setMenu();

	colors = (RelativeLayout) findViewById(R.id.colors);
	animations = (RelativeLayout) findViewById(R.id.animations);
	favourites = (RelativeLayout) findViewById(R.id.favourites);
	rlPower = (RelativeLayout) findViewById(R.id.rlPower);
	btnPower = (ImageView) findViewById(R.id.btnPower);

	animations.setOnClickListener(this);
	colors.setOnClickListener(this);
	favourites.setOnClickListener(this);
	rlPower.setOnClickListener(this);

	// Check this only for LEDController, otherwise NOT!!!
	new GetControllerStatus(this, controller).execute();
    }

    @Override
    public void onClick(View v) {
	Intent i = null;
	switch (v.getId()) {
	case R.id.colors:
	    i = new Intent(LEDControllerActivity.this,
		    ColorPickerActivity.class);
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    break;

	case R.id.animations:
	    i = new Intent(LEDControllerActivity.this, AnimationActivity.class);
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    break;

	case R.id.favourites:
	    i = new Intent(LEDControllerActivity.this, FavouritesActivity.class);
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    break;

	case R.id.rlPower:
	    if (controller.getHostDevice().isTurnedOn()) {
		currentColor = Color.BLACK;
	    } else {
		currentColor = Color.WHITE;
	    }
	    new SetColorTask(this, controller).execute(currentColor);
	    break;
	}

	if (i != null) {
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    startActivity(i);
	}
    }

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this, "Device",
		Mode.NORMAL);
    }

    @Override
    public void handleReceivedStatus(boolean status) {
	if (status) {
	    btnPower.setImageResource(R.drawable.icon_remote_poweron);
	} else {
	    btnPower.setImageResource(R.drawable.icon_remote_poweroff);
	}

	controller.getHostDevice().setTurnedOn(status);
    }

    @Override
    public void setSending(boolean isSending) {
	// TODO Auto-generated method stub

    }

    @Override
    public int getCurrentValue() {
	return currentColor;
    }

    @Override
    public Activity getActivity() {
	return this;
    }

    /*public void showDeleteDialog() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Settings")
		.setMessage("This device will be deleted. Are you sure?")
		.setNeutralButton("Cancel",
			new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				dialog.dismiss();

			    }
			})
		.setPositiveButton("Delete",
			new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				DataManager.getInstance().deleteDevice(
					controller.getHostDevice());
				dialog.dismiss();
				finish();
			    }
			}).show();
    }*/
    
    public void showDeleteDialog() {
	    CustomizeDialogDeleteDevice customizeDialog = new CustomizeDialogDeleteDevice(LEDControllerActivity.this, this, selectedDevice);  
        customizeDialog.setTitle(selectedDevice.getName());   
        customizeDialog.setMessage("This device will be deleted. Are you sure?");
        customizeDialog.show();
	}

    public void setMenu() {

	ImageView alarm = (ImageView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item, null);
	alarm.setId(R.id.actionbar_alarm);
	alarm.setImageDrawable(getResources().getDrawable(
		R.drawable.actionbar_alarm_selector));
	alarm.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(LEDControllerActivity.this,
			AlarmControlActivity.class);
		i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
		startActivityForResult(i, Constants.REQUEST_ALARM);
	    }
	});
	getActionBarHandler().addMenuItem(alarm);

	ImageView settings = (ImageView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item, null);
	settings.setId(R.id.actionbar_settings);
	settings.setImageDrawable(getResources().getDrawable(
		R.drawable.actionbar_settings_selector));
	settings.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(LEDControllerActivity.this,
			DeviceControlsEditActivity.class);
		i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
		startActivityForResult(i,
			Constants.REQUEST_DEVICE_MULTICONTROLLER_EDIT);
	    }
	});
	getActionBarHandler().addMenuItem(settings);
	ImageView delete = (ImageView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item, null);
	delete.setId(R.id.actionbar_delete);
	delete.setImageDrawable(getResources().getDrawable(
		R.drawable.actionbar_cancel_selector));
	delete.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showDeleteDialog();
	    }
	});
	getActionBarHandler().addMenuItem(delete);

    }
}
