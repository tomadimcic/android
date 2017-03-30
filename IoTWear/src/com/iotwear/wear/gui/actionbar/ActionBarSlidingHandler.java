package com.iotwear.wear.gui.actionbar;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.BaseActivity;
import com.slidingmenu.lib.SlidingMenu;

public class ActionBarSlidingHandler extends ActionBarHandler implements
		OnClickListener {

	private ImageView leftIcon;
	private TextView titleView;
	private LinearLayout leftContainer;
	private LinearLayout rightContainer;

	ActionBarSlidingHandler(Activity activity) {
		super(activity, R.layout.actionbar_sliding_menu);
	}

	@Override
	protected void initActionBar() {
		leftIcon = (ImageView) activity.findViewById(R.id.actionbar_left_icon);
		titleView = (TextView) activity.findViewById(R.id.actionbar_title);
		leftContainer = (LinearLayout) activity
				.findViewById(R.id.actionbar_left_container);

		leftContainer.setOnClickListener(this);
		rightContainer = (LinearLayout) activity.findViewById(R.id.rightContainer);

	}

	@Override
	public void onClick(View v) {
		SlidingMenu menu = ((BaseActivity) activity).getSlidingMenu();

		if (menu.isMenuShowing() || menu.isSecondaryMenuShowing()) {
			menu.showContent();
		} else {
			if (v.getId() == R.id.actionbar_left_container) {
				menu.showMenu();
			}
		}
	}

	@Override
	public void setTitle(String title) {
		titleView.setText(title);
	}

	@Override
	public void setLeftIcon(int resourceId) {
		leftIcon.setImageDrawable(activity.getResources().getDrawable(
				resourceId));
	}

	@Override
	public void showOnlyTitle() {
		leftIcon.setVisibility(View.GONE);
	}
	
	@Override
	public void addMenuItem(View view){
	    rightContainer.addView(view);
	}

}
