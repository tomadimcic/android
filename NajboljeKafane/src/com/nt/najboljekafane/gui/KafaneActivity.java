package com.nt.najboljekafane.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.gui.fragment.KafaneListFragment;
import com.nt.najboljekafane.gui.fragment.OkoMeneMapFragment;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.model.Pesma;


public class KafaneActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener {

	public static final String TIP_KAFANE = "vrsta";

	KafaneListFragment listFragment;
	OkoMeneMapFragment okoMeneFragment;
	EditText editsearch;
	ArrayList<Pesma> svePesme;
	boolean isPrviPut = true;
	String hint = "Pretraga";

	int tabSelected;
	String tip;

	DatabaseHandler db = new DatabaseHandler(this);

	public DatabaseHandler getDbHandler() {
		return db;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_kafana);
		// text = (TextView)findViewById(R.id.text);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		getSupportActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));

		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		int t = getIntent().getIntExtra("tab", 0);
		tip = b.getString(TIP_KAFANE);

		String[] tabTitleArray = null;

		if (tip.equals("kafana"))
			tabTitleArray = getResources().getStringArray(
					R.array.array_tabovi_kafane);
		else
			tabTitleArray = getResources().getStringArray(
					R.array.array_tabovi_restorani);

		for (int i = 0; i < tabTitleArray.length; i++) {
			ActionBar.Tab tab = getSupportActionBar().newTab();
			tab.setText(tabTitleArray[i]);
			tab.setTabListener(this);
			getSupportActionBar().addTab(tab);
		}

		getSupportActionBar().selectTab(getSupportActionBar().getTabAt(t));

		// System.out.println("+++++++++++++++++++++++");
		// System.out.println(t);

		// getSupportActionBar().setSelectedNavigationItem(t);
		// getSupportActionBar().getSelectedTab();

