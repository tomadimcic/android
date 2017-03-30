package com.dis.fiademo.dialog;

import com.dis.fiademo.R;

import android.app.Dialog;  
import android.content.Context;  
import android.text.method.ScrollingMovementMethod;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.Window;  
import android.widget.Button;  
import android.widget.TextView;  

/** Class Must extends with Dialog */  
/** Implement onClickListener to dismiss dialog when OK Button is pressed */  
public class CustomizeDialog extends Dialog implements OnClickListener {  
    
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    Button cancelButton;
    Button okButton;
    boolean pressed = false;
    long startTime;
    String imei, ipv6Address, user, l, code;
    int flag;
    
    public CustomizeDialog(Context context, String imei, String ipv6Address, String user, String l, String code, int flag) {  
        super(context);  
        mContext = context; 
        this.imei = imei;
        this.ipv6Address = ipv6Address;
        this.user = user;
        this.l = l;
        this.code = code;
        this.flag = flag;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        cancelButton = (Button) v.findViewById(R.id.cancel_btn);
        okButton = (Button) v.findViewById(R.id.ok_btn);  
	mTitle = (TextView) findViewById(R.id.dialogTitle); 
	mMessage = (TextView) findViewById(R.id.message_txt);
	cancelButton.setOnClickListener(this); 
	okButton.setOnClickListener(this);
	
	
	/*okButton.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		
	    }
	});*/
    }  
    @Override  
    public void onClick(View v) {  
        /** When OK Button is clicked, dismiss the dialog */  
        if (v == cancelButton)  
            dismiss();  
        if (v == okButton){
            CustomizeDialogForRegistration customizeDialog = new CustomizeDialogForRegistration(mContext, imei, ipv6Address, user, l, code, flag);  
            customizeDialog.setTitle("Data for registration"); 
            //customizeDialog.setMessage("");
            customizeDialog.show();
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
