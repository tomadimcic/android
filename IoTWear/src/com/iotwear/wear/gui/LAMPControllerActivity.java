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
import com.iotwear.wear.model.LampControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.GetControllerStatus;
import com.iotwear.wear.task.SendToLamp;
import com.iotwear.wear.task.led.SetColorTask;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class LAMPControllerActivity extends BaseActivity implements
	OnClickListener, GetStatusCaller, ValueSendingInterface {

    RelativeLayout colors, favourites, rlPower; // , animations
    ImageView btnPower;
    public ProgressDialog progress;
    LampControl controller;
    int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	View content = LayoutInflater.from(this).inflate(
		R.layout.lamp_activity, null);
	contentFrame.addView(content);

	controller = (LampControl) getIntent().getSerializableExtra(
		Constants.EXTRA_CONTROLLER);

	if (controller == null) {
	    Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
	    Intent i = new Intent(this, DeviceListActivity.class);
	    startActivity(i);
	    finish();
	} else {
	    getActionBarHandler()
		    .setTitle(controller.getHostDevice().getName());
	}

	if (controller.getHostDevice().isSingleControllerDevice())
	    setMenu();

	colors = (RelativeLayout) findViewById(R.id.colors);
	//animations = (RelativeLayout) findViewById(R.id.animations);
	favourites = (RelativeLayout) findViewById(R.id.favourites);
	rlPower = (RelativeLayout) findViewById(R.id.rlPower);
	btnPower = (ImageView) findViewById(R.id.btnPower);

	//animations.setOnClickListener(this);
	colors.setOnClickListener(this);
	favourites.setOnClickListener(this);
	rlPower.setOnClickListener(this);

	// Check this only for LEDController, otherwise NOT!!!
	//new GetControllerStatus(this, controller).execute();
    }

    @Override
    public void onClick(View v) {
	Intent i = null;
	switch (v.getId()) {
	case R.id.colors:
	    i = new Intent(LAMPControllerActivity.this,
		    ColorPickerForLampActivity.class);
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    break;

	/*case R.id.animations:
	    i = new Intent(LAMPControllerActivity.this, AnimationActivity.class);
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    break;*/

	case R.id.favourites:
	    i = new Intent(LAMPControllerActivity.this, FavouritesLampActivity.class);
	    i.putExtra(Constants.EXTRA_CONTROLLER, controller);
	    break;

	case R.id.rlPower:
	    if (controller.getHostDevice().isTurnedOn()) {
		controller.getHostDevice().setTurnedOn(false);
		currentColor = Color.BLACK;
		btnPower.setImageResource(R.drawable.icon_remote_poweroff);
	    } else {
		controller.getHostDevice().setTurnedOn(true);
		currentColor = Color.WHITE;
		btnPower.setImageResource(R.drawable.icon_remote_poweron);
	    }
	    String c = Integer.toHexString(currentColor);
	    controller.setValue("red=" + c.substring(4, 6) + "&green=" + c.substring(2, 4) + "&blue=" + c.substring(6, 8));
	    new SendToLamp(this, controller, 0).execute();
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
		Mode.SLIDING);
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

    public void showDeleteDialog() {
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
    }

    public void setMenu() {
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
