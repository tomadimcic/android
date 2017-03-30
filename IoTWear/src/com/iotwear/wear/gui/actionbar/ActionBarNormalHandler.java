package com.iotwear.wear.gui.actionbar;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iotwear.wear.R;

public class ActionBarNormalHandler extends ActionBarHandler implements
		OnClickListener {

	private LinearLayout leftCorner;
	private RelativeLayout rlHeader;
	private LinearLayout rightContainer;
	private TextView headerTitle;

	private ImageView headerLeftIcon;

	ActionBarNormalHandler(Activity activity) {
		super(activity, R.layout.actionbar_for_old_device);
	}

	@Override
	protected void initActionBar() {
		rlHeader = (RelativeLayout) activity.findViewById(R.id.rlHeader);
		leftCorner = (LinearLayout) activity.findViewById(R.id.leftCorner);
		headerLeftIcon = (ImageView) activity.findViewById(R.id.headerLeftIcon);
		headerTitle = (TextView) activity.findViewById(R.id.headerTitle);
		rightContainer = (LinearLayout) activity.findViewById(R.id.rightContainer);
		rlHeader.setVisibility(View.VISIBLE);

		leftCorner.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftCorner:
			if(activity instanceof ActionbarLeftButtonAware) {
				((ActionbarLeftButtonAware) activity).onActionBarLeftButton();
			} else {
				activity.finish();
			}
			break;
		}
	}

	@Override
	public void setTitle(String title) {
		headerTitle.setText(title);
	}

	@Override
	public void setLeftIcon(int resourceId) {
		headerLeftIcon.setImageDrawable(activity.getResources().getDrawable(
				resourceId));
	}

	@Override
	public void showOnlyTitle() {
		headerLeftIcon.setVisibility(View.GONE);
	}
	
	@Override
	public void addMenuItem(View view){
	    rightContainer.addView(view);
	}
}
