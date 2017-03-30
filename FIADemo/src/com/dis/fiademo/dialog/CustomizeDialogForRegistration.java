package com.dis.fiademo.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
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

public class CustomizeDialogForRegistration extends Dialog implements OnClickListener {  
    
    Context mContext;  
    TextView mTitle = null;  
    //TextView mMessage = null;  
    View v = null;  
    Button cancelButton;
    Button okButton;
    boolean pressed = false;
    long startTime;
    String imei, ipv6Address, user, l, c;
    TextView imeiTV = null;  
    TextView location = null;
    TextView code = null;
    EditText username = null;
    EditText url = null;
    EditText dname = null;
    EditText dprotocol = null;
    EditText ipv6address = null;
    EditText vname = null;
    EditText vvalue = null;
    EditText vunit = null;
    int flag;
    
    public CustomizeDialogForRegistration(Context context, String imei, String ipv6Address, String user, String l, String c, int flag) {  
        super(context);  
        mContext = context; 
        this.imei = imei;
        this.ipv6Address = ipv6Address;
        this.user = user;
        this.l = l;
        this.c = c;
        this.flag = flag;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_for_registration);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        cancelButton = (Button) v.findViewById(R.id.cancel_btn);
        okButton = (Button) v.findViewById(R.id.ok_btn);  
	mTitle = (TextView) findViewById(R.id.dialogTitle); 
	//mMessage = (TextView) findViewById(R.id.message_txt);
	imeiTV = (TextView) findViewById(R.id.imei); 
	location = (TextView) findViewById(R.id.location);
	code = (TextView) findViewById(R.id.code);
	username = (EditText) findViewById(R.id.username);
	url = (EditText) findViewById(R.id.url);
	dname = (EditText) findViewById(R.id.dev_name);
	dprotocol = (EditText) findViewById(R.id.dev_protocol);
	ipv6address = (EditText) findViewById(R.id.ipv6_address);
	vname = (EditText) findViewById(R.id.var_name);
	vvalue = (EditText) findViewById(R.id.var_value);
	vunit = (EditText) findViewById(R.id.var_unit);
	cancelButton.setOnClickListener(this); 
	okButton.setOnClickListener(this);
	
	try {
	    imeiTV.setText(imei);
	    location.setText(l);
	    username.setText(user);
	    code.setText(c);
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
        if (v == cancelButton)  
            dismiss();  
        if (v == okButton){
            new RegisterCodeTask().execute();
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
    
    public class RegisterCodeTask extends AsyncTask<Void, Void, String> {

	protected void onPreExecute() {

	}

	protected String doInBackground(Void... arg0) {
	    String response = null;
	    if (!ipv6Address.contains("coap"))
		ipv6Address = "coap://" + ipv6Address;
	    if(flag == 0)
		ipv6Address = ipv6Address + ":5683/nfc";
	    else
		ipv6Address = ipv6Address + ":5683/qrcode";
	    Request req = new GETRequest();
	    req.setURI(ipv6Address);
	    JSONObject json = new JSONObject();
		try {
		    json.put("user", username.getText().toString());
		    json.put("imei", imei);
		    json.put("location", l);
		    json.put("code", c);
		    json.put("url", url.getText().toString());
		    json.put("dname", dname.getText().toString());
		    json.put("dprotocol", dprotocol.getText().toString());
		    json.put("ipv6addr", ipv6address.getText().toString());
		    json.put("vname", vname.getText().toString());
		    json.put("vvalue", vvalue.getText().toString());
		    json.put("vunit", vunit.getText().toString());
		    
		} catch (JSONException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		System.out.println(json.toString());
		req.setPayload(json.toString());
	    req.enableResponseQueue(true);

	    try {
		req.execute();
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    }
	    Response res = null;
	    try {
		res = req.receiveResponse();
		//System.out.println(res.getCode());
		response = res.getPayloadString();
		System.out.println(response);
		if(res.getCode() == 68 || res.getCode() == 69
		 || res.getCode() == 65 || res.getCode() == 67)
		 response = "The code is succesfully registered!";
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "Response invalid!";
	    }
	    return response;

	}

	@Override
	protected void onPostExecute(String result) {
	    Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();
	    dismiss();
	    

	}
    }
}  