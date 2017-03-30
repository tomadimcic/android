package com.dis.fiademo.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.dis.fiademo.R;

public class CustomizeDialogInfo extends Dialog implements OnClickListener {  
    
    Context mContext;  
    TextView mTitle = null;  
    //TextView mMessage = null;  
    View v = null;  
    Button urlButton;
    Button okButton;
    boolean pressed = false;
    long startTime;
    TextView devName = null;  
    TextView devprotocol = null;
    TextView addr = null;
    TextView addrIPv6 = null;
    TextView varname = null;
    TextView varvalue = null;
    TextView varunit = null;
    int flag;
    JSONObject json = null;
    String dname, dprotocol, address, ipv6addr, vname, vvalue, vunit;
    
    public CustomizeDialogInfo(Context context, String result) {  
        super(context);  
        mContext = context; 
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_info);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        urlButton = (Button) v.findViewById(R.id.url_btn);
        okButton = (Button) v.findViewById(R.id.ok_btn);  
	mTitle = (TextView) findViewById(R.id.dialogTitle); 
	//mMessage = (TextView) findViewById(R.id.message_txt);
	devName = (TextView) findViewById(R.id.dname); 
	devprotocol = (TextView) findViewById(R.id.dprotocol);
	addr = (TextView) findViewById(R.id.address);
	addrIPv6 = (TextView) findViewById(R.id.ipv6addr);
	varname = (TextView) findViewById(R.id.vname);
	varvalue = (TextView) findViewById(R.id.vvalue);
	varunit = (TextView) findViewById(R.id.vunit);
	urlButton.setOnClickListener(this); 
	okButton.setOnClickListener(this);
	
	try {
	    json = new JSONObject(result);
	    dname = json.getString("dname");
	    dprotocol = json.getString("dprotocol");
	    address = json.getString("address");
	    if (!address.startsWith("http://")
		    && !address.startsWith("https://"))
		address = "http://" + address;
	    ipv6addr = json.getString("ipv6addr");
	    vname = json.getString("vname");
	    vvalue = json.getString("vvalue");
	    vunit = json.getString("vunit");
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	try {
	    devName.setText(dname);
	    devprotocol.setText(dprotocol);
	    addr.setText(address);
	    addrIPv6.setText(ipv6addr);
	    varname.setText(vname);
	    varvalue.setText(vvalue);
	    varunit.setText(vunit);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	
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
        if (v == urlButton)  {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
   		  Uri.parse(address)); 
            mContext.startActivity(browserIntent);
            dismiss();  
        }
        if (v == okButton){
            
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
    /*public void setMessage(CharSequence message) {  
        mMessage.setText(message);  
        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
    } */ 
    /**  
     * Set the message text for this dialog's window. The text is retrieved from the resources with the supplied  
     * identifier.  
     *   
     * @param messageId  
     *      - the message's text resource identifier <br>  
     * @see <b>Note : if resourceID wrong application may get crash.</b><br>  
     *   Exception has not handle.  
     */  
    /*public void setMessage(int messageId) {  
        mMessage.setText(mContext.getResources().getString(messageId));  
        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
    }  */
    
}  