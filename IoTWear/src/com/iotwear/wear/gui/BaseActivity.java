package com.iotwear.wear.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.slidingmenu.lib.SlidingMenu;

public abstract class BaseActivity extends Activity {

    protected FrameLayout contentFrame;

    private RelativeLayout devices, settings;
    private SlidingMenu menu;

    private ActionBarHandler actionBarHandler;

    public ActionBarHandler getActionBarHandler() {
	return actionBarHandler;
    }

    public abstract ActionBarHandler createActionBarHandler();

    @Override
    protected void onStart() {
	super.onStart();
	EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.content_frame);
	menu = new SlidingMenu(this);
	menu.setMode(SlidingMenu.LEFT);
	menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	menu.setBehindOffset(240);
	menu.setFadeDegree(0.45f); //bilo je 0.35

	menu.setMenu(R.layout.sidebar_navigation);

	contentFrame = (FrameLayout) findViewById(R.id.content_frame);

	devices = (RelativeLayout) menu
		.findViewById(R.id.sidebar_navigation_devices);
	settings = (RelativeLayout) menu
		.findViewById(R.id.sidebar_navigation_settings);

	devices.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (BaseActivity.this instanceof DeviceListActivity)
		    return;
		else {
		    Intent i = new Intent(BaseActivity.this,
			    DeviceListActivity.class);
		    startActivity(i);
		}
	    }
	});

	settings.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (BaseActivity.this instanceof SettingsActivity)
		    return;
		else {
		    Intent i = new Intent(BaseActivity.this,
			    SettingsActivity.class);
		    startActivity(i);
		}
	    }
	});

	menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT,
		R.id.content_frame);

	actionBarHandler = createActionBarHandler();
	if (actionBarHandler == null)
	    findViewById(R.id.action_bar_frame).setVisibility(View.GONE);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    public SlidingMenu getSlidingMenu() {
	return menu;
    }
    
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_back, R.anim.animation2_back);
    }

}
