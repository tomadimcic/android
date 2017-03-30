package com.iotwear.wear.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.NewAlarmAdapter;
import com.iotwear.wear.gui.adapter.NewAlarmAdapter.ViewHolder;
import com.iotwear.wear.model.Alarm;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class NewAlarmControlActivity extends BaseActivity {

    private EditText alarmName;
    private ListView controllerList;
    private ImageView save, cancel;

    private NewAlarmAdapter alarmAdapter;
    private PiDevice selectedDevice;
    
    
    TimePicker timePicker;

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this,
		"Create alarm", Mode.ONLY_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	selectedDevice = (PiDevice) getIntent().getExtras().getSerializable(
		Constants.EXTRA_DEVICE);
	alarmAdapter = new NewAlarmAdapter(this, selectedDevice);

	View content = View.inflate(this, R.layout.activity_new_alarm, null);
	contentFrame.addView(content);

	alarmName = (EditText) findViewById(R.id.device_name_edittext1);
	timePicker = (TimePicker) findViewById(R.id.timePicker1);
	timePicker.setIs24HourView(true);
	timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
	
	controllerList = (ListView) findViewById(R.id.controller_list1);

	controllerList.setAdapter(alarmAdapter);

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
		Alarm alarm = new Alarm();
		Alarm newAlarm = alarmAdapter.getAlarm();
		alarm.setDeviceName(selectedDevice.getName());
		timePicker.clearFocus();
		// re-read the values, in my case i put them in a Time object.
		int hour   = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		
		alarm.setTime(hour + ":" + minute);
		
		
		boolean hasSameName = false;
		boolean isEmpty = false;
		ArrayList<Alarm> alarmList = DataManager.getInstance().getAlarmListForDevice(selectedDevice);

		int size = alarmList.size();
		for (int i = 0; i < size; i++) {
		    if (hasSameName || isEmpty)
			break;
		    
		    String name = alarmName.getText().toString();
		    if (name.length() == 0) {
			isEmpty = true;
			break;
		    }

		    for (int j = 0; j < size; j++) {
			String another = alarmList.get(j).getName();
			if (name.equals(another)) {
			    hasSameName = true;
			    break;
			}
		    }

		}

		if (hasSameName) {
		    Toast.makeText(
			    NewAlarmControlActivity.this,
			    "Alarms must have different names. Please rename the alarm.",
			    Toast.LENGTH_SHORT).show();
		}

		else {
		    if (isEmpty) {
		    Toast.makeText(NewAlarmControlActivity.this,
			    "Alarm must have name. Please name the alarm.",
			    Toast.LENGTH_SHORT).show();
		}

		else {
		    alarm.setName(alarmName.getText().toString());
		    alarm.setActivated(false);
		    alarm.setDaily("0");
		    boolean oneChecked = false;
		    ArrayList<Integer> cbl = alarmAdapter.getCheckBoxList();
		    for (int i = 0; i < cbl.size(); i++) {
			if(cbl.get(i) == 1){
			    alarm.addType1(newAlarm.getType().get(i));
			    alarm.addId(newAlarm.getId().get(i));
			    alarm.addValue(newAlarm.getValue().get(i));
			    oneChecked = true;
			}
		    }
		    
		    if(!oneChecked){
			Toast.makeText(NewAlarmControlActivity.this,
				    "At least one control should be checked!",
				    Toast.LENGTH_SHORT).show();
		    }else{
		    
        		    boolean res = DataManager.getInstance().addAlarmList(alarm);
        		    if (res) {
        			/*Intent i = new Intent();
        			i.putExtra(Constants.EXTRA_ALARM,
        				alarmAdapter.getDevice());
        			setResult(RESULT_OK, i);*/
        			finish();
        		    } else {
        			Toast.makeText(NewAlarmControlActivity.this,
        				"Error while saving data.", Toast.LENGTH_SHORT)
        				.show();
        		    }
        		}
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
