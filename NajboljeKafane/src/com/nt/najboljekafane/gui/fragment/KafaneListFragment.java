package com.nt.najboljekafane.gui.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.adapters.DesavanjaListAdapter;
import com.nt.najboljekafane.adapters.KafaneListAdapter;
import com.nt.najboljekafane.adapters.PesmeListAdapter;
import com.nt.najboljekafane.gui.DesavanjeActivity;
import com.nt.najboljekafane.gui.KafanaActivity;
import com.nt.najboljekafane.gui.KafaneActivity;
import com.nt.najboljekafane.gui.PesmaActivity;
import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.model.Pesma;

public class KafaneListFragment extends Fragment implements OnItemClickListener {

	private static final String EXTRA_KAFANA = "kafana";
	private static final String EXTRA_SEARCH = "search_input";
	private static final String EXTRA_MODE = "mode";

	public static final int MODE_KAFANE = 0;
	public static final int MODE_DESAVANJA = 1;
	public static final int MODE_PESME = 2;

	public static final String TIP_KAFANE = "vrsta";

	String tip;
	int listMode;
	String search;
	ArrayList<Kafana> kafanaList;
	ArrayList<Desavanje> desavanjaList;
	ArrayList<Pesma> pesmaList;

	KafaneActivity activity;

	ListView list;
	KafaneListAdapter kafaneAdapter;
	PesmeListAdapter pesmeAdapter;
	DesavanjaListAdapter desavanjaAdapter;

	public static KafaneListFragment newInstance(String searchText, int mode,
			String tip) {
		KafaneListFragment frag = new KafaneListFragment();
		Bundle args = new Bundle();
		args.putString(EXTRA_SEARCH, searchText);
		args.putInt(EXTRA_MODE, mode);
		args.putString(TIP_KAFANE, tip);
		frag.setArguments(args);
		return frag;
	}

	public KafaneListFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_kafane_list, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listMode = getArguments().getInt(EXTRA_MODE);
		search = getArguments().getString(EXTRA_SEARCH);
		tip = getArguments().getString(TIP_KAFANE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (KafaneActivity) getActivity();

		list = (ListView) activity.findViewById(R.id.listview);
		setListMode(activity.getSupportActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (listMode) {
		case MODE_KAFANE:
			Kafana selectedKafana = kafanaList.get(position);
			Intent i = new Intent(getActivity(), KafanaActivity.class);
			i.putExtra(EXTRA_KAFANA, selectedKafana);
			i.putExtra(TIP_KAFANE, tip);
			startActivity(i);
		}

	}

	public void setListMode(int listMode) {
		this.listMode = listMode;
		switch (listMode) {
		case MODE_KAFANE:
			setKafaneListMode();
			break;
		case MODE_DESAVANJA:
			setDesavanjaListMode();
			break;
		case MODE_PESME:
			setPesmeListMode();
			break;
		}
	}

	private void setDesavanjaListMode() {
	    index = 0;
		if (desavanjaAdapter == null) {
			desavanjaList = new ArrayList<Desavanje>();
			desavanjaList = filterDesavanjaByDatum(activity.getDbHandler()
					.getAllDesavanja(tip), tip);
			desavanjaAdapter = new DesavanjaListAdapter(desavanjaList,
					activity, activity.getDbHandler());
			desavanjaAdapter.setDb(activity.getDbHandler());
		}
		list.setAdapter(desavanjaAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Desavanje selectedDesavanje = desavanjaList.get(position);
				String telefon = activity.getDbHandler().getKafanaByKafanaId(selectedDesavanje.getKafanaId()).getTelefon();
				Intent i = new Intent(activity, DesavanjeActivity.class);
				i.putExtra("desavanja", selectedDesavanje);
				i.putExtra("telefon", telefon);
				startActivity(i);

			}
		});
	}

	private void setKafaneListMode() {
	    index = 0;
		if (kafaneAdapter == null) {
			kafanaList = new ArrayList<Kafana>();
			kafanaList = activity.getDbHandler().getAllKafana(tip);
			//activity.getDbHandler().close();

			if (search != null) {
				ArrayList<Kafana> pomList = new ArrayList<Kafana>();
				for (Kafana kafana : kafanaList) {
					if (kafana.getGrad().equals(search))
						pomList.add(kafana);
				}
				kafanaList = pomList;
			}
			kafaneAdapter = new KafaneListAdapter(kafanaList, activity);
		}
		list.setAdapter(kafaneAdapter);
		//list.setSelectionFromTop(index, 0);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			    //index = position;
				Kafana selectedKafana = kafanaList.get(position);
				Intent i = new Intent(activity, KafanaActivity.class);
				i.putExtra(EXTRA_KAFANA, selectedKafana);
				i.putExtra(TIP_KAFANE, tip);
				startActivity(i);
			}
		});
	}
	
	int index = 0;

	private void setPesmeListMode() {
		// if (pesmeAdapter == null) {
		pesmaList = new ArrayList<Pesma>();
		pesmaList = activity.getDbHandler().getAllPesme();
		pesmeAdapter = new PesmeListAdapter(pesmaList, activity);
		// }
		list.setAdapter(pesmeAdapter);
		list.setSelectionFromTop(index, 0);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			    index = position;
				Pesma selectedPesma = pesmaList.get(position);
				Intent i = new Intent(activity, PesmaActivity.class);
				i.putExtra("pesma", selectedPesma);
				startActivity(i);

			}
		});
	}
	
	@Override
	public void onPause() {
	// TODO Auto-generated method stub
	    super.onPause();
	    if(listMode == 2 || listMode == 0)
		index = list.getFirstVisiblePosition();
	}

	public void filterResults(String text) {
		switch (listMode) {
		case MODE_KAFANE:
			kafaneAdapter.filterKafane(text);
			break;
		case MODE_DESAVANJA:
			desavanjaAdapter.filterDesavanja(text);
			break;
		case MODE_PESME:
			pesmeAdapter.filterPesme(text);
			break;
		}
	}

	public void filterResultsSubmenu(String text) {
		switch (listMode) {
		case MODE_KAFANE:
			kafaneAdapter.filterKafaneByGrad(text);
			break;
		case MODE_DESAVANJA:
			desavanjaAdapter.filterDesavanjaByKafane(text);
			break;
		case MODE_PESME:
			pesmeAdapter.filterPesmeByIzvodjac(text);
			break;
		}
	}

	public void filterFavorites(String text) {
		pesmeAdapter.filterFavorites(text);

	}

	private ArrayList<Desavanje> filterDesavanjaByDatum(
			ArrayList<Desavanje> svaDesavanjaList, String vrsta) {
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
				String vrstaKafane = activity.getDbHandler()
						.getKafanaByKafanaId(desavanja.getKafanaId())
						.getVrsta();
				if (differance < 86400000 && vrstaKafane.equals(vrsta))
					filtered.add(desavanja);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return filtered;
	}

}
