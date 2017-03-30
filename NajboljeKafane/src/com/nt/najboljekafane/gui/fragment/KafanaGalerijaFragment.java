package com.nt.najboljekafane.gui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.gui.adapter.LazyAdapter;
import com.nt.najboljekafane.model.Kafana;

public class KafanaGalerijaFragment extends Fragment {

	public static final String EXTRA_KAFANA = "kafana";

	Kafana kafana;
	ListView imageList;
	LazyAdapter imageAdapter;

	public static KafanaGalerijaFragment newInstance(Kafana kafana) {
		KafanaGalerijaFragment frag = new KafanaGalerijaFragment();
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_KAFANA, kafana);
		frag.setArguments(args);
		return frag;
	}

	public KafanaGalerijaFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_kafana_galerija, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		kafana = (Kafana) getArguments().getSerializable(EXTRA_KAFANA);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		imageList = (ListView) getActivity().findViewById(R.id.list);

		ArrayList<String> imageUrlList = kafana.getSlikeList();
		
		imageAdapter = new LazyAdapter(getActivity(), imageUrlList);
		imageList.setAdapter(imageAdapter);
	}
}
