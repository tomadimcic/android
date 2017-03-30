package com.iotwear.wear.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.model.DMXControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.SendSingleControlData;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.NetworkUtil;

public class DMXControlActivity extends BaseActivity implements OnClickListener{

    Button b1, b2, b3, b4, b5, b6, b0, set;
    PiControl control;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View content = LayoutInflater.from(this).inflate(
				R.layout.dmx_control_layout_wheel, null);
		contentFrame.addView(content);
		
		control = (DMXControl) getIntent().getSerializableExtra(
			Constants.EXTRA_CONTROLLER);
		
		set = (Button) findViewById(R.id.btn_set);
		
		initWheel(R.id.passw_1);
		
		/*b1 = (Button) findViewById(R.id.btn1);
		b2 = (Button) findViewById(R.id.btn2);
		b3 = (Button) findViewById(R.id.btn3);
		b4 = (Button) findViewById(R.id.btn4);*/
		b5 = (Button) findViewById(R.id.btn5);
		b6 = (Button) findViewById(R.id.btn6);
		/*b0 = (Button) findViewById(R.id.btn0);
		
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);*/
		b5.setOnClickListener(this);
		b6.setOnClickListener(this);
		//b0.setOnClickListener(this);
		
		set.setOnClickListener(this);
    }
    
    @Override
    public ActionBarHandler createActionBarHandler() {
		return ActionBarHandlerFactory.createActionBarHandler(this, "Device",
				Mode.SLIDING);
	}

    @Override
    public void onClick(View v) {
	int pom = 0;
	switch (v.getId()) {
	case R.id.btn1:
	    pom = 1;
	    break;
	case R.id.btn2:
	    pom = 2;
	    break;
	case R.id.btn3:
	    pom = 3;
	    break;
	case R.id.btn4:
	    pom = 4;
	    break;
	case R.id.btn5:
	    pom = 101;
	    break;
	case R.id.btn6:
	    pom = 100;
	    break;
	case R.id.btn_set:
	    pom = currentWheel.getCurrentItem();
	    break;
	    
	}
	
	System.out.println("Usao " + pom);
	
	((DMXControl) control).setValue(pom);
	
	new SendSingleControlData(this, control).execute();
	
	//new SendToDMX().execute(pom);
	
    }
    
    private boolean wheelScrolled = false;
    WheelView currentWheel;
    
    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            currentWheel = wheel;
            
            //updateStatus(wheel.getCurrentItem(), wheel.getCurrentItem());
        }
    };
    
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                updateStatus(oldValue, newValue);
            }
        }
    };
    
    /**
     * Updates entered PIN status
     */
    private void updateStatus(int oldValue, int newValue) {
        TextView text = (TextView) findViewById(R.id.speed_txt);
        System.out.println("Ovdeeee");
        System.out.println(oldValue + " " + newValue);
    }
    
    private void initWheel(int id) {
        WheelView wheel = getWheel(id);
        currentWheel = wheel;
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 29));
        wheel.setCurrentItem(0);
        
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }
    
    private WheelView getWheel(int id) {
    	return (WheelView) findViewById(id);
    }
    
    public class SendToDMX extends AsyncTask<Integer, Void, Void> {


	@Override
	protected Void doInBackground(Integer... params) {
	    URL url;
		String response = "";
		int pom = params[0];
		try {
		    url = new URL("http://" + NetworkUtil.getDeviceUrl(DMXControlActivity.this, control.getHostDevice()) + "/dmx?id=" );
		
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(150);
			conn.setConnectTimeout(200);
			conn.setRequestMethod("GET");
			//conn.setRequestProperty("Connection", "close");
			//conn.getInputStream().close();
			conn.setDoInput(true);
			conn.setDoOutput(true);

		    BufferedReader rd = new BufferedReader(new InputStreamReader(
			    conn.getInputStream(), "UTF-8"));
		    String line = null;
		    
		    while ((line = rd.readLine()) != null) {
			response += line;
		    }
		    
		    //pom = Integer.parseInt(response);
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   // return 10;
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    //return 10;
		}
		return null;
	}
	
    }

}
