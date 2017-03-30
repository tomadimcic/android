package com.iotwear.wear.gui.actionbar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.iotwear.wear.R;


public abstract class ActionBarHandler {

    protected Activity activity;

    public enum PowerButtonStatus {
	IDLE, ON, OFF
    }

    
    ActionBarHandler(Activity activity, int actionBarLayoutResourceId) {
	this.activity = activity;
	FrameLayout fl = (FrameLayout)activity.findViewById(R.id.action_bar_frame);
	LayoutInflater.from(activity).inflate(actionBarLayoutResourceId, fl);
	initActionBar();
    }
    
    protected abstract void initActionBar();

    public abstract void setTitle(String title);
    
    public abstract void setLeftIcon(int resourceId);

    public abstract void showOnlyTitle();
    
    public abstract void addMenuItem(View view);
    
    
}
