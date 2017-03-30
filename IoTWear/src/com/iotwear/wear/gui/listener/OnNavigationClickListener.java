package com.iotwear.wear.gui.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.iotwear.wear.gui.AnimationActivity;
import com.iotwear.wear.gui.ColorPickerActivity;
import com.iotwear.wear.gui.DeviceListActivity;
import com.iotwear.wear.gui.FavouritesActivity;
import com.iotwear.wear.gui.SettingsActivity;

public class OnNavigationClickListener implements OnItemClickListener {

    Context context;

    public OnNavigationClickListener(Context context) {
	this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position,
	    long id) {
	Intent i = null;

	switch (position) {
	case 0:
	    i = new Intent(context, DeviceListActivity.class);
	    break;
	case 1:
	    i = new Intent(context, ColorPickerActivity.class);
	    break;
	case 2:
	    i = new Intent(context, AnimationActivity.class);
	    break;
	case 3:
	    i = new Intent(context, FavouritesActivity.class);
	    break;
	case 4:
	    i = new Intent(context, SettingsActivity.class);
	    break;
	}
	if (i != null)
	    context.startActivity(i);

    }

}
