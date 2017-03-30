package com.iotwear.wear;

import android.app.Application;

import com.iotwear.wear.util.DataManager;

public class LedApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		DataManager.initSettingsManager(getApplicationContext());
	}
}
