package com.iotwear.wear.gui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.AlarmAdapter;
import com.iotwear.wear.model.Alarm;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.GetAlarmTask;
import com.iotwear.wear.task.SendAlarm;
import com.iotwear.wear.task.interfaces.GetAlarmInterface;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class AlarmActivity extends BaseActivity implements GetAlarmInterface {

    private TextView deviceName;
    private ListView controllerList;
    private ImageView save, cancel;

    private AlarmAdapter mAlarmAdapter;
    private ArrayList<Alarm> alarmList;
    ImageButton addNewAlarmBtn;
    private PiDevice mSelectedDevice;

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this,
		"Alarms", Mode.ONLY_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	mSelectedDevice = (PiDevice) getIntent().getExtras().getSerializable(
		Constants.EXTRA_DEVICE);
	//alarmList = DataManager.getInstance().getAlarmListForDevice(selectedDevice);
	mAlarmAdapter = new AlarmAdapter(this, mSelectedDevice);
	

	View content = View.inflate(this, R.layout.activity_alarm_new, null);
	contentFrame.addView(content);

	//deviceName = (TextView) findViewById(R.id.device_name_edittext2);
	//deviceName.setText(selectedDevice.getName());
	controllerList = (ListView) findViewById(R.id.alarms_list);

	controllerList.setAdapter(mAlarmAdapter);
	addNewAlarmBtn = (ImageButton) findViewById(R.id.addAlarmBtn);
	addNewAlarmBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(AlarmActivity.this,
			NewAlarmControlActivity.class);
		i.putExtra(Constants.EXTRA_DEVICE, mSelectedDevice);
		startActivityForResult(i, Constants.REQUEST_ALARM);
		
	    }
	});

	setMenu();
	
	new GetAlarmTask(this, this, mSelectedDevice).execute();
	
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
		ArrayList<Alarm> al = mAlarmAdapter.getAlarmList();
		ArrayList<Alarm> alForSending = new ArrayList<Alarm>();
		try {
			DataManager.getInstance().saveAlarmList(al);
		    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		for (Alarm alarm : al) {
		    
		    if(alarm.isActivated())
			alForSending.add(alarm);
		}
		if(alForSending.size() > 0){
		    new SendAlarm(AlarmActivity.this, alForSending, mSelectedDevice).execute();
		}
		finish();
		
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        alarmList = DataManager.getInstance().getAlarmListForDevice(mSelectedDevice);
        mAlarmAdapter.notifyAdapter(alarmList);
    }

    @Override
    public void onAlarmReceived(String alarms) {
	System.out.println(alarms);
	ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
	Alarm alarm;
	JSONArray jsonArray = null;
	
	try {
	    jsonArray = new JSONArray(alarms);
	    for (int i = 0; i < jsonArray.length(); i++) {
		alarm = Alarm.fromReceivedJsonToAlarm(jsonArray.getJSONObject(i));
		alarmList.add(alarm);
	    }
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	mAlarmAdapter.refresh();
	
    }
}
