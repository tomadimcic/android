package com.dis.fiademo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

import com.dis.fiademo.AddBeaconActivity.MyLocationListener;
import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.dialog.CustomizeDialog;
import com.dis.fiademo.dialog.CustomizeDialogInfo;

import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NFCIoT6Activity extends Activity {

    private static final String TAG = "stickynotes";
	private boolean mResumed = false;
	private boolean mWriteMode = false;
	NfcAdapter mNfcAdapter;
	TextView mNote;
	EditText ipAddress;
	private  SoundPool mSoundPool;
	private  HashMap mSoundPoolMap;
	private  AudioManager  mAudioManager;
	private  Context mContext;
	LayoutInflater inflater;
	View view;
	TextView textview;
	EditText ipAddressDialog;
	String ipv6Address;
	String ipv6Address1;
	NdefMessage[] msgs;
	String l;
	LocationManager mlocManager;
	LocationListener mlocListener;
	
	private SharedPreferences prefs;
	DatabaseHandler db;

	PendingIntent mNfcPendingIntent;
	IntentFilter[] mWriteTagFilters;
	IntentFilter[] mNdefExchangeFilters;

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(NFCIoT6Activity.this);
		db = new DatabaseHandler(this);

		setContentView(R.layout.activity_nfciot6);
		mNote = ((TextView) findViewById(R.id.note));
		ipAddress = (EditText) findViewById(R.id.ip_address);
		ipv6Address1 = prefs.getString("ip","");
		ipv6Address = db.getIpv6Address();
		if(ipv6Address1.equals("4"))
		    ipv6Address = db.getIpv4Address();
		
		ipAddress.setText(ipv6Address);
		
		mNote.setBackgroundColor(android.graphics.Color.DKGRAY);
		
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		mlocListener = new MyLocationListener();

		mlocManager.requestLocationUpdates(
			LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		
		/*Intent iin = getIntent();
		Bundle b = iin.getExtras();
		try {
		    l = b.getString("location");
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}*/
		
		
		initSounds(getBaseContext());
		//addSound(1, R.raw.);

		// Handle all of our received NFC intents in this activity.
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// Intent filters for reading a note from a tag or exchanging over p2p.
		IntentFilter ndefDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		/*try {
			ndefDetected.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
		}*/
		mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

		// Intent filters for writing to a tag
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		mWriteTagFilters = new IntentFilter[] { tagDetected };
	}
	
	

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		mResumed = true;
		// Sticky notes received from Android
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
			NdefMessage[] messages = getNdefMessages(getIntent());
			byte[] payload = messages[0].getRecords()[0].getPayload();
			setNoteBody(new String(payload), 0);
			setIntent(new Intent()); // Consume this intent.
		}
		enableNdefExchangeMode();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		mResumed = false;
		mNfcAdapter.disableForegroundNdefPush(this);
	}

	String ndefMsg = "";
	String open = "";
	@SuppressLint("NewApi")
	@Override
	protected void onNewIntent(Intent intent) {
		System.gc();
		ipv6Address = ipAddress.getText().toString();
		// NDEF exchange mode
		if (!mWriteMode
				&& NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			msgs = getNdefMessages(intent);
			System.out.println("Usao ovde!!!!!!!!!!");
			ndefMsg = new String(msgs[0].getRecords()[0].getPayload());
			if(ipv6Address.equals("")){
			    inflater= LayoutInflater.from(NFCIoT6Activity.this);
			        view=inflater.inflate(R.layout.info_dialog, null);
			        textview=(TextView)view.findViewById(R.id.textmsg);
			        ipAddressDialog = (EditText) view.findViewById(R.id.ip_address_dialog);
			    textview.setText("Please enter IP address first in order to send the NFC reading!");
				AlertDialog.Builder builder = new AlertDialog.Builder(NFCIoT6Activity.this);
				builder.setTitle("Enter IP address!");
				builder.setView(view);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int which)
			            {
			        	prefs.edit().putString("ip", ipAddressDialog.getText().toString()).commit();
			        	if((ipAddressDialog.getText().toString()).contains("::"))
			        	    ipv6Address = "[" + ipAddressDialog.getText().toString() + "]";
			        	else
			        	    ipv6Address = ipAddressDialog.getText().toString();
			        	dialog.dismiss();
			        	ipAddress.setText(ipAddressDialog.getText().toString());
			        	promptForContent(msgs[0]);
			            }
			        });
				AlertDialog alert = builder.create();
				alert.show();
			}else{
			    //prefs.edit().putString("ip", ipv6Address).commit();
			    if(ipv6Address.contains("::"))
				ipv6Address = "[" + ipv6Address + "]";
			    promptForContent(msgs[0]);
			}
		}

		// Tag writing mode
		if (mWriteMode
				&& NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			writeTag(getNoteAsNdef(), detectedTag);
		}
	}



	@SuppressLint("NewApi")
	private void promptForContent(final NdefMessage msg) {
		String body = new String(msg.getRecords()[0].getPayload());
		SendNFCDataTask d = new SendNFCDataTask();
		d.execute(body);
	}

	String code = "";

	private class SendNFCDataTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
		    System.out.println(urls[0]);
		    code = urls[0];
		    String url = ipv6Address + ":5683/nfc";
		    if(!url.contains("coap"))
			url = "coap://" + url;
		    Request req = new POSTRequest();
	 		req.setURI(url);
	 		//req.setURI("coap://89.216.116.166:5684/rd/ep/ericsson.org.CoAPServer");
	 		//req.setPayload(urls[0].substring(3));
	 		//System.out.println(urls[0].substring(3));
	 		//req.setPayload("354909050056732");
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
			if (!result.equals(null) && !result.equals("") && !result.contains("Bad") && !result.contains("URL")){
			    CustomizeDialogInfo customizeDialog = new CustomizeDialogInfo(
					NFCIoT6Activity.this, result);
				customizeDialog.setTitle("Info about the device");
				customizeDialog.show();
			    /*if (!result.startsWith("http://") && !result.startsWith("https://"))
				result = "http://" + result;
			    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
			    startActivity(browserIntent);*/
				//setNoteBody("Message successfuly sent!", 1);
			}
			else
				setNoteBody("IMEI number is not recognized, please try again", 2);
			System.gc();
		}
	}


	private void setNoteBody(String body, int state) {
		mNote.setTextColor(android.graphics.Color.BLACK);
		mNote.setText(body);
		mNote.setTextColor(android.graphics.Color.WHITE);
		Timer timing = new Timer();
		if (state == 1) {
			hand1.postDelayed(run0, 0);
		}
		if (state == 2) {
			hand1.postDelayed(run, 0);
		}
	}
	Handler hand1 = new Handler();
	
	Runnable run0 = new Runnable() {
	    @Override
	    public void run() {
	    	mNote.setBackgroundResource(R.drawable.allow_pc);
	        hand1.postDelayed(run1, 4000);
	        
	    }
	};
	
	Runnable run = new Runnable() {
	    @Override
	    public void run() {
	    	mNote.setBackgroundResource(R.drawable.not_allow_pct);
	        hand1.postDelayed(run1, 4000);
	    }
	};
	
	Runnable run1 = new Runnable() {
	    @Override
	    public void run() {
	    	mNote.setBackgroundColor(android.graphics.Color.DKGRAY);
	    	mNote.setText("Waiting for a NFC tag...");
	    	mNote.setTextColor(android.graphics.Color.WHITE);
	        //hand1.postDelayed(run1, 3000);
	    	TelephonyManager telephonyManager =
			(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			String imei = telephonyManager.getDeviceId();
			String user = prefs.getString("user", "");
	    	CustomizeDialog customizeDialog = new CustomizeDialog(NFCIoT6Activity.this, imei, ipv6Address, user, l, code, 0);  
	            customizeDialog.setTitle("Code does not exist"); 
	            customizeDialog.setMessage("Would you like to regiter this code to the CMS?");
	            customizeDialog.show();
	    }
	};
	

	@SuppressLint("NewApi")
	private NdefMessage getNoteAsNdef() {
		byte[] textBytes = mNote.getText().toString().getBytes();
		NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(), new byte[] {}, textBytes);
		return new NdefMessage(new NdefRecord[] { textRecord });
	}

	@SuppressLint("NewApi")
	NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}
		} else {
			Log.d(TAG, "Unknown intent.");
			finish();
		}
		return msgs;
	}

	@SuppressLint("NewApi")
	private void enableNdefExchangeMode() {
		// pokrenuta app se shar-uje
		mNfcAdapter.enableForegroundNdefPush(NFCIoT6Activity.this,
				getNoteAsNdef());
		// ostaje u istoj app za citanje, nece da nudi opet izbor preko koje app
		// hoces da citas
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
				mNdefExchangeFilters, null);
	}

	@SuppressLint("NewApi")
	private void disableNdefExchangeMode() {
		mNfcAdapter.disableForegroundNdefPush(this);
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@SuppressLint("NewApi")
	private void enableTagWriteMode() {
		/*mWriteMode = true;
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
				mWriteTagFilters, null);*/
	}

	@SuppressLint("NewApi")
	private void disableTagWriteMode() {
		mWriteMode = false;
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@SuppressLint("NewApi")
	boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;

		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();

				if (!ndef.isWritable()) {
					toast("Tag is read-only.");
					return false;
				}
				if (ndef.getMaxSize() < size) {
					toast("Tag capacity is " + ndef.getMaxSize()
							+ " bytes, message is " + size + " bytes.");
					return false;
				}

				ndef.writeNdefMessage(message);
				toast("Wrote message to pre-formatted tag.");
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						toast("Formatted tag and wrote message");
						return true;
					} catch (IOException e) {
						toast("Failed to format tag.");
						return false;
					}
				} else {
					toast("Tag doesn't support NDEF.");
					return false;
				}
			}
		} catch (Exception e) {
			toast("Failed to write tag");
		}

		return false;
	}

	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	public void initSounds(Context theContext) {
	    mContext = theContext;
	    mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void addSound(int index, int SoundID)
	{
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	public void playSound(int index)
	{
	float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    mSoundPool.play((Integer) mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
	 
	public void playLoopedSound(int index)
	{
	    float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    mSoundPool.play((Integer) mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }
    
    public  void onDestroy()
    {
        super.onDestroy();
        if(mlocManager != null)
            mlocManager.removeUpdates(mlocListener);
    }

    public class MyLocationListener implements LocationListener {
	boolean first = true;

	@Override
	public void onLocationChanged(Location loc) {
	    loc.getLatitude();
	    loc.getLongitude();
	    l = loc.getLatitude() + ";" + loc .getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
	    buildAlertMessageNoGps();
	}

	@Override
	public void onProviderEnabled(String provider) {
	    Toast.makeText(getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){

	}
	
	private void buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				NFCIoT6Activity.this);
		alertDialogBuilder
				.setMessage("If you wish to turn on the GPS go to settings!")
				.setCancelable(false)
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

    }

}
