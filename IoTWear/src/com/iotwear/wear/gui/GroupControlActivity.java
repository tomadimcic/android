package com.iotwear.wear.gui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.GroupControlAdapter;
import com.iotwear.wear.model.DimControl;
import com.iotwear.wear.model.GroupControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.SwitchControl;
import com.iotwear.wear.model.PiControl.PiControlType;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class GroupControlActivity extends BaseActivity {

    private PiDevice selectedDevice;
    private GroupControl groupControl;
    private boolean isEditMode;
    private ListView controllerList;
    private EditText groupName;
    private ImageView add, save, cancel;
    private GroupControlAdapter controllerAdapter;
    private ControllerSelectionAdapter selectionAdapter;

    private AlertDialog selectControllerDialog;

    private ArrayList<PiControl> selectableControllerList;

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this,
		"New group", Mode.ONLY_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	View content = View.inflate(this, R.layout.activity_controllers_group,
		null);
	contentFrame.addView(content);
	
	controllerList = (ListView) findViewById(R.id.controller_list);
	groupName = (EditText) findViewById(R.id.group_name_edittext);
	add = (ImageView) findViewById(R.id.add_controller);

	selectedDevice = (PiDevice) getIntent().getExtras().getSerializable(
		Constants.EXTRA_DEVICE);
	GroupControl selectedGroup = (GroupControl) getIntent().getExtras()
		.getSerializable(Constants.EXTRA_GROUP_CONTROL);
	if (selectedGroup == null) {
	    groupControl = new GroupControl();
	} else {
	    groupControl = selectedGroup;
	    getActionBarHandler().setTitle(groupControl.getName());
	    isEditMode = true;
	}

	controllerAdapter = new GroupControlAdapter(this);
	controllerAdapter.setControlList(groupControl.getGroupControlList());
	controllerList.setAdapter(controllerAdapter);

	groupName.setText(groupControl.getName());
	add.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		AlertDialog.Builder newBuilder = new AlertDialog.Builder(
			GroupControlActivity.this);

		View content = getLayoutInflater().inflate(
			R.layout.dialog_add_group_item, null);
		ListView controlListView = (ListView) content
			.findViewById(R.id.controller_list);
		selectionAdapter = new ControllerSelectionAdapter();
		controlListView.setAdapter(selectionAdapter);
		controlListView
			.setOnItemClickListener(new OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> parent,
				    View view, int position, long id) {
				PiControl item = selectableControllerList
					.get(position);
				if (item.getPiControlType() == PiControlType.SWITCH)
				    showSwitchControlDialog((SwitchControl) item);
				else
				    showDimControlDialog((DimControl) item);
				selectControllerDialog.dismiss();
			    }
			});
		newBuilder.setView(content);
		newBuilder.setTitle("Select controller");
		selectControllerDialog = newBuilder.create();
		selectControllerDialog.show();
	    }
	});

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
		String name = groupName.getText().toString();
		if (name.length() == 0) {
		    Toast.makeText(GroupControlActivity.this,
			    "Group must have a name.", Toast.LENGTH_SHORT)
			    .show();
		    return;
		}

		if (controllerAdapter.getControlList().size() == 0) {
		    Toast.makeText(GroupControlActivity.this,
			    "Group must have at least one controller.",
			    Toast.LENGTH_SHORT).show();
		    return;
		}

		groupControl.setName(name);

		groupControl.setGroupControlList(controllerAdapter
			.getControlList());

		if (isEditMode) {
		    selectedDevice.getControlList().set(Integer.parseInt(groupControl.getId()),
			    groupControl);
		} else {
		    groupControl.setId(Integer.toString(selectedDevice.getControlList().size()));
		    selectedDevice.getControlList().add(groupControl);
		}

		boolean res = DataManager.getInstance().saveDevice(
			selectedDevice);
		if (res) {
		    Intent i = new Intent();
		    i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
		    setResult(RESULT_OK, i);
		    finish();
		} else {
		    Toast.makeText(GroupControlActivity.this,
			    "Error while saving data.", Toast.LENGTH_SHORT)
			    .show();
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

    public void showSwitchControlDialog(final SwitchControl item) {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle(item.getName());
	builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		item.setTurnedOn(true);
		groupControl.addControl(item);
		selectableControllerList.remove(item);
		controllerAdapter.notifyDataSetChanged();
		dialog.dismiss();
	    }
	});
	builder.setNegativeButton("OFF", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		item.setTurnedOn(false);
		groupControl.addControl(item);
		selectableControllerList.remove(item);
		controllerAdapter.notifyDataSetChanged();
		dialog.dismiss();
	    }
	});
	builder.show();
    }

    public void showDimControlDialog(final DimControl item) {
	AlertDialog.Builder builder2 = new AlertDialog.Builder(
		GroupControlActivity.this);

	View dimControlView = LayoutInflater.from(GroupControlActivity.this)
		.inflate(R.layout.dialog_control_dimmer, null);
	SeekBar dimBar = (SeekBar) dimControlView
		.findViewById(R.id.seekbar_dimmer);
	dimBar.setProgress(item.getDimValue());
	dimBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress,
		    boolean fromUser) {
		item.setDimValue(progress);
	    }

	});
	builder2.setView(dimControlView);
	builder2.setTitle(item.getName());
	builder2.setPositiveButton("DONE",
		new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			groupControl.addControl(item);
			selectableControllerList.remove(item);
			controllerAdapter.notifyDataSetChanged();
			dialog.dismiss();
		    }
		});
	builder2.show();
    }

    class ControllerSelectionAdapter extends BaseAdapter {

	public ControllerSelectionAdapter() {
	    super();
	    selectableControllerList = new ArrayList<PiControl>();
	    for (PiControl pi : selectedDevice.getControlList()) {
		if (pi.getPiControlType() == PiControlType.DIMMER
			|| pi.getPiControlType() == PiControlType.SWITCH) {
		    boolean isSame = false;
		    for (PiControl selPi : groupControl.getGroupControlList()) {
			if (selPi.getName().equals(pi.getName())) {
			    isSame = true;
			    break;
			}
		    }
		    if (!isSame)
			selectableControllerList.add(pi);
		}
	    }
	}

	@Override
	public Object getItem(int position) {
	    return selectableControllerList.get(position);
	}

	@Override
	public int getCount() {
	    return selectableControllerList.size();
	}

	@Override
	public long getItemId(int position) {
	    return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    final PiControl item = selectableControllerList.get(position);

	    convertView = getLayoutInflater().inflate(
		    R.layout.item_select_controller, null);

	    ViewHolder holder = new ViewHolder();
	    holder.icon = (ImageView) convertView
		    .findViewById(R.id.controller_icon);
	    holder.name = (TextView) convertView
		    .findViewById(R.id.controller_name);

	    holder.name.setText(item.getName());
	    switch (item.getPiControlType().ordinal()) {
	    // Switch
	    case 1:
		holder.icon.setImageDrawable(getResources().getDrawable(
			R.drawable.ic_switch));
		break;
	    // Dimmer
	    case 2:
		holder.icon.setImageDrawable(getResources().getDrawable(
			R.drawable.ic_dimmer));
		break;
	    }

	    return convertView;
	}

	class ViewHolder {
	    ImageView icon;
	    TextView name;
	}

    }

}
