package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.model.DimControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.SwitchControl;
import com.iotwear.wear.model.PiControl.PiControlType;

public class GroupControlAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<PiControl> controlList;

    public GroupControlAdapter(Activity activity) {
	this.activity = activity;
	inflater = activity.getLayoutInflater();
	controlList = new ArrayList<PiControl>();
    }

    public ArrayList<PiControl> getControlList() {
	return controlList;
    }

    public void setControlList(ArrayList<PiControl> controlList) {
	this.controlList = controlList;
	notifyDataSetChanged();
    }

    public void refresh() {
	notifyDataSetInvalidated();
	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
	return controlList.size();
    }

    @Override
    public Object getItem(int position) {
	return controlList.get(position);
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	final PiControl item = controlList.get(position);

	convertView = inflater.inflate(R.layout.item_group_controller, null);

	ViewHolder holder = new ViewHolder();
	holder.icon = (ImageView) convertView
		.findViewById(R.id.controller_icon);
	holder.name = (TextView) convertView.findViewById(R.id.controller_name);
	holder.value = (TextView) convertView
		.findViewById(R.id.controller_value);
	holder.edit = (ImageView) convertView
		.findViewById(R.id.controller_edit);
	holder.remove = (ImageView) convertView
		.findViewById(R.id.controller_remove);

	holder.name.setText(item.getName());
	switch (item.getPiControlType().ordinal()) {
	// Switch
	case 1:
	    holder.value.setText(((SwitchControl) item).isTurnedOn() ? "ON"
		    : "OFF");
	    holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    R.drawable.ic_switch));
	    break;
	// Dimmer
	case 2:
	    holder.value.setText(String.valueOf(((DimControl) item).getDimValue()));
	    holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    R.drawable.ic_dimmer));
	    break;
	}

	holder.edit.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (item.getPiControlType() == PiControlType.SWITCH) {
		    showSwitchControlDialog((SwitchControl) item);
		} else {
		    showDimControlDialog((DimControl) item);
		}
	    }
	});

	holder.remove.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		controlList.remove(item);
		refresh();
	    }
	});

	return convertView;
    }

    public void showSwitchControlDialog(final SwitchControl item) {
	AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	builder.setTitle(item.getName());
	builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		item.setTurnedOn(true);
		refresh();
		dialog.dismiss();
	    }
	});
	builder.setNegativeButton("OFF", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		item.setTurnedOn(false);
		refresh();
		dialog.dismiss();
	    }
	});
	builder.show();
    }

    public void showDimControlDialog(final DimControl item) {
	AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);

	View dimControlView = LayoutInflater.from(activity).inflate(
		R.layout.dialog_control_dimmer, null);
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
			refresh();
			dialog.dismiss();
		    }
		});
	builder2.show();
    }

    class ViewHolder {
	ImageView icon;
	TextView name;
	TextView value;
	ImageView edit;
	ImageView remove;
    }
}
