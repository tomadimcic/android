package com.nt.najboljekafane.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.model.Glas;
import com.nt.najboljekafane.model.Pesma;
import com.nt.najboljekafane.task.SetOcenaPesmeTask;

public class PesmaActivity extends SherlockActivity implements
		ActionBar.TabListener, OnItemClickListener {

	TextView izvodjacPesma;
	TextView brojGlasova;
	TextView textPesme;
	TextView youtube;
	
	EditText editsearch;
	Pesma p;
	ImageView slika1;
	ImageView slika2;
	ImageView slika3;
	ImageView slika4;
	ImageView slika5;
	ImageButton glasaj;
	ListView listV;

	RatingBar oceniAtmosferu;
	RatingBar oceniKafanu;
	ImageButton oceni;

	MenuItem favorites;
	MenuItem favoritesRemove;

	DatabaseHandler db = new DatabaseHandler(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pesma);
		// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		getSupportActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));

		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		p = (Pesma) b.getSerializable("pesma");

		izvodjacPesma = (TextView) findViewById(R.id.izvodjacPesma);
		brojGlasova = (TextView) findViewById(R.id.brojGlasova);
		textPesme = (TextView) findViewById(R.id.textPesme);
		youtube = (TextView) findViewById(R.id.youtube);

		izvodjacPesma.setText(p.getIzvodjac() + " - " + p.getNaziv());
		brojGlasova.setText(String.valueOf(p.getBrojglasova()));
		textPesme.setText(p.getTekst());
		
		Pesma yt = db.getPesmaByPesmaId(p.getPesmaId());
		
		youtube.setText(yt.getYoutube());
		Linkify.addLinks(youtube, Linkify.ALL);
		//Pesma ba = db.getPesmaByPesmaId("1");
		// Log.d("text pesme", p.getTekst());
		glasaj = (ImageButton) findViewById(R.id.glasaj);

		glasaj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String timestampNow = (String) android.text.format.DateFormat
						.format("dd-MM-yy kk:mm:ss", new java.util.Date());

				boolean isGlasao = false;

				String glasLastTimestamp = db.getGlasLastTimestamp("pesma",
						p.getPesmaId(), "");

				if (!glasLastTimestamp.equals("")) {
					// System.out.println(glasLastTimestamp);
					try {
						long currentTime = System.currentTimeMillis() / 1000;
						Date newDate = new SimpleDateFormat(
								"dd-MM-yy kk:mm:ss", Locale.ENGLISH)
								.parse(glasLastTimestamp);
						long newTime = newDate.getTime() / 1000;
						long differance = currentTime - newTime;
						System.out.println(differance);
						if (differance < 86400)
							isGlasao = true;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (!isGlasao) {
					boolean isUspesno = false;
					try {
						String uuid = db.getDeviceUUID();
						isUspesno = new SetOcenaPesmeTask().execute(
								String.valueOf(p.getPesmaId()),
								String.valueOf(uuid)).get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						neuspesnoGlasanje();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						neuspesnoGlasanje();
					}

					if (isUspesno) {

						Toast.makeText(PesmaActivity.this,
								R.string.uspesno_glasanje,
								Toast.LENGTH_SHORT).show();

						int brGlasova = p.getBrojglasova() + 1;
						p.setBrojglasova(brGlasova);
						db.updateBrojGlasovaByPesmaId(p);

						Glas glas = new Glas();
						glas.setTip("pesma");
						glas.setTipId(p.getPesmaId());
						glas.setTimestamp(timestampNow);
						db.addGlas(glas);
					} else
						neuspesnoGlasanje();
				} else
					Toast.makeText(
							PesmaActivity.this,
							R.string.dupli_glas,
							Toast.LENGTH_SHORT).show();

			}
		});

	}

	public void neuspesnoGlasanje() {
		Toast.makeText(PesmaActivity.this,
				R.string.neuspelo_glasanje,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.pesma_menu, menu);

		favorites = menu.findItem(R.id.favorites);
		favoritesRemove = menu.findItem(R.id.favoritesremove);

		if (p.getOmiljena() != null) {
			if (p.getOmiljena().equals("da")) {
				favorites.setVisible(false);
				favoritesRemove.setVisible(true);
			} else {
				favorites.setVisible(true);
				favoritesRemove.setVisible(false);
			}
		} else {
			favorites.setVisible(true);
			favoritesRemove.setVisible(false);
		}
		favorites.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				favorites.setVisible(false);
				p.setOmiljena("da");
				db.setOmiljenPesmaByPesmaId(p);
				Toast.makeText(
						PesmaActivity.this,
						"Pesma: \"" + p.getNaziv()
								+ "\" je ubacena u kategoriju omiljene pesme!",
						Toast.LENGTH_SHORT).show();
				db.close();
				return false;
			}
		});

		favoritesRemove
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						favoritesRemove.setVisible(false);
						p.setOmiljena("ne");
						db.setOmiljenPesmaByPesmaId(p);
						Toast.makeText(
								PesmaActivity.this,
								"Pesma: \""
										+ p.getNaziv()
										+ "\" je izbacena iz kategorije omiljene pesme!",
								Toast.LENGTH_SHORT).show();
						db.close();
						return false;
					}
				});
		return true;
	}

	public void search(String query) {
		System.out.println("+++++++++++++++++++++++++++++" + query);
		Intent i = new Intent(PesmaActivity.this, KafaneActivity.class);
		i.putExtra("search_input", query);
		startActivity(i);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
