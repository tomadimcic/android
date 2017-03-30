package com.iotwear.wear.gui.actionbar;

import android.app.Activity;

public class ActionBarHandlerFactory {

    public enum Mode {
	NORMAL, SLIDING, ONLY_TITLE
    }

    public static ActionBarHandler createActionBarHandler(Activity activity,
	    int titleResource, Mode mode) {
	String title = activity.getString(titleResource);
	ActionBarHandler abh = createActionBarHandler(activity, title, mode);
	return abh;
    }

    public static ActionBarHandler createActionBarHandler(Activity activity,
	    String title, Mode mode) {
	ActionBarHandler abh = null;
	switch (mode) {
	case ONLY_TITLE:
	    abh = new ActionBarNormalHandler(activity);
	    abh.showOnlyTitle();
	    break;

	case SLIDING:
	    abh = new ActionBarSlidingHandler(activity);
	    break;

	default:
	    abh = new ActionBarNormalHandler(activity);
	    break;
	}
	if (title != null)
	    abh.setTitle(title);
	return abh;
    }
}
