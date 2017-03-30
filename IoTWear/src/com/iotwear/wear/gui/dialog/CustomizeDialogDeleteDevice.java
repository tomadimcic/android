package com.iotwear.wear.gui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.ControllerGridActivity;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.SwitchControl;
import com.iotwear.wear.task.SendSingleControlData;
import com.iotwear.wear.util.DataManager;

public class CustomizeDialogDeleteDevice extends Dialog implements OnClickListener {  
    Button okButton;  
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    Button cancelBtn;
    Button deleteBtn;
    boolean pressed = false;
    PiDevice mItem;
    Activity mActivity;
    
    public CustomizeDialogDeleteDevice(Context context, Activity activity, PiDevice item) {  
        super(context);  
        mContext = context; 
        mItem = item;
        mActivity = activity;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_delete_device);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        cancelBtn = (Button) v.findViewById(R.id.cancel_btn);
        deleteBtn = (Button) v.findViewById(R.id.delete_btn);  
	mTitle = (TextView) findViewById(R.id.dialogTitle);
	mMessage = (TextView) findViewById(R.id.delete_device_message);
          
        cancelBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        
	
    }  
    @Override  
    public void onClick(View v) {  
        /** When OK Button is clicked, dismiss the dialog */  
        
        if (v == cancelBtn) { 
            dismiss();
        }
        if (v == deleteBtn) { 
            DataManager.getInstance().deleteDevice(
			mItem);
            dismiss();
            mActivity.finish();
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
        mTitle.setText(mContext.getResources().getString(titleId));  
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
        mMessage.setText(mContext.getResources().getString(messageId));  
        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
    }  
    
}  
