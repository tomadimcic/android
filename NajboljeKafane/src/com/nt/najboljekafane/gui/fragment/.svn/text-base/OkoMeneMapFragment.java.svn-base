package com.nt.najboljekafane.gui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.bugsense.trace.BugSenseHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.gui.KafanaActivity;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.view.KafanaMapInfoAdapter;

public class OkoMeneMapFragment extends SupportMapFragment implements
		OnMyLocationChangeListener, OnMarkerClickListener {

	HashMap<String, Kafana> idKafanaMap;
	ProgressDialog loadingDialog;
	boolean isLocationSet;
	long refreshTime = 0;
	String tip;

	public OkoMeneMapFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		idKafanaMap = new HashMap<String, Kafana>();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity().getBaseContext());

		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
					getActivity(), requestCode);
			dialog.show();

		} else {
			getMap().setMyLocationEnabled(true);
			getMap().setOnMyLocationChangeListener(this);
			getMap().setInfoWindowAdapter(
					new KafanaMapInfoAdapter(getActivity().getLayoutInflater()));
			loadingDialog = new ProgressDialog(getActivity());
			loadingDialog.show();
		}
	}

	@Override
	public void onMyLocationChange(Location location) {
		if (loadingDialog.isShowing())
			loadingDialog.dismiss();

		if (refreshTime == 0
				|| (refreshTime - System.currentTimeMillis()) > 20000) {

			refreshTime = System.currentTimeMillis();
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();

			LatLng latLng = new LatLng(latitude, longitude);

			getMap().moveCamera(CameraUpdateFactory.newLatLng(latLng));

			getMap().animateCamera(CameraUpdateFactory.zoomTo(15));
			showKafaneOkoMene(location);
		}

	}

	private void showKafaneOkoMene(Location curLocation) {
		DatabaseHandler db = new DatabaseHandler(getActivity());
		ArrayList<Kafana> kafanaList = new ArrayList<Kafana>();
		kafanaList = db.getAllKafana(tip);
		db.close();
		for (Kafana k : kafanaList) {
			try {
				double lat = Double.parseDouble(k.getLat());
				double lng = Double.parseDouble(k.getLng());
				Location kLoc = new Location(curLocation);
				kLoc.setLatitude(lat);
				kLoc.setLongitude(lng);
				if (kLoc.distanceTo(curLocation) < 30000) {
					Marker m = getMap().addMarker(
							new MarkerOptions()
									.position(new LatLng(lat, lng))
									.title(k.getNaziv())
									.snippet(
											k.getAdresa() + "#"
													+ k.getRadnoVreme())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.pointer)));
					idKafanaMap.put(m.getId(), k);

				}

			} catch (NumberFormatException e) {
				BugSenseHandler.sendExceptionMessage("OkoMeneMapFragment",
						k.getKafanaId() + " " + k.getNaziv(), e);
			}

		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Kafana selectedKafana = idKafanaMap.get(marker.getId());
		if (selectedKafana != null) {
			Intent i = new Intent(getActivity(), KafanaActivity.class);
			i.putExtra(KafanaActivity.EXTRA_KAFANA, selectedKafana);
			startActivity(i);
		}
		return false;
	}

	public void setTip(String tip) {
		this.tip = tip;

	}

}
