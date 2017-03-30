package com.iotwear.wear.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.FavouritesLampAnimatioAdapter;
import com.iotwear.wear.gui.adapter.FavouritesPictureAdapter;
import com.iotwear.wear.model.LampControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.SendToLamp;
import com.iotwear.wear.util.Constants;

public class FavouritesLampActivity extends BaseActivity implements
	OnClickListener, OnItemClickListener, ValueSendingInterface {

    public static final int ANIMATIONS_MODE = 1;
    public static final int COLORS_MODE = 2;

    PiControl controller;

    ListView contentList;
    Button colors, animations;

    FavouritesPictureAdapter pictureAdapter;
    FavouritesLampAnimatioAdapter animationAdapter;

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

	pictureAdapter = new FavouritesPictureAdapter(this);
	animationAdapter = new FavouritesLampAnimatioAdapter(this);

	contentList.setOnItemClickListener(this);
	contentList.setAdapter(pictureAdapter);
	currentMode = COLORS_MODE;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	if (currentMode == ANIMATIONS_MODE) {
	    String selectedAnimation = (String) animationAdapter.getItem(position);
	    ((LampControl) controller).setValue("anim=" + selectedAnimation);
	    new SendToLamp(this, controller, 2).execute();
	} else {
	    String selectedPicture = (String) pictureAdapter.getItem(position);
	    ((LampControl) controller).setValue("pic=" + selectedPicture);
	    new SendToLamp(this, controller, 1).execute();
	    
	}
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.colors:
	    currentMode = COLORS_MODE;
	    contentList.setAdapter(pictureAdapter);
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
