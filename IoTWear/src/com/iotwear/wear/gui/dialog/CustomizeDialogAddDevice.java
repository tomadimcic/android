package com.iotwear.wear.gui.dialog;

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
import com.iotwear.wear.gui.AddDeviceActivity;
import com.iotwear.wear.gui.SettingsActivity;
import com.iotwear.wear.gui.adapter.ControllerEditAdapter;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.WiFiNetwork;
import com.iotwear.wear.task.GetDeviceListFromMaster;
import com.iotwear.wear.task.SendDataToPi;
import com.iotwear.wear.task.SendDeviceListToMaster;

public class CustomizeDialogAddDevice extends Dialog implements OnClickListener {  
    
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
    AddDeviceActivity mActivity;
    boolean mIsGet;
    PiDevice newPi;
    int sameNetworkDevices;
    EditText password;
    WiFiNetwork selectedNetwork;
    //CharSequence message;
    String ipAddressToSend;
    
    public CustomizeDialogAddDevice(AddDeviceActivity activity, String master, PiDevice device, int sameNetworkDevice, EditText password1, WiFiNetwork selectedNetwork1, String ipAddressToSend) {  
        super(activity);  
        mActivity = activity; 
        newPi = device;
        sameNetworkDevices = sameNetworkDevice;
        password = password1;
        selectedNetwork = selectedNetwork1;
        this.ipAddressToSend = ipAddressToSend;
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
        //textview.setText(message);
        
        ipAddressDialog.setText(master + ":" + (PiDevice.MASTER_PORT_NUMBER + sameNetworkDevices));
	mTitle = (TextView) findViewById(R.id.dialogTitle); 
	
        
    }  
    @Override  
    public void onClick(View v) { 
	
        if (v == okButton){
    	
    	newPi.setPort(Integer.parseInt(ipAddressDialog.getText().toString().split(":")[1]));
    	newPi.setLocalIp(ipAddressDialog.getText().toString().split(":")[0]);
    	
    	new SendDataToPi(mActivity, newPi, password
			.getText().toString(), selectedNetwork, ipAddressToSend).execute();
    	
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
        //mMessage.setText(message);  
        //this.message = message;
        textview.setText(message);
       // mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
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