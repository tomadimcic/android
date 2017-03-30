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
import android.widget.TextView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.adapters.DesavanjaListAdapter;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.gui.DesavanjeActivity;
import com.nt.najboljekafane.gui.KafanaActivity;
import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Kafana;

public class KafanaDesavanjaFragment extends Fragment {

	public static final String EXTRA_KAFANA = "kafana";

	KafanaActivity activity;
	Kafana kafana;
	private ArrayList<Desavanje> desavanjaListSvihDesavanja;
	private ArrayList<Desavanje> desavanjaList;
	private DesavanjaListAdapter desavanjaListAdapter;
	ArrayList<Desavanje> arraylistDesavanja;
	private ListView desavanjaListView;
	private TextView emptyListDesavanja;
	String tip;

	DatabaseHandler db;

	public static KafanaDesavanjaFragment newInstance(Kafana kafana) {
		KafanaDesavanjaFragment fragment = new KafanaDesavanjaFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_KAFANA, kafana);
		fragment.setArguments(b);
		return fragment;
	}

	public KafanaDesavanjaFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.desavanja_list, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		kafana = (Kafana) getArguments().getSerializable(EXTRA_KAFANA);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (KafanaActivity) getActivity();

		desavanjaListView = (ListView) activity
				.findViewById(R.id.desavanja_list);
		emptyListDesavanja = (TextView) activity
				.findViewById(R.id.empty_list_desavanja);
		desavanjaListSvihDesavanja = new ArrayList<Desavanje>();
		desavanjaList = new ArrayList<Desavanje>();
		arraylistDesavanja = new ArrayList<Desavanje>();
		desavanjaListSvihDesavanja = filterDesavanjaByDatum(db.getAllDesavanja(tip));

		for (Desavanje desavanje : desavanjaListSvihDesavanja) {
			if (desavanje.getKafana().equals(kafana.getNaziv()))
				desavanjaList.add(desavanje);
		}

		arraylistDesavanja.addAll(desavanjaList);

		if (desavanjaList.size() == 0) {
			desavanjaListView.setVisibility(View.GONE);
			emptyListDesavanja.setVisibility(View.VISIBLE);
		} else {
			desavanjaListAdapter = new DesavanjaListAdapter(desavanjaList,
					activity, activity.getDbHandler());
			desavanjaListView.setAdapter(desavanjaListAdapter);
			desavanjaListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Desavanje selectedDesavanje = desavanjaList.get(position);
					Intent i = new Intent(activity, DesavanjeActivity.class);
					i.putExtra("desavanja", selectedDesavanje);
					startActivity(i);

				}
			});
		}
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
			if (differance < 86400000)
				filtered.add(desavanja);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	return filtered;
	}

	public void setDb(DatabaseHandler db) {
		this.db = db;

	}

	public void setTip(String tip) {
	    this.tip = tip;
	    
	}

}
