package com.iotwear.wear.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.model.SwitchControl;
import com.iotwear.wear.task.SendSingleControlData;

public class CustomizeDialogSwitch extends Dialog implements OnClickListener {  
    Button okButton;  
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    Button switchButton1;
    Button switchButton2;
    boolean pressed = false;
    SwitchControl mItem;
    
    public CustomizeDialogSwitch(Context context, SwitchControl item) {  
        super(context);  
        mContext = context; 
        mItem = item;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_control_switch);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        switchButton1 = (Button) v.findViewById(R.id.button_turn_on);
        switchButton2 = (Button) v.findViewById(R.id.button_turn_off);  
	mTitle = (TextView) findViewById(R.id.dialogTitle);   
        okButton = (Button) findViewById(R.id.OkButtonSwitch); 
        okButton.setOnClickListener(this);  
        switchButton1.setOnClickListener(this);
        switchButton2.setOnClickListener(this);
        
	
    }  
    @Override  
    public void onClick(View v) {  
        /** When OK Button is clicked, dismiss the dialog */  
        if (v == okButton)  
            dismiss();
        if (v == switchButton1) { 
            mItem.setTurnedOn(true);
		new SendSingleControlData(mContext, mItem)
				.execute();
		dismiss();
        }
        if (v == switchButton2) { 
            mItem.setTurnedOn(false);
		new SendSingleControlData(mContext, mItem)
				.execute();
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
