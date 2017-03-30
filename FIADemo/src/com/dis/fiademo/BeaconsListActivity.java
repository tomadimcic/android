package com.dis.fiademo;

import java.util.ArrayList;
import java.util.HashMap;

import com.dis.fiademo.adapters.BeaconListAdapter;
import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.model.Beacon;
import com.dis.fiademo.util.DataManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BeaconsListActivity extends Activity implements OnItemClickListener{
    
    private ArrayList<Beacon> beaconList;
    private BeaconListAdapter beaconListAdapter;

    private ListView beaconListView;
    // private Button addDevice;
    private TextView emptyList;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	View content = LayoutInflater.from(this).inflate(
		R.layout.activity_beacon_list, null);
	setContentView(content);
	
	db = new DatabaseHandler(this);
	//db.deleteAllBeacons();
	
	beaconListView = (ListView) findViewById(R.id.beacon_list);
	emptyList = (TextView) findViewById(R.id.empty_list);
	
	/*ArrayList<Beacon> list = DataManager.getInstance().testList();
	for (Beacon beacon : list) {
	    db.addBeacon(beacon);
	}*/

	beaconList = new ArrayList<Beacon>();
	beaconList = DataManager.getInstance().getBeaconsList(db);

	if (beaconList.size() == 0) {
	    beaconListView.setVisibility(View.GONE);
	    emptyList.setVisibility(View.VISIBLE);
	} else {
	    beaconListAdapter = new BeaconListAdapter(BeaconsListActivity.this, beaconList, db);
	    beaconListView.setAdapter(beaconListAdapter);
	    beaconListView.setOnItemClickListener(this);
	}
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
	
    }

}
