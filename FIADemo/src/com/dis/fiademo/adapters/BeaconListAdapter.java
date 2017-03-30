package com.dis.fiademo.adapters;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.ethz.inf.vs.californium.coap.DELETERequest;
import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.dis.fiademo.AddBeaconActivity;
import com.dis.fiademo.R;
import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.model.Beacon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BeaconListAdapter extends BaseAdapter {
    
    private ArrayList<Beacon> beaconList;
    Activity activity;
    DatabaseHandler db;
    String ipAddress;
    String fourorsix;;
    private SharedPreferences prefs;
    
    public BeaconListAdapter(Activity activity, ArrayList<Beacon> beaconList, DatabaseHandler db) {
	this.activity = activity;
	this.beaconList = beaconList;
	this.db = db;
	prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	
	fourorsix = prefs.getString("ip","");
	if(fourorsix.equals("4"))
	    ipAddress = db.getIpv4Address();
	else
	    ipAddress = "[" + db.getIpv6Address() + "]";
    }

    @Override
	public int getCount() {
	    return beaconList.size();
	}

	@Override
	public Object getItem(int position) {
	    return beaconList.get(position);
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    final Beacon item = beaconList.get(position);
	    final int pos = position;
	    if (convertView == null)
		convertView = View.inflate(activity,
			R.layout.adapter_beacon_item, null);

	    TextView beaconTitle = (TextView) convertView.findViewById(R.id.beacon_title);
	    TextView beaconGPSPosition = (TextView) convertView.findViewById(R.id.textView2);
	    TextView beaconDistance = (TextView) convertView.findViewById(R.id.textView4);
	    TextView beaconEmail= (TextView) convertView.findViewById(R.id.textView6);
	    TextView beaconSMS = (TextView) convertView.findViewById(R.id.textView8);
	    TextView beaconURL = (TextView) convertView.findViewById(R.id.textView10);
	    Button activateButton = (Button) convertView.findViewById(R.id.activate_button);
	    Button deactivateButton = (Button) convertView.findViewById(R.id.deactivate_button);
	    Button editButton = (Button) convertView.findViewById(R.id.edit_button);
	    Button deleteButton = (Button) convertView.findViewById(R.id.deletee_button);
	    
	    if(item.getActivated().equals("0")){
		activateButton.setVisibility(View.VISIBLE);
		deactivateButton.setVisibility(View.GONE);
	    }else{
		activateButton.setVisibility(View.GONE);
		deactivateButton.setVisibility(View.VISIBLE);
	    }
	    
	    beaconTitle.setText(item.getTitle());
	    beaconGPSPosition.setText(item.getGps());
	    beaconDistance.setText(Integer.toString(item.getDistance()));
	    beaconEmail.setText(item.getEmail());
	    beaconSMS.setText(item.getSms());
	    beaconURL.setText(item.getUrlCMS() + " " + item.getIPv4CMS());
	    
	    editButton.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	            Intent i = new Intent(activity, AddBeaconActivity.class);
	    	    i.putExtra("beacon", item);
	    	    activity.startActivity(i);
	    	
	        }
	    });
	    
	    deleteButton.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	            String result = "";
	            beaconList.remove(pos);
	            
	            DeleteBeaconTask crb = new DeleteBeaconTask();
	            try {
	    	    result = crb.execute(item.getId()).get();
	    		} catch (InterruptedException e) {
	    	    // TODO Auto-generated catch block
	    	    e.printStackTrace();
        	    	} catch (ExecutionException e) {
        	    	    // TODO Auto-generated catch block
        	    	    e.printStackTrace();
        	    	}
	            if(!result.equals("")){
        	            db.deleteBeaconById(item.getId());
        	            notifyDataSetChanged();
	            }
	    	
	        }
	    });
	    
	    activateButton.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	            item.setActivated("1");
	            item.setSent("");
	            db.updateBeacon(item);
	            notifyDataSetChanged();
	    	
	        }
	    });
	    
	    deactivateButton.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	            item.setActivated("0");
	            db.updateBeacon(item);
	            notifyDataSetChanged();
	    	
	        }
	    });

	    /*
	     * ToggleButton powerButton = (ToggleButton)
	     * convertView.findViewById(R.id.powerButton);
	     * powerButton.setChecked(item.isTurnedOn());
	     * powerButton.setOnCheckedChangeListener(new
	     * OnCheckedChangeListener() {
	     * 
	     * @Override public void onCheckedChanged(CompoundButton buttonView,
	     * boolean isChecked) { new
	     * SendColorToDevice(DeviceListActivity.this,
	     * item).execute(Color.WHITE); } });
	     */

	    return convertView;
	}
	
	private class DeleteBeaconTask extends AsyncTask<String, Void, String> {
		
		@Override
		    protected void onPreExecute() {
			super.onPreExecute();
			//progress = ProgressDialog.show(AddBeaconActivity.this, "Sending data",
			//	"Registring Beacon...");
		    }
		
		@Override
		protected String doInBackground(String... urls) {
		    System.out.println(urls[0]);
		    String url = ipAddress + ":5683/beacon";
		    if(!url.contains("coap"))
			url = "coap://" + url;
		    Request req = new DELETERequest();
	 		req.setURI(url);
	 		//req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
	 		//System.out.println(json.toString());
	 		req.setPayload(urls[0]);
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
				response = res.getPayloadString();
				if(res.getCode() == 68 || res.getCode() == 69
					 || res.getCode() == 65 || res.getCode() == 66 || res.getCode() == 67)
				    response = "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	        return response;
		}

		@Override
		protected void onPostExecute(String result) {
		    //progress.dismiss();
			System.out.println("usaoooooooooooooooooooooooooooooo");
			if (!result.equals(null) && !result.equals("")){
			    Toast.makeText(activity,"Beacon deleted",Toast.LENGTH_SHORT).show();
			}
			else
			    Toast.makeText(activity,"Beacon not deleted. Please try again!",Toast.LENGTH_SHORT).show();

		}
	    }

}