		svePesme = db.getAllPesme();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.kafane_menu, menu);

		MenuItem favorites = menu.findItem(R.id.favorites);
		MenuItem actionCity = menu.findItem(R.id.action_city);

		// Locate the EditText in menu.xml
		editsearch = (EditText) menu.findItem(R.id.menu_search).getActionView();

		// Capture Text in EditText
		editsearch.addTextChangedListener(textWatcher);

		// Show the search menu item in menu.xml
		MenuItem menuSearch = menu.findItem(R.id.menu_search);

		switch (tabSelected) {
		case 0:
			hint = "Pretrazite kafane";
			menuSearch.setVisible(true);
			actionCity.setVisible(false);
			favorites.setVisible(false);
			ArrayList<String> filtered = filterKafaneByGrad(db
					.getAllKafana(tip));
			java.util.Collections.sort(filtered);
			
			List<Kafana> reports = db.getAllKafana("kafana");
    		for (Kafana cn : reports) {
	            String log = "naziv: "+cn.getNaziv()+" ,grad: " + cn.getGrad();
	            Log.d("DB entries: ", log);
    		}
	            
	            
	            
			SubMenu subMenu1 = menu.addSubMenu("Gradovi");
			subMenu1.add("Svi gradovi");
			for (String grad : filtered) {
				subMenu1.add(grad);
			}

			MenuItem subMenu1Item = subMenu1.getItem();
			subMenu1Item.setIcon(R.drawable.selector_icon_list);
			subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
					| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			break;
		case 1:
			hint = "Pretrazite desavanja";
			menuSearch.setVisible(true);
			actionCity.setVisible(false);
			favorites.setVisible(false);
			ArrayList<String> sviNazivi = new ArrayList<String>();
			ArrayList<Kafana> sveKafane = db.getAllKafana(tip);
			boolean isBila = false;
			for (Kafana kafana : sveKafane) {
				for (String kafanaNazivi : sviNazivi) {
					if (kafanaNazivi.equals(kafana.getNaziv()))
						isBila = true;
				}
				if (sviNazivi.size() == 0 || !isBila)
					sviNazivi.add(kafana.getNaziv());
				isBila = false;
			}

			java.util.Collections.sort(sviNazivi);

			SubMenu subMenu2 = menu.addSubMenu("Kafane");
			subMenu2.add("Sve kafane");

			for (String kafana : sviNazivi) {
				subMenu2.add(kafana);
			}

			MenuItem subMenu2Item = subMenu2.getItem();
			subMenu2Item.setIcon(R.drawable.selector_icon_list);
			subMenu2Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
					| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			break;
		case 2:
			hint = "Pretrazite pesme";
			menuSearch.setVisible(true);
			actionCity.setVisible(false);
			favorites.setVisible(true);
			favorites.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {

					if (isPrviPut) {
						filterFavorites("favorites");
						isPrviPut = false;
					} else {
						filterFavorites("");
						isPrviPut = true;
					}
					return true;
				}
			});
			ArrayList<String> sviIzvodjaci = new ArrayList<String>();
			boolean isBilaPesma = false;
			for (Pesma pesma : svePesme) {
				for (String izvodjaci : sviIzvodjaci) {
					if (izvodjaci.equals(pesma.getIzvodjac()))
						isBilaPesma = true;
				}
				if (sviIzvodjaci.size() == 0 || !isBilaPesma)
					sviIzvodjaci.add(pesma.getIzvodjac());
				isBilaPesma = false;
			}
			java.util.Collections.sort(sviIzvodjaci);
			SubMenu subMenu3 = menu.addSubMenu("Izvodjaci");
			subMenu3.add("Svi izvodjaci");

			for (String pesma : sviIzvodjaci) {
				subMenu3.add(pesma);
			}

			MenuItem subMenu3Item = subMenu3.getItem();
			subMenu3Item.setIcon(R.drawable.selector_icon_list);
			subMenu3Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
					| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			break;
		case 3:
			menuSearch.setVisible(false);
			actionCity.setVisible(false);
			favorites.setVisible(false);
			break;
		default:
			break;
		}

		menuSearch.setOnActionExpandListener(new OnActionExpandListener() {

			// Menu Action Collapse
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Empty EditText to remove text filtering

				editsearch.setText("");
				editsearch.clearFocus();
				return true;
			}

			// Menu Action Expand
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Focus on EditText
				((EditText) item.getActionView()).setHint(hint);
				editsearch.requestFocus();

				// Force the keyboard to show on EditText focus
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});

		editsearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_DONE) {
					search(arg0.getText().toString());
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!item.getTitle().toString().equals("Gradovi")
				&& !item.getTitle().toString().equals("Kafane")
				&& !item.getTitle().toString().equals("Izvodjaci")
				&& !item.getTitle().toString().equals("Search"))
			search(item.getTitle().toString());
		return false;

	}

	public void search(String query) {
		if (query.equals("Svi gradovi") || query.equals("Sve kafane")
				|| query.equals("Svi izvodjaci"))
			query = "";
		listFragment.filterResultsSubmenu(query);
	}

	public void filterFavorites(String text) {
		listFragment.filterFavorites(text);
	}

	// EditText TextWatcher
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String text = editsearch.getText().toString()
					.toLowerCase(Locale.getDefault());
			listFragment.filterResults(text);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	int t;
	boolean first = true;

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		boolean isLastTabList = tabSelected < 3;
		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		tip = b.getString(TIP_KAFANE);
		tabSelected = tab.getPosition();
		t = b.getInt("tab");
		if (t != 0 && first) {
			tabSelected = t;
			first = false;
		}else{

		supportInvalidateOptionsMenu();
		switch (tabSelected) {
		case 3:
			if (okoMeneFragment == null) {
				okoMeneFragment = new OkoMeneMapFragment();
			}

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_placeholder, okoMeneFragment)
					.commit();
			okoMeneFragment.setTip(tip);
			break;
		default:
			if (listFragment == null) {
				listFragment = KafaneListFragment.newInstance(getIntent()
						.getStringExtra("search_input"), tabSelected, tip);
				getSupportFragmentManager().beginTransaction()
						.add(R.id.fragment_placeholder, listFragment).commit();
			} else {
				if (!isLastTabList) {
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.fragment_placeholder, listFragment)
							.commit();
				}
			}
			if (listFragment.isAdded()) {
				listFragment.setListMode(tabSelected);
			}
			break;

		}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(tabSelected == 0  && !first)
		    listFragment.setListMode(tabSelected);
		//if(tabSelected == 2)
		  //  listFragment.setListMode(tabSelected);
		first = false;
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

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
	
	private boolean isGpsEnabled() {
		final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return false;
		return true;
	}

	private void showGpsDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getString(R.string.gps_upozorenje))
				.setCancelable(false)
				.setPositiveButton("Da",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {
								getSupportActionBar().setSelectedNavigationItem(tabSelected);
								Tab tabBefore = getSupportActionBar().getTabAt(tabSelected);
								getSupportActionBar().selectTab(tabBefore);
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
						getSupportActionBar().setSelectedNavigationItem(tabSelected);
						Tab tabBefore = getSupportActionBar().getTabAt(tabSelected);
						getSupportActionBar().selectTab(tabBefore);
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}
