package com.iotwear.wear.gui.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.LoginActivity;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.SendSaveState;

public class CustomizeDialogSave extends Dialog implements OnClickListener {  
    Button okButton;  
    Context activity;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    Button cancelBtn;
    Button saveBtn;
    ArrayList<PiDevice> deviceList;
    LinearLayout ll;
    ArrayList<CheckBox> cbList;
    
    public CustomizeDialogSave(Activity activity, ArrayList<PiDevice> deviceList) {  
        super(activity);  
        this.activity = activity; 
        this.deviceList = deviceList;
        cbList = new ArrayList<CheckBox>();
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_save_device);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        cancelBtn = (Button) v.findViewById(R.id.cancel_btn);
        saveBtn = (Button) v.findViewById(R.id.save_btn);  
	mTitle = (TextView) findViewById(R.id.dialogTitle);
	mMessage = (TextView) findViewById(R.id.save_state_message);
	ll = (LinearLayout) findViewById(R.id.save_state_lin_layout);
	
	for (PiDevice piDevice : deviceList) {
	    CheckBox cb = new CheckBox(activity);
            cb.setText(piDevice.getName());
            ll.addView(cb);
            cbList.add(cb);
	}
	
          
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        
	
    }  
    @Override  
    public void onClick(View v) {  
        /** When OK Button is clicked, dismiss the dialog */  
        
        if (v == cancelBtn) { 
            dismiss();
        }
        if (v == saveBtn) { 
	    for (int i = 0; i < cbList.size(); i++) {
		if(cbList.get(i).isChecked())
		    new SendSaveState(activity, deviceList.get(i)).execute();
	    }
	    dismiss();
        }
    }  
    @Override  
    public void setTitle(CharSequence title) {  
        super.setTitle(title);  
        mTitle.setText(title);  
    }  
    @Override  
    public void setTitle(int titleId) {  
        super.setTitle(titleId);  
        mTitle.setText(activity.getResources().getString(titleId));  
    }  
    /**  
     * Set the message text for this dialog's window.  
     *   
     * @param message  
     *      - The new message to display in the title.  
     */  
    public void setMessage(CharSequence message) {  
        mMessage.setText(message);  
        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
    }  
    /**  
     * Set the message text for this dialog's window. The text is retrieved from the resources with the supplied  
     * identifier.  
     *   
     * @param messageId  
     *      - the message's text resource identifier <br>  
     * @see <b>Note : if resourceID wrong application may get crash.</b><br>  
     *   Exception has not handle.  
     */  
    public void setMessage(int messageId) {  
        mMessage.setText(activity.getResources().getString(messageId));  
        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
    }  
    
}  
