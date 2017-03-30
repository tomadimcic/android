package com.iotwear.wear.gui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.SettingsActivity;
import com.iotwear.wear.gui.adapter.ControllerEditAdapter;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.GetDeviceListFromMaster;
import com.iotwear.wear.task.SendDeviceListToMaster;

public class CustomizeDialogGetDeviceList  extends Dialog implements OnClickListener {  
    
    Context mContext;  
    TextView mTitle = null;  
    TextView textview = null; 
    EditText ipAddressDialog = null; 
    TextView mMessage = null;
    View v = null;  
    ImageButton iconBtn1;
    ImageButton iconBtn2;
    ImageButton iconBtn3;
    ImageButton iconBtn4;
    ImageButton iconBtn5;
    ImageButton iconBtn6;
    boolean pressed = false;
    PiControl mItem;
    ControllerEditAdapter mAdapter;
    String mIpAddress;
    Button okButton;
    Activity mActivity;
    boolean mIsGet;
    
    public CustomizeDialogGetDeviceList(Activity activity, String master, boolean isGet) {  
        super(activity);  
        mActivity = activity; 
        mIsGet = isGet;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.info_dialog);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        textview=(TextView) v.findViewById(R.id.textmsg);
        ipAddressDialog = (EditText) v.findViewById(R.id.ip_address_dialog);
        okButton = (Button) findViewById(R.id.OkButtonInfo); 
        okButton.setOnClickListener(this);
        textview.setText("Please choose IP address for the device. The default address is:");
        
        ipAddressDialog.setText(master);
	mTitle = (TextView) findViewById(R.id.dialogTitle); 
	
        
    }  
    @Override  
    public void onClick(View v) { 
	
        if (v == okButton){
            if(mIsGet)
        	new GetDeviceListFromMaster(mActivity, ipAddressDialog.getText().toString()).execute();
            else
        	new SendDeviceListToMaster(mActivity, ipAddressDialog.getText().toString()).execute();
        }
        
        dismiss();
        
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
	textview.setText(message);
        //mMessage.setText(message);  
        //mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
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