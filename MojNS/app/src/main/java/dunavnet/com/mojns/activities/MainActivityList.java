package dunavnet.com.mojns.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import dunavnet.com.mojns.R;
import dunavnet.com.mojns.activities.adapters.MyEventListAdapter;
import dunavnet.com.mojns.interfaces.ResponseHandler;
import dunavnet.com.mojns.model.LogData;
import dunavnet.com.mojns.model.MyEvents;
import dunavnet.com.mojns.tasks.LoadEventsTask;
import dunavnet.com.mojns.tasks.LogTask;
import dunavnet.com.mojns.util.SettingsConstants;
import dunavnet.com.mojns.util.Util;

public class MainActivityList extends Activity implements SwipeRefreshLayout.OnRefreshListener, ResponseHandler {

    public static final int authentication = 1;
    public static final int writing = 2;
    public static final int reading = 3;
    public ListView mEventsView;
    public TextView emptyList;
    public ArrayList<MyEvents> mEventsList;
    public MyEventListAdapter mEventsListAdapter;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public Button report;
    public ImageView profileImage, logoutImage;
    public SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_list);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        mEventsView = (ListView) findViewById(R.id.events_list);
        emptyList = (TextView) findViewById(R.id.empty_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        report = (Button) findViewById(R.id.report_button);
        profileImage = (ImageView) findViewById(R.id.imageView);
        logoutImage = (ImageView) findViewById(R.id.logout);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(MainActivityList.this, ProfileActivity.class);
                startActivity(in2);
            }
        });

        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prefs.edit().putString("access_token", "").commit();
                prefs.edit().putInt("signed", 0).commit();
                Intent in2 = new Intent(MainActivityList.this, LoginActivityNew.class);
                startActivity(in2);
                ActivityCompat.finishAffinity(MainActivityList.this);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mEventsList = new ArrayList<>();

        check();

        new LoadEventsTask(this).execute();

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isThereConnectivity(MainActivityList.this)) {
                    Intent in2 = new Intent(MainActivityList.this, ReportActivity.class);
                    startActivity(in2);
                } else {
                    Toast.makeText(MainActivityList.this, "No connectivity!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        check();
        new LoadEventsTask(this).execute();
    }

    @Override
    public void onRefresh() {
        //Toast.makeText(BeertijaHomeActivity.this, "refresing", Toast.LENGTH_SHORT).show();
        check();
        new LoadEventsTask(this).execute();
    }

    public void check(){
        if (mEventsList.size() == 0) {
            mEventsView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        } else {
            mEventsListAdapter = new MyEventListAdapter(this, mEventsList);
            mEventsView.setAdapter(mEventsListAdapter);
        }
    }


    @Override
    public void onResponseReceived(String response) {
        mEventsList = MyEvents.toJson(response);
        if(mEventsList.size() != 0) {
            mEventsListAdapter = new MyEventListAdapter(this, mEventsList);
            mEventsView.setAdapter(mEventsListAdapter);
            mEventsListAdapter.refreshList(mEventsList);
            mEventsView.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
        }
        //mEventsListAdapter.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }



}
