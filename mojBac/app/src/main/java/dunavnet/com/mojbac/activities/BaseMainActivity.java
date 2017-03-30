package dunavnet.com.mojbac.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.activities.fragments.EventsListFragment;
import dunavnet.com.mojbac.activities.fragments.MapFragment;
import dunavnet.com.mojbac.util.SettingsConstants;
import dunavnet.com.mojbac.util.Util;

public class BaseMainActivity extends FragmentActivity implements View.OnClickListener {

    protected FrameLayout contentFrame;
    private RelativeLayout listView, mapView;
    public ImageView profileImage, logoutImage;
    private EventsListFragment eventsListFragment;
    private MapFragment mapFragment;
    public Button report;
    public SharedPreferences prefs;
    public int tab = 0; // 0 - lista; 1 - mapa

    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.3f);
    AlphaAnimation alphaAnimationDefault = new AlphaAnimation(0.3f,1.0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentFrame = (FrameLayout) findViewById(R.id.action_bar_frame);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        listView = (RelativeLayout) findViewById(R.id.listView);
        mapView = (RelativeLayout) findViewById(R.id.mapView);
        profileImage = (ImageView) findViewById(R.id.imageView);
        logoutImage = (ImageView) findViewById(R.id.logout);

        report = (Button) findViewById(R.id.report_button);

        if (eventsListFragment == null)
            eventsListFragment = EventsListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.action_bar_frame, eventsListFragment).commit();

        listView.setOnClickListener(this);
        mapView.setOnClickListener(this);

        profileImage.setOnClickListener(this);
        logoutImage.setOnClickListener(this);

        report.setOnClickListener(this);

        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(30);

        alphaAnimationDefault.setFillAfter(true);
        alphaAnimationDefault.setDuration(30);

        mapView.startAnimation(alphaAnimation);
        listView.startAnimation(alphaAnimationDefault);
    }

    @Override
    public void onBackPressed() {
        if(tab == 1 && mapFragment != null && mapFragment.isInfoWindowOpen())
            mapFragment.hideInfoWindow();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v == listView){
            mapView.startAnimation(alphaAnimation);
            listView.startAnimation(alphaAnimationDefault);
            if (eventsListFragment == null)
                eventsListFragment = EventsListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.action_bar_frame, eventsListFragment)
                    .commit();
            tab = 0;

        }
        if(v == mapView){
            listView.startAnimation(alphaAnimation);
            mapView.startAnimation(alphaAnimationDefault);
            if (mapFragment == null)
                mapFragment = MapFragment.newInstance(eventsListFragment.getEventList());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.action_bar_frame, mapFragment)
                    .commit();
            tab = 1;
        }
        if(v == profileImage){
            Intent in2 = new Intent(BaseMainActivity.this, ProfileActivity.class);
            startActivity(in2);
        }
        if(v == logoutImage){
            prefs.edit().putString("access_token", "").commit();
            Intent in2 = new Intent(BaseMainActivity.this, LoginActivityNew.class);
            startActivity(in2);
            ActivityCompat.finishAffinity(BaseMainActivity.this);
        }
        if(v == report){
            if (Util.isThereConnectivity(BaseMainActivity.this)) {
                Intent in2 = new Intent(BaseMainActivity.this, ReportActivity.class);
                startActivity(in2);
            } else {
                Toast.makeText(BaseMainActivity.this, "No connectivity!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
