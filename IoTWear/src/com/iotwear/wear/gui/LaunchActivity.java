package com.iotwear.wear.gui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.model.Animation;
import com.iotwear.wear.model.DimControl;
import com.iotwear.wear.model.LEDControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.SwitchControl;
import com.iotwear.wear.util.DataManager;

public class LaunchActivity extends BaseActivity {

	private static final String BUGSENSE_API_KEY = "7f35c154";
	public static final int SPLASH_DURATION_MILLIS = 2000;
	protected boolean firstRun;
	protected int currentVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		new SplashAsyncTask().execute();
	}

	@Override
	public ActionBarHandler createActionBarHandler() {
		return null;
	}

	public void addMockDevices() {
		/*for (int i = 0; i < 6; i++) { */
			PiDevice pi = new PiDevice("Dev1 ",
					"http://1", "192.168.2.1", 1, "S1JFK-5", "1234");
			LEDControl led = new LEDControl();
			led.setId("0");
			led.setName("LED1");
			pi.getControlList().add(led);
			for(int i = 0; i < 6; i++){
				SwitchControl sw = new SwitchControl();
				sw.setId(Integer.toString(i+1));
				sw.setName("Switch " + (i+1));
				pi.getControlList().add(sw);
			}
			
			for(int i = 0; i < 3; i++){
				DimControl sw = new DimControl();
				sw.setId(Integer.toString(i+8));
				sw.setName("Dim " + (i+1));
				pi.getControlList().add(sw);
			}
			
			DataManager.getInstance().addDevice(pi);
		/*}*/
	}

	public void addMockColors() {
		for (int i = 0; i < 10; i++) {
			switch (i) {
			case 0:
				DataManager.getInstance().saveColor(Color.RED);
				break;
			case 1:
				DataManager.getInstance().saveColor(Color.GREEN);
				break;
			case 2:
				DataManager.getInstance().saveColor(Color.BLUE);
				break;
			case 3:
				DataManager.getInstance().saveColor(Color.CYAN);
				break;
			case 4:
				DataManager.getInstance().saveColor(Color.MAGENTA);
				break;
			case 5:
				DataManager.getInstance().saveColor(Color.YELLOW);
				break;
			case 6:
				DataManager.getInstance().saveColor(Color.WHITE);
				break;
			case 7:
				DataManager.getInstance().saveColor(Color.LTGRAY);
				break;
			case 8:
				DataManager.getInstance().saveColor(Color.GRAY);
				break;
			case 9:
				DataManager.getInstance().saveColor(Color.DKGRAY);
				break;
			}
		}
	}

	public void addMockAnimations() {
		//ArrayList<Integer> colorList = DataManager.getInstance()
		//		.getColors();
		/*for (int i = 0; i < 5; i++) {
			Animation a = new Animation();
			a.setName("Animation " + i);
			ArrayList<Integer> colors = new ArrayList<Integer>();
			for (int j = 0; j < i + 2; j++) {
				colors.add(colorList.get(j));
			}
			a.setColorList(colors);
			a.setCrossfadeDuration(5);
			a.setLightOffDuration(1);
			a.setLightOnDuration(i + 2);

			DataManager.getInstance().addAnimation(a);
		}*/
		
		Animation a1 = new Animation();
		a1.setName("Stroboscope");
		ArrayList<Integer> colors1 = new ArrayList<Integer>();
		colors1.add(Color.WHITE);
		colors1.add(Color.BLACK);
		a1.setColorList(colors1);
		ArrayList<Double> on0 = new ArrayList<Double>();
		ArrayList<Double> cross0 = new ArrayList<Double>();
		for (Integer integer : colors1) {
		    on0.add(1.0);
		    cross0.add(1.0);
		}
		a1.setOnList(on0);
		a1.setCrossfadeList(cross0);
		
		Animation a2 = new Animation();
		a2.setName("Rainbow 1");
		ArrayList<Integer> colors2 = new ArrayList<Integer>();
		ArrayList<Double> on = new ArrayList<Double>();
		ArrayList<Double> cross = new ArrayList<Double>();
		
		colors2.add(Color.RED);
		colors2.add(Color.GREEN);
		colors2.add(Color.YELLOW);
		colors2.add(Color.BLUE);
		colors2.add(Color.WHITE);
		a2.setColorList(colors2);
		for (Integer integer : colors2) {
		    on.add(1.0);
		    cross.add(1.0);
		}
		a2.setOnList(on);
		a2.setCrossfadeList(cross);
		
		Animation a3 = new Animation();
		a3.setName("Rainbow 2");
		ArrayList<Integer> colors3 = new ArrayList<Integer>();
		ArrayList<Double> on1 = new ArrayList<Double>();
		ArrayList<Double> cross1 = new ArrayList<Double>();
		colors3.add(Color.GREEN);
		colors3.add(Color.YELLOW);
		colors3.add(Color.BLUE);
		colors3.add(Color.WHITE);
		colors3.add(Color.RED);
		a3.setColorList(colors3);
		for (Integer integer : colors3) {
		    on1.add(1.0);
		    cross1.add(0.0);
		}
		a3.setOnList(on1);
		a3.setCrossfadeList(cross1);
		
		Animation a4 = new Animation();
		a4.setName("Rainbow 3");
		ArrayList<Integer> colors4 = new ArrayList<Integer>();
		ArrayList<Double> on2 = new ArrayList<Double>();
		ArrayList<Double> cross2 = new ArrayList<Double>();
		colors4.add(Color.YELLOW);
		colors4.add(Color.BLUE);
		colors4.add(Color.WHITE);
		colors4.add(Color.RED);
		colors4.add(Color.GREEN);
		a4.setColorList(colors4);
		for (Integer integer : colors4) {
		    on2.add(1.0);
		    cross2.add(0.0);
		}
		a4.setOnList(on2);
		a4.setCrossfadeList(cross2);
		
		/*Animation a5 = new Animation();
		a5.setName("Rainbow 4");
		ArrayList<Integer> colors5 = new ArrayList<Integer>();
		colors5.add(Color.BLUE);
		colors5.add(Color.WHITE);
		colors5.add(Color.RED);
		colors5.add(Color.GREEN);
		colors5.add(Color.YELLOW);
		a5.setColorList(colors5);
		a5.setCrossfadeDuration(0);
		a5.setLightOffDuration(1);
		a5.setLightOnDuration(5);*/

		DataManager.getInstance().addAnimation(a1);
		DataManager.getInstance().addAnimation(a2);
		DataManager.getInstance().addAnimation(a3);
		DataManager.getInstance().addAnimation(a4);
		//DataManager.getInstance().addAnimation(a5);
		
	}

	private class SplashAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			long start = System.currentTimeMillis();
			if (DataManager.getInstance().isFirstRun()) {
				DataManager.getInstance().clearLists();
				//addMockDevices();
				addMockColors();
				addMockAnimations();
				DataManager.getInstance().setFirstRun(false);
			}

			/*ArrayList<PiDevice> deviceList = DataManager.getInstance()
					.getDeviceList();
			for (PiDevice pi : deviceList) {
				try {

					HttpClient client = new DefaultHttpClient();
					HttpGet req = new HttpGet("http://"
							+ NetworkUtil.getDeviceUrl(LaunchActivity.this, pi)
							+ PiDevice.URL_STATUS);
					HttpResponse response = client.execute(req);

					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						String resp = NetworkUtil.inputStreamToString(
								response.getEntity().getContent()).toString();
						int res = Integer.valueOf(resp);
						pi.setTurnedOn((res == 1) ? true : false);
					} else {
						pi.setTurnedOn(false);
					}

				} catch (Exception e) {
					e.printStackTrace();
					pi.setTurnedOn(false);
				}
			}*/

			// TODO Save device statuses to MemoryCache so it can be retrieved
			// and manipulated without DataManager which should only handle
			// device preferences
			long deltaT = System.currentTimeMillis() - start;
			if (deltaT < SPLASH_DURATION_MILLIS) {
				try {
					Thread.sleep(SPLASH_DURATION_MILLIS - deltaT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent i = new Intent(LaunchActivity.this, LoginActivity.class);
			startActivity(i);
			finish();
		}
	}
}
