package com.nt.najboljekafane.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.model.Pesma;
import com.nt.najboljekafane.model.Settings;
import com.nt.najboljekafane.util.NetworkUtil;

public class SplashScreenActivity extends Activity {

	protected int splashTime = 100;
	public static final String SYNC_URL = "http://www.najboljekafane.rs/android/webapi/sync.php";
	DatabaseHandler db;
	boolean prvoPokretanje;
	boolean isFinished;
	AlertDialog.Builder builder;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		db = new DatabaseHandler(this);
		ArrayList<Kafana> check = db.getAllKafana("kafana");
		if (check.size() > 0) {
			prvoPokretanje = false;
		} else {
			prvoPokretanje = true;

		}
		db.close();
		if (prvoPokretanje) {
			Settings settings = new Settings();
			settings.setDeviceUUID(telephonyManager.getDeviceId());
			db.addDeviceUUID(settings);
			this.builder = new AlertDialog.Builder(SplashScreenActivity.this);
			this.builder.setTitle("Prvo pokretanje");
			this.builder.setMessage(R.string.dialog_splash);
			this.builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							new DatabaseSyncTask().execute();
						}
					});
			this.builder.show();
		} else
			new DatabaseSyncTask().execute();

	}

	public void startKafane() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

	public class DatabaseSyncTask extends AsyncTask<Void, Void, Boolean> {

		protected void onPreExecute() {

		}

		protected Boolean doInBackground(Void... arg0) {

			/*
			 * String text = null; List<Desavanje> test =
			 * db.getAllDesavanja("kafana");
			 * 
			 * for (Desavanje kafana : test) { text += kafana.getTimestamp() +
			 * " " + kafana.getKafanaId() + "naziv desavanja: " +
			 * kafana.getNaslov();
			 * 
			 * } Log.d("=-=-=-=-=-=-=-BAZAAAAA", "" + text);
			 */
			String timeStampKafane = db.getKafanaLastTimestamp();

			String timeStampPesme = db.getPesmaLastTimestamp();
			String timestampDesavanja = db.getDesavanjeLastTimestamp();

			// db.close();

			String response = null;
			try {
				try {
					HttpClient client = NetworkUtil.getDefaultHttpClient();

					HttpPost postReq = new HttpPost();
					postReq.setURI(new URI(SYNC_URL));
					ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					postParameters.add(new BasicNameValuePair(
							"timestamp_kafane", timeStampKafane));
					postParameters.add(new BasicNameValuePair(
							"timestamp_pesme", timeStampPesme));
					postParameters.add(new BasicNameValuePair(
							"prvo_pokretanje", String.valueOf(prvoPokretanje)));
					postParameters.add(new BasicNameValuePair(
							"timestamp_desavanja", timestampDesavanja));
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
							postParameters);
					postReq.setEntity(formEntity);
					HttpResponse httpResponse = client.execute(postReq);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						response = NetworkUtil
								.convertStreamToString(httpResponse.getEntity()
										.getContent());
						Log.i("RESP", response);
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (response != null && response != "") {

					JSONObject myAwway = new JSONObject(response);
					JSONArray jsonArrKafane = myAwway.getJSONArray("kafane");

					for (int i = 0; i < jsonArrKafane.length(); i++) {
						Kafana kafana = Kafana.fromJson(jsonArrKafane
								.getJSONObject(i));

						if (kafana != null) {
							String kafanaDeleted = jsonArrKafane
									.getJSONObject(i)
									.getString("kafana_is_deleted").trim();
							String kafanaEdited = jsonArrKafane
									.getJSONObject(i)
									.getString("kafana_edited").trim();

							Kafana postojecaKafana = db
									.getKafanaByKafanaId(kafana.getKafanaId());
							// String postojeci_id =
							// postojecaKafana.getKafanaId();

							if (kafanaDeleted.equals("1")) {
								db.deleteKafanaByKafanaId(kafana.getKafanaId());
							} else if (postojecaKafana.getKafanaId() == null) {
								db.addKafana(kafana);
							} else if (kafanaEdited.equals("1")) {
								db.updateKafanaByKafanaId(kafana);
							}
						}
					}

					JSONArray jsonArrPesme = myAwway.getJSONArray("pesme");
					
					
					for (int i = 0; i < jsonArrPesme.length(); i++) {
						Pesma pesma = Pesma.fromJson(jsonArrPesme
								.getJSONObject(i));
						if (pesma != null) {
							Pesma postojecaPesma = db.getPesmaByPesmaId(pesma
									.getPesmaId());
							String postojeciId = postojecaPesma.getPesmaId();

							if (postojeciId != null) {
								db.updatePesmaByPesmaId(pesma);
								
							} else {
								db.addPesme(pesma);
							}
						}
					}

					JSONArray jsonArrDesavanja = myAwway
							.getJSONArray("desavanja");

					for (int i = 0; i < jsonArrDesavanja.length(); i++) {
						Desavanje desavanje = Desavanje
								.fromJson(jsonArrDesavanja.getJSONObject(i));
						if (desavanje != null) {

							String desavanjeDeleted = jsonArrDesavanja
									.getJSONObject(i).getString("is_deleted")
									.trim();
							Desavanje postojeDesavanje = db
									.getDesavanjeByDesavanjeId(desavanje
											.getDesavanjeId());
							String postojeciId = postojeDesavanje
									.getDesavanjeId();

							if (desavanjeDeleted.equals("1")) {
								db.deleteDesavanjeByDesavanjeId(desavanje
										.getDesavanjeId());
							} else if (postojeciId == null) {
								db.addDesavanje(desavanje);
							}

							else if (postojeciId != null) {
								db.updateDesavanjeByDesavanjeId(desavanje);
							}
						}

					}
				}
				db.close();

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// if(prvoPokretanje)
			// progressDialog.dismiss();
			startKafane();

		}
	}

}