package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.adapter.AlarmAdapter.ViewHolder;
import com.iotwear.wear.model.Alarm;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.PiControl.PiControlType;

public class NewAlarmAdapter extends BaseAdapter {

    private PiDevice selectedDevice;
    private Activity activity;
    private LayoutInflater inflater;
    TextView textview;
    int i;
    ViewHolder holder;
    Alarm alarm;
    ArrayList<Integer> rbl;
    ArrayList<Integer> cbl;
    

    public NewAlarmAdapter(Activity activity, PiDevice selectedDevice) {
	this.selectedDevice = selectedDevice;
	alarm = new Alarm();
	rbl = new ArrayList<Integer>();
	cbl = new ArrayList<Integer>();
	setInit();
	this.activity = activity;
	inflater = activity.getLayoutInflater();
    }
    
    public void setInit(){
	for (PiControl control : selectedDevice.getControlList()) {
	    alarm.addType(control.getPiControlType());
	    alarm.addId(control.getId());
	    alarm.addValue("0");
	    rbl.add(1);
	    cbl.add(0);
	}
    }

    @Override
    public int getCount() {
	return selectedDevice.getControlList().size();
    }

    @Override
    public Object getItem(int position) {
	return selectedDevice.getControlList().get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	
	convertView = inflater.inflate(R.layout.item_new_alarm, null);
	
	holder = new ViewHolder();
	holder.icon = (ImageView) convertView
		.findViewById(R.id.controller_icon1);
	holder.name = (TextView) convertView.findViewById(R.id.controller_name1);
	holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_new_alarm);
	holder.onRB = (RadioButton) convertView.findViewById(R.id.radioButton_new_alarm_on);
	holder.offRB = (RadioButton) convertView.findViewById(R.id.radioButton_new_alarm_off);
	convertView.setTag(holder);
	
	if(alarm.getValue().get(position).equals("1")){
	    holder.onRB.setChecked(true);
	    holder.offRB.setChecked(false);
	}
	if(alarm.getValue().get(position).equals("0")){
	    holder.onRB.setChecked(false);
	    holder.offRB.setChecked(true);
	}
	
	if(cbl.get(position) == 0)
	    holder.checkBox.setChecked(false);
	if(cbl.get(position) == 1)
	    holder.checkBox.setChecked(true);

	holder = (ViewHolder) convertView.getTag();
	final int pos = position;
	final PiControl item = selectedDevice.getControlList().get(position);
	
	
	holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    item.getIcon()));

	holder.name.setText(item.getName());
	
	holder.onRB.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		rbl.set(pos, 0);
		holder.onRB.setChecked(true);
		    holder.offRB.setChecked(false);
		    alarm.setValueAt(pos, "1");
		    refresh();
		
	    }
	});
	holder.offRB.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		rbl.set(pos, 1);
		holder.onRB.setChecked(false);
		    holder.offRB.setChecked(true);
		    alarm.setValueAt(pos, "0");
		    refresh();
		
	    }
	});
	
	holder.checkBox.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		if(cbl.get(pos) == 0){
		    cbl.set(pos, 1);
		    holder.checkBox.setChecked(true);
		}else{
		    cbl.set(pos, 0);
		    holder.checkBox.setChecked(false);
		}
		refresh();
		
	    }
	});

	return convertView;
    }
    
    public void refresh() {
	notifyDataSetInvalidated();
	notifyDataSetChanged();
    }
    
    public void notifyAdapter(){
	notifyDataSetChanged();
    }

    public Alarm getAlarm() {
	return alarm;
    }
    
    public ArrayList<Integer> getCheckBoxList() {
	return cbl;
    }

    public class ViewHolder {
	ImageView icon;
	TextView name;
	CheckBox checkBox;
	RadioButton onRB;
	RadioButton offRB;
    }
    
    public int[] getIcons(){
	int[] icons = {R.drawable.ic_launcher,
		R.drawable.ic_switch,
		R.drawable.ic_taster
	};
	return icons;
    }
}
