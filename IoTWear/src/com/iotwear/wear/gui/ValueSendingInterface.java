package com.iotwear.wear.gui;

import android.app.Activity;

public interface ValueSendingInterface {
    public void setSending(boolean isSending);

    public int getCurrentValue();

    public Activity getActivity();
}
