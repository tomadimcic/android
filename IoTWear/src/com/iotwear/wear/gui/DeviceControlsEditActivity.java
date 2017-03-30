package com.iotwear.wear.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.ControllerEditAdapter;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class DeviceControlsEditActivity extends BaseActivity {

    private EditText deviceName;
    private ListView controllerList;
    private ImageView save, cancel;

    private ControllerEditAdapter controllerAdapter;
    private PiDevice selectedDevice;

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this,
		"Edit controls", Mode.ONLY_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	selectedDevice = (PiDevice) getIntent().getExtras().getSerializable(
		Constants.EXTRA_DEVICE);
	controllerAdapter = new ControllerEditAdapter(this, selectedDevice);

	View content = View.inflate(this, R.layout.activity_device_edit, null);
	contentFrame.addView(content);

	deviceName = (EditText) findViewById(R.id.device_name_edittext);
	deviceName.setText(selectedDevice.getName());
	controllerList = (ListView) findViewById(R.id.controller_list);

	controllerList.setAdapter(controllerAdapter);

	setMenu();
    }

    private void setMenu() {
	save = (ImageView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item, null);
	save.setId(R.id.actionbar_ok);
	save.setImageDrawable(getResources().getDrawable(
		R.drawable.actionbar_ok_selector));
	save.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		controllerAdapter.getDevice().setName(
			deviceName.getText().toString());
		boolean hasSameName = false;
		boolean isEmpty = false;

		int size = controllerAdapter.getDevice().getControlList()
			.size();
		for (int i = 0; i < size; i++) {
		    if (hasSameName || isEmpty)
			break;
		    
		    String name = controllerAdapter.getDevice()
			    .getControlList().get(i).getName();
		    if (name.length() == 0) {
			isEmpty = true;
			break;
		    }

		    for (int j = 0; j < size; j++) {
			String another = controllerAdapter.getDevice()
				.getControlList().get(j).getName();
			if (i != j && name.equals(another)) {
			    hasSameName = true;
			    break;
			}
		    }

		}

		if (hasSameName) {
		    Toast.makeText(
			    DeviceControlsEditActivity.this,
			    "Controllers must have different names. Please rename them.",
			    Toast.LENGTH_SHORT).show();
		}

		else if (isEmpty) {
		    Toast.makeText(DeviceControlsEditActivity.this,
			    "Controllers must have names. Please name them.",
			    Toast.LENGTH_SHORT).show();
		}

		else {
		    boolean res = DataManager.getInstance().saveDevice(
			    controllerAdapter.getDevice());
		    if (res) {
			Intent i = new Intent();
			i.putExtra(Constants.EXTRA_DEVICE,
				controllerAdapter.getDevice());
			setResult(RESULT_OK, i);
			finish();
		    } else {
			Toast.makeText(DeviceControlsEditActivity.this,
				"Error while saving data.", Toast.LENGTH_SHORT)
				.show();
		    }
		}
	    }
	});
	getActionBarHandler().addMenuItem(save);

	cancel = (ImageView) getLayoutInflater().inflate(
		R.layout.actionbar_menu_item, null);
	cancel.setId(R.id.actionbar_cancel);
	cancel.setImageDrawable(getResources().getDrawable(
		R.drawable.actionbar_cancel_selector));
	cancel.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		setResult(RESULT_CANCELED);
		finish();
	    }
	});
	getActionBarHandler().addMenuItem(cancel);
    }

}
