package com.iotwear.wear.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.FavouritesAnimationAdapter;
import com.iotwear.wear.gui.adapter.FavouritesColorAdapter;
import com.iotwear.wear.gui.dialog.CustomizeDialogDeleteDevice;
import com.iotwear.wear.gui.dialog.CustomizeDialogFavourites;
import com.iotwear.wear.model.Animation;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.led.SetAnimationTask;
import com.iotwear.wear.task.led.SetColorTask;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class FavouritesActivity extends BaseActivity implements
	OnClickListener, OnItemClickListener, ValueSendingInterface {

    public static final int ANIMATIONS_MODE = 1;
    public static final int COLORS_MODE = 2;

    PiControl controller;

    ListView contentList;
    Button colors, animations;

    FavouritesColorAdapter colorAdapter;
    FavouritesAnimationAdapter animationAdapter;

    int currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	View content = LayoutInflater.from(this).inflate(
		R.layout.activity_favourites, null);
	contentFrame.addView(content);

	controller = (PiControl) getIntent().getSerializableExtra(
		Constants.EXTRA_CONTROLLER);
	if (controller == null) {
	    Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
	    Intent i = new Intent(this, DeviceListActivity.class);
	    startActivity(i);
	    finish();
	} else {
	    getActionBarHandler().setTitle(controller.getName());
	}

	contentList = (ListView) findViewById(R.id.favouritesList);
	colors = (Button) findViewById(R.id.colors);
	animations = (Button) findViewById(R.id.animations);

	colors.setOnClickListener(this);
	animations.setOnClickListener(this);

	colorAdapter = new FavouritesColorAdapter(this);
	animationAdapter = new FavouritesAnimationAdapter(this);

	contentList.setOnItemClickListener(this);
	contentList.setAdapter(colorAdapter);
	currentMode = COLORS_MODE;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	if (currentMode == ANIMATIONS_MODE) {
	    Animation selectedAnimation = (Animation) animationAdapter
		    .getItem(position);
	    CustomizeDialogFavourites customizeDialog = new CustomizeDialogFavourites(this, controller, true);  
	    customizeDialog.setAnimAdapter(animationAdapter);
	    customizeDialog.setSelectedAnimation(selectedAnimation);
            customizeDialog.setTitle("Animation");   
            customizeDialog.setMessage("Do you want to activate this animation or to delete it?");
            customizeDialog.show();
	} else {
	    final int selectedColor = (Integer) colorAdapter.getItem(position);
	    CustomizeDialogFavourites customizeDialog = new CustomizeDialogFavourites(this, controller, false);  
	    customizeDialog.setColorAdapter(colorAdapter);
	    customizeDialog.setSelectedColor(selectedColor);
            customizeDialog.setTitle("Color");   
            customizeDialog.setMessage("Do you want to activate this color or to deleete it?");
            customizeDialog.show();
	    
	}
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.colors:
	    currentMode = COLORS_MODE;
	    contentList.setAdapter(colorAdapter);
	    break;
	case R.id.animations:
	    currentMode = ANIMATIONS_MODE;
	    contentList.setAdapter(animationAdapter);
	    break;
	}
	contentList.invalidate();

    }

    @Override
    public void setSending(boolean isSending) {
	// TODO Auto-generated method stub

    }

    @Override
    public int getCurrentValue() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public Activity getActivity() {
	return this;
    }

    @Override
    public ActionBarHandler createActionBarHandler() {
	return ActionBarHandlerFactory.createActionBarHandler(this,
		"Favourites", Mode.NORMAL);
    }
}
