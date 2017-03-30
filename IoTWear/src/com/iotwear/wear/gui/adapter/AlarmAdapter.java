package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.adapter.ControllerEditAdapter.ViewHolder;
import com.iotwear.wear.gui.dialog.CustomizeDialogIconChooser;
import com.iotwear.wear.model.Alarm;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.PiControl.PiControlType;
import com.iotwear.wear.util.DataManager;

public class AlarmAdapter extends BaseAdapter {

    private ArrayList<Alarm> alarmList;
    private Activity activity;
    private LayoutInflater inflater;
    TextView textview;
    int i;
    PiDevice selectedDevice;
    ViewHolder holder;
    

    public AlarmAdapter(Activity activity, PiDevice selectedDevice) {
	this.selectedDevice = selectedDevice;
	this.alarmList = DataManager.getInstance().getAlarmListForDevice(selectedDevice);
	this.activity = activity;
	inflater = activity.getLayoutInflater();
    }
    

    @Override
    public int getCount() {
	return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
	return alarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	
	convertView = inflater.inflate(R.layout.item_alarm, null);

	final View view = convertView;
	
	holder = new ViewHolder();
	holder.name = (TextView) convertView.findViewById(R.id.controller_name2);
	holder.time = (TextView) convertView.findViewById(R.id.time_txt);
	holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox2);
	holder.rb1 = (RadioButton) convertView.findViewById(R.id.alarm_radioButton1);
	holder.rb2 = (RadioButton) convertView.findViewById(R.id.alarm_radioButton2);
	holder.ib = (ImageButton) convertView.findViewById(R.id.delete_imageButton1);
	convertView.setTag(holder);

	holder = (ViewHolder) convertView.getTag();
	final int pos = position;
	final Alarm item = alarmList.get(position);
	
	if(item.isActivated())
	    holder.cb.setChecked(true);
	else
	    holder.cb.setChecked(false);
	
	if(item.getDaily().equals("0")){
	    holder.rb1.setChecked(true);
	    holder.rb2.setChecked(false);
	}
	else{
	    holder.rb1.setChecked(false);
	    holder.rb2.setChecked(true);
	}
	

	holder.name.setText(item.getName());
	holder.time.setText(item.getTime());
	
	holder.cb.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		if(item.isActivated()){
		    holder.cb.setChecked(false);
		    alarmList.get(pos).setActivated(false);
		}else{
		    holder.cb.setChecked(true);
		    alarmList.get(pos).setActivated(true);
		}
		alarmList.set(pos, item);
		refresh();
		
	    }
	});
	
	holder.rb1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		holder.rb1.setChecked(true);
		holder.rb2.setChecked(false);
		//item.setDaily("0");
		alarmList.get(pos).setDaily("0");
		alarmList.set(pos, item);
		refresh();
		
	    }
	});
	
	holder.rb2.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		holder.rb2.setChecked(true);
		holder.rb1.setChecked(false);
		
		alarmList.get(pos).setDaily("1");
		alarmList.set(pos, item);
		refresh();
		
	    }
	});
	
	holder.ib.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		alarmList.remove(pos);
		DataManager.getInstance().deleteAlarm(item);
		refresh();
	    }
	});
	
	

	return convertView;
    }
    
    public void refresh() {
	notifyDataSetInvalidated();
	notifyDataSetChanged();
    }
    
    public void notifyAdapter(ArrayList<Alarm> alarmList){
	this.alarmList = alarmList;
	notifyDataSetChanged();
    }
    
    public ArrayList<Alarm> getAlarmList(){
	return alarmList;
    }

    class ViewHolder {
	TextView name;
	TextView time;
	CheckBox cb;
	RadioButton rb1;
	RadioButton rb2;
	ImageButton ib;
    }
}
