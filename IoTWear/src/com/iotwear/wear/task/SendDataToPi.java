package com.iotwear.wear.task;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.ControllerGridActivity;
import com.iotwear.wear.gui.dialog.CustomizeDialogSwitch;
import com.iotwear.wear.model.GroupControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.WiFiNetwork;
import com.iotwear.wear.util.DataManager;
import com.iotwear.wear.util.NetworkUtil;

public class SendDataToPi extends AsyncTask<Void, Void, String> {

    Activity activity;
    ProgressDialog progress;
    PiDevice newPi;
    String wifiPassword;
    WiFiNetwork network;
    String ipAddressToSend;
    public static final String TYPE = "t";
    public static final String ID = "id";

    public SendDataToPi(Activity activity, PiDevice newPi, String wifiPassword,
	    WiFiNetwork network, String ipAddressToSend) {
	this.activity = activity;
	this.newPi = newPi;
	this.wifiPassword = wifiPassword;
	this.network = network;
	this.ipAddressToSend = ipAddressToSend;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	progress = ProgressDialog.show(activity, "Sync",
		"Sending network data to device...");
    }

    @Override
    protected String doInBackground(Void... arg0) {

	HttpClient httpclient = NetworkUtil.getDefaultHttpClient();

	// if (firstDevice.isChecked()) {
	// newPi.setLocalIp(PiDevice.DEFAULT_IP_ADDRESS);
	// newPi.setPort(PiDevice.DEFAULT_PORT_NUMBER);
	// } else {

	// }
	String ssid = newPi.getSsid();
	ssid = ssid.replaceAll(" ", "%20");

	String query = "dhcp=0&ip=" + newPi.getLocalIp() + "&port=" + newPi.getPort() + "&ssid=" + ssid;
	if(!wifiPassword.equals(""))
	    query += "&password=" + wifiPassword;
	/*HttpPost httpPost = new HttpPost("http://"
		+ ipAddressToSend + "/setup?ssid="
		+ ssid + "&password=" + wifiPassword + "&ip="
		+ newPi.getLocalIp() + "&port=" + newPi.getPort() + "&type="
		+ network.getScanResultSecurity() + "&capabilities="
		+ network.capabilities);*/
	HttpGet httpGet = new HttpGet("http://"
		+ ipAddressToSend + "/setup?" + query);
	try {
	    // httpPost.setEntity(new StringEntity("ssid=" +
	    // spinner.getSelectedItem()
	    // .toString() + "&password=" + password.getText().toString()));
	    // Execute HTTP Post Request
	    HttpResponse response = httpclient.execute(httpGet);
	    int statusCode = response.getStatusLine().getStatusCode();
	    if (statusCode == HttpStatus.SC_OK) {
		return NetworkUtil.inputStreamToString(
			response.getEntity().getContent()).toString();
	    }

	} catch (ClientProtocolException e) {
	    return null;
	} catch (IOException e) {
	    return null;
	}
	return null;
    }

    @Override
    protected void onPostExecute(String result) {
	progress.dismiss();
	//result = "[{\"p\":61616,\"s\":\"HG530\" ,\"url\":\"controller17.no-ip.biz\",\"ip\":\"192.168.1.135\",\"n\":\"controller17\",\"m\":\"00003052F511\",\"c\":[{\"t\":0,\"id\":0,\"n\":\"led0\"}]}]";
	if (result != null) {
	    try {
		JSONArray ja = new JSONArray(result);
		JSONObject resJson = new JSONObject(ja.getJSONObject(0).toString());

		String url = resJson.getString(PiDevice.URL);
		newPi.setUrl(url + ":" + newPi.getPort());
		newPi.setMac(resJson.getString(PiDevice.MAC));

		JSONArray controllers = resJson.getJSONArray(PiDevice.CONTROLLERS);
		ArrayList<PiControl> piclist = new ArrayList<PiControl>();
		newPi.setControlList(piclist);
		for(int i = 0; i < controllers.length(); i++){
		    JSONObject pic = controllers.getJSONObject(i); 
		    //newPi.getControlList().add(PiControl.buildFromJson(pic, newPi, 1));
		    
		    int type = pic.optInt(TYPE, -1);
		    String id = pic.optString(ID);
		    if(type != 5 && type != 6 && id.contains("-")){
			int from = Integer.parseInt(id.split("-")[0]);
			int to = Integer.parseInt(id.split("-")[1]);
			for(int j = from; j<=to; j++){
				PiControl ctrl = PiControl.buildFromJsonNew(pic, newPi, j);
				if(!(ctrl instanceof GroupControl)){
				    if (ctrl != null)
					newPi.getControlList().add(ctrl);
				}
			}
		    }else{
			PiControl ctrl = PiControl.buildFromJson(pic, newPi, 0);
			if(!(ctrl instanceof GroupControl)){
			    if (ctrl != null)
				newPi.getControlList().add(ctrl);
			}
		    }
		}
		
		DataManager.getInstance().addDevice(newPi);
		
		CustomDialog customizeDialog = new CustomDialog();  
	            customizeDialog.setTitle("Sync success");   
	            customizeDialog.show();
	    } catch (JSONException e) {
		Toast.makeText(activity, "Error occured. Please try again.",
			Toast.LENGTH_SHORT).show();
	    }
	}

	else {
	    Toast.makeText(activity, "Error occured. Please try again.",
		    Toast.LENGTH_SHORT).show();
	}
    }
    
    public class CustomDialog extends Dialog implements OnClickListener {  
	    
	    Context mContext;  
	    TextView mTitle = null;  
	    TextView textview = null; 
	    EditText nameOfDevice = null; 
	    TextView mMessage = null;
	    View v = null;  
	    Button saveButton;
	    boolean mIsGet;
	    
	    public CustomDialog() {  
	        super(activity);  
	        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        /** Design the dialog in main.xml file */  
	        setContentView(R.layout.dialog_new_device_added);  
	        v = getWindow().getDecorView();  
	        v.setBackgroundResource(android.R.color.transparent);  
	        textview=(TextView) v.findViewById(R.id.textmsg);
	        nameOfDevice = (EditText) v.findViewById(R.id.nameOfDevice);
	        saveButton = (Button) findViewById(R.id.saveButtonInfo); 
	        saveButton.setOnClickListener(this);
	        textview.setText("Device successfully synced. Please name this device.");
	        
		mTitle = (TextView) findViewById(R.id.dialogTitle); 
		
	        
	    }  
	    @Override  
	    public void onClick(View v) { 
		
	        if (v == saveButton){
	            if (nameOfDevice.getText().toString().length() > 0) {
			    newPi.setName(nameOfDevice.getText()
				    .toString());
			    DataManager.getInstance()
				    .addDevice(newPi);
			    dismiss();
			    activity.finish();
			    // showAnotherDialog();
			} else {
			    Toast.makeText(
				    activity,
				    "You must name this device",
				    Toast.LENGTH_SHORT).show();
			}
	        }
	        
	    }  
	    @Override  
	    public void setTitle(CharSequence title) {  
	        super.setTitle(title);  
	        mTitle.setText("Choose icon");  
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
}
