package com.nt.najboljekafane.gui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.gui.fragment.KafanaDesavanjaFragment;
import com.nt.najboljekafane.gui.fragment.KafanaGalerijaFragment;
import com.nt.najboljekafane.gui.fragment.KafanaInfoFragment;
import com.nt.najboljekafane.gui.fragment.KafanaMapFragment;
import com.nt.najboljekafane.gui.fragment.KafanaOceniFragment;
import com.nt.najboljekafane.model.Kafana;

public class KafanaActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener {

	public static final String EXTRA_KAFANA = "kafana";
	public static final String TIP_KAFANE = "vrsta";
	
	private static final String[] TAB_TITLES = { "Info", "Galerija", "Oceni",
			"Lokacija", "Dešavanja" };
	private static final int POSITION_INFO = 0;
	private static final int POSITION_GALERIJA = 1;
	private static final int POSITION_OCENI = 2;
	private static final int POSITION_LOKACIJA = 3;
	private static final int POSITION_DESAVANJA = 4;

	Kafana kafana;
	String tip;

	KafanaInfoFragment kafanaInfoFragment;
	KafanaOceniFragment kafanaOceniFragment;
	KafanaMapFragment kafanaMapFragment;
	KafanaGalerijaFragment kafanaGalerijaFragment;
	KafanaDesavanjaFragment kafanaDesavanjaFragment;

	DatabaseHandler db = new DatabaseHandler(this);

	public DatabaseHandler getDbHandler() {
		return db;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kafana);

		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		tip = b.getString(TIP_KAFANE);
		kafana = (Kafana) b.getSerializable(EXTRA_KAFANA);

		if (kafanaInfoFragment == null)
			kafanaInfoFragment = KafanaInfoFragment.newInstance(kafana);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_placeholder, kafanaInfoFragment).commit();
		try {
			MapsInitializer.initialize(getApplicationContext());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setActionBar();

	}

	private void setActionBar() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		getSupportActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		
		String[] tabTitleArray = getResources().getStringArray(
					R.array.array_tabovi_kafana);

		for (int i = 0; i < tabTitleArray.length; i++) {
			ActionBar.Tab tab = getSupportActionBar().newTab();
			tab.setText(tabTitleArray[i]);
			tab.setTabListener(this);
			getSupportActionBar().addTab(tab);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.kafana_menu, menu);

		MenuItem dialer = menu.findItem(R.id.dialer);
		dialer.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
			    if(kafana.getTelefon().equals("")){
				Toast.makeText(
					KafanaActivity.this,
					R.string.nema_informacija_broj_telefona,
					Toast.LENGTH_SHORT).show();
			    }else{
				Intent i = new Intent(Intent.ACTION_CALL);
				i.setData(Uri.parse("tel:" + kafana.getTelefon()));

				startActivity(i);
			    }
				return false;
			}
		});

		return true;
	}

	public void search(String query) {
		Intent i = new Intent(KafanaActivity.this, KafaneActivity.class);
		i.putExtra("search_input", query);
		startActivity(i);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch (tab.getPosition()) {
		case POSITION_INFO:
			if (kafanaInfoFragment == null)
				kafanaInfoFragment = KafanaInfoFragment.newInstance(kafana);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_placeholder, kafanaInfoFragment)
					.commit();
			break;

		case POSITION_GALERIJA:
			if (kafanaGalerijaFragment == null)
				kafanaGalerijaFragment = KafanaGalerijaFragment
						.newInstance(kafana);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_placeholder, kafanaGalerijaFragment)
					.commit();
			break;

		case POSITION_OCENI:
			if (kafanaOceniFragment == null) {
				kafanaOceniFragment = KafanaOceniFragment.newInstance(kafana);
				kafanaOceniFragment.setDb(db);
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_placeholder, kafanaOceniFragment)
					.commit();
			break;

		case POSITION_LOKACIJA:
			if (kafanaMapFragment == null)
				kafanaMapFragment = KafanaMapFragment.newInstance(kafana);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_placeholder, kafanaMapFragment)
					.commit();
			break;
		case POSITION_DESAVANJA:
			if (kafanaDesavanjaFragment == null) {
				kafanaDesavanjaFragment = KafanaDesavanjaFragment
						.newInstance(kafana);
				kafanaDesavanjaFragment.setDb(db);
				kafanaDesavanjaFragment.setTip(tip);
			}
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_placeholder, kafanaDesavanjaFragment)
					.commit();
			break;
		}

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
