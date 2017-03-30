package com.nt.najboljekafane.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.bugsense.trace.BugSenseHandler;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Kafana;

public class MainActivity extends SherlockActivity implements
		ActionBar.TabListener, OnItemClickListener {

	public static final String BUGSENSE_API_KEY = "71fd9677";
	public static final String TIP_KAFANE = "vrsta";

	ImageButton kafane;
	ImageButton fotografije;
	ImageButton pivnice;
	Boolean first = true;
	EditText editsearch;
	ArrayList<Kafana> kafaneGet = new ArrayList<Kafana>();
	TextView tv;
	Button obavestenje;
	DatabaseHandler db = new DatabaseHandler(this);
	ImageButton rezervacije;
	ImageView fly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		BugSenseHandler
				.initAndStartSession(MainActivity.this, BUGSENSE_API_KEY);
		tv = (TextView) findViewById(R.id.inform);
		obavestenje = (Button) findViewById(R.id.obavestenje);
		fly = (ImageView) findViewById(R.id.fly);
		fly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fly.setBackgroundResource(R.drawable.muva2);
				final MediaPlayer mp1 = MediaPlayer.create(getBaseContext(),
						R.raw.recycle);
				mp1.start();
			}
		});

		kafane = (ImageButton) findViewById(R.id.kafane_btn);
		kafane.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, KafaneActivity.class);
				i.putExtra("tab", 0);
				i.putExtra(TIP_KAFANE, "kafana");
				startActivity(i);

			}
		});

		pivnice = (ImageButton) findViewById(R.id.restorani_btn);
		pivnice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, KafaneActivity.class);
				i.putExtra("tab", 0);
				i.putExtra(TIP_KAFANE, "restorani");
				startActivity(i);

			}
		});

		rezervacije = (ImageButton) findViewById(R.id.rezervacije_btn);
		rezervacije.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						RezervacijeActivity.class);
				i.putExtra("tab", 0);
				startActivity(i);

			}
		});

		fotografije = (ImageButton) findViewById(R.id.fotografije_btn);
		fotografije.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, GalerijaActivity.class);
				startActivity(i);
			}
		});

		obavestenje.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, KafaneActivity.class);
				i.putExtra("tab", 1);
				i.putExtra(TIP_KAFANE, "kafana");
				startActivity(i);
			}
		});

		ArrayList<Desavanje> desavanjaList = db.getAllDesavanja("kafana");
		ArrayList<Desavanje> filtered = filterDesavanjaByDatum(desavanjaList);
		String text = "";
		for (Desavanje desavanja : filtered) {
			text += desavanja.getDatum() + "-" + desavanja.getNaslov() + ": "
					+ desavanja.getKafana() + " " + desavanja.getGrad() + "; ";
		}
		tv.setText(text);
		if (text.equals(""))
		    tv.setText(R.string.obavestenje_nema_desavanja);
		
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.main, menu);

		ArrayList<Kafana> kafaneList = db.getAllKafana("kafana");
		ArrayList<String> filtered = filterKafaneByGrad(kafaneList);
		java.util.Collections.sort(filtered);

		SubMenu subMenu1 = menu.addSubMenu("Gradovi");
		for (String grad : filtered) {
			subMenu1.add(grad);
		}

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.selector_icon_list);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!item.getTitle().toString().equals("Gradovi")
				&& !item.getTitle().toString().equals("Search"))
			search(item.getTitle().toString());

		return true;

	}

	public void search(String query) {
		Intent i = new Intent(MainActivity.this, KafaneActivity.class);
		i.putExtra("search_input", query);
		i.putExtra(TIP_KAFANE, "kafana");
		startActivity(i);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		Intent i = new Intent(MainActivity.this, KafaneActivity.class);
		i.putExtra("search_input", tab.getText());
		startActivity(i);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		if (!first) {
			Intent i = new Intent(MainActivity.this, KafaneActivity.class);
			i.putExtra("search_input", tab.getText());
			startActivity(i);
		}
		first = false;

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	private ArrayList<Desavanje> filterDesavanjaByDatum(
			ArrayList<Desavanje> svaDesavanjaList) {
		ArrayList<Desavanje> filtered = new ArrayList<Desavanje>();
		long currentTime = (new Date()).getTime();
		for (Desavanje desavanja : svaDesavanjaList) {
			Date newDate = null;
			try {
				newDate = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)
						.parse(desavanja.getDatum());
				long newTime = newDate.getTime();
				long differance = currentTime - newTime;
				System.out.println(differance);
				if (differance < 86400000 && differance > 0)
					filtered.add(desavanja);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return filtered;
	}

	private ArrayList<String> filterKafaneByGrad(ArrayList<Kafana> sveKafane) {
		ArrayList<String> filtered = new ArrayList<String>();
		boolean isBila = false;
		for (Kafana kafana : sveKafane) {
			for (String kafanaGradovi : filtered) {
				if (kafanaGradovi.equals(kafana.getGrad()))
					isBila = true;
			}
			if (filtered.size() == 0 || !isBila)
				filtered.add(kafana.getGrad());
			isBila = false;
		}
		return filtered;
	}

}
