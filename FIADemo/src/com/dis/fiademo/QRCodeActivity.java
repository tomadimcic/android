package com.dis.fiademo;

import com.dis.fiademo.db.DatabaseHandler;

import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QRCodeActivity extends Activity{
    
    private Button scan;
    String ipv6Address;
	String ipv6Address1;
	DatabaseHandler db;
	
	private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr);
         
        scan= (Button)findViewById(R.id.btnScan);
        prefs = PreferenceManager.getDefaultSharedPreferences(QRCodeActivity.this);
        db = new DatabaseHandler(this);
        
        ipv6Address1 = prefs.getString("ip","");
	ipv6Address = db.getIpv6Address();
	if(ipv6Address1.equals("4"))
	    ipv6Address = db.getIpv4Address();
         
        scan.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub  
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });
    }
 
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
           if (requestCode == 0) {
              if (resultCode == RESULT_OK) {
                  
                 String contents = intent.getStringExtra("SCAN_RESULT");
                 String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                 System.out.println("Usaoooooooooooooooo");
                 System.out.println(contents);
                 Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_SHORT).show();
                 
                 new SendQRDataTask().execute(contents);
              
                 // Handle successful scan
                                           
              } else if (resultCode == RESULT_CANCELED) {
                 // Handle cancel
                 Log.i("App","Scan unsuccessful");
              }
         }
      }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class SendQRDataTask extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... urls) {
	    System.out.println(urls[0]);
	    String url = ipv6Address + ":5683/nfc";
	    if(!url.contains("coap"))
		url = "coap://" + url;
	    Request req = new POSTRequest();
 		req.setURI(url);
 		//req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
 		//req.setPayload(urls[0].substring(3));
 		System.out.println(urls[0]);
 		req.setPayload("354909050056732");
 		req.enableResponseQueue(true);
 		
 		try {
 			req.execute();
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			return "";
 		}
 		Response res = null;
 		String response = "";
		try {
			res = req.receiveResponse();
			System.out.println(res.getCode());
			//response = res.getPayloadString();
			if(res.getCode() == 68 || res.getCode() == 69
				 || res.getCode() == 65 || res.getCode() == 67)
			    response = res.getPayloadString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
        return response;
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("usaoooooooooooooooooooooooooooooo");
		if (!result.equals(null) && !result.equals("")){
		    if (!result.startsWith("http://") && !result.startsWith("https://"))
			result = "http://" + result;
		    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
		    startActivity(browserIntent);
			//setNoteBody("Message successfuly sent!", 1);
		}
		else
		    Toast.makeText(getApplicationContext(),"Not sent",Toast.LENGTH_SHORT).show();
		System.gc();
	}
}

}
