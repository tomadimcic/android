package com.nt.najboljekafane.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.TextView;

import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.util.DataManager;

public class GetKafaneTask extends AsyncTask<Void, Void, ArrayList<Kafana>> {

	public static final String KAFANE_GET = "http://www.najboljekafane.rs/android/webapi/kafane_get.php";

	private ArrayList<Kafana> result;
	TextView tv;

	public GetKafaneTask(TextView tv) {
		this.tv = tv;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// progress = ProgressDialog.show(getApplicationContext(), "Sync",
		// "Preuzimanje kafana..");
	}

	@Override
	protected ArrayList<Kafana> doInBackground(Void... arg0) {

		try {
			result = new ArrayList<Kafana>();
			URL url = new URL(KAFANE_GET);
			URLConnection conn = url.openConnection();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			String response = "";
			while ((line = rd.readLine()) != null) {
				response += line;
			}
			if (response.equals("")) {
				System.out.println(response);
				JSONArray ja = new JSONArray(response);

				for (int i = 0; i < ja.length(); i++) {
					Kafana kafana = new Kafana();
					kafana.setId(Integer.parseInt(ja.getJSONObject(i)
							.getString("id")));
					kafana.setNaziv(ja.getJSONObject(i).getString("naziv"));
					kafana.setGrad(ja.getJSONObject(i).getString("grad"));
					kafana.setAdresa(ja.getJSONObject(i).getString("adresa"));
					kafana.setWebsajt(ja.getJSONObject(i).getString("websajt"));
					kafana.setEmail(ja.getJSONObject(i).getString("email"));
					kafana.setOpis(ja.getJSONObject(i).getString("opis"));
					kafana.setTelefon(ja.getJSONObject(i).getString(
							"broj_telefona"));
					kafana.setOcenaKafane(Float.valueOf(ja.getJSONObject(i)
							.getString("ocena_kafane")));
					kafana.setOcenaAtmosfere(Float.valueOf(ja.getJSONObject(i)
							.getString("ocena_atmosfere")));
					kafana.setSlika1(ja.getJSONObject(i).getString("slika1"));
					kafana.setSlika2(ja.getJSONObject(i).getString("slika2"));
					kafana.setSlika3(ja.getJSONObject(i).getString("slika3"));
					kafana.setSlika4(ja.getJSONObject(i).getString("slika4"));
					kafana.setSlika5(ja.getJSONObject(i).getString("slika5"));
					kafana.setVip(ja.getJSONObject(i).getString("vip"));
					kafana.setLat(ja.getJSONObject(i).getString("lat"));
					kafana.setLng(ja.getJSONObject(i).getString("lng"));
					kafana.setVerzijaBaze(ja.getJSONObject(i).getString(
							"verzija"));

					result.add(kafana);

				}

				DataManager.getInstance().setKafaneList(result);

				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Kafana>();
		}
		return new ArrayList<Kafana>();

	}

	@Override
	protected void onPostExecute(ArrayList<Kafana> result) {
		if (result != null)
			this.result = result;

	}

}
