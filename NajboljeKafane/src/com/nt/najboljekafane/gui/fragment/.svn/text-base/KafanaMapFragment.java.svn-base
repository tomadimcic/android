package com.nt.najboljekafane.gui.fragment;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.view.KafanaMapInfoAdapter;

public class KafanaMapFragment extends SupportMapFragment {

	private static final String EXTRA_KAFANA = "kafana";
	Kafana kafana;

	public static KafanaMapFragment newInstance(Kafana kafana) {
		KafanaMapFragment frag = new KafanaMapFragment();
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_KAFANA, kafana);
		frag.setArguments(args);
		return frag;
	}

	public KafanaMapFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		kafana = (Kafana) getArguments().getSerializable(EXTRA_KAFANA);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		double lat = 44.869901;
		double lng = 20.462036;
		try {
			lat = Double.parseDouble(kafana.getLat());
			lng = Double.parseDouble(kafana.getLng());
		} catch (Exception e) {
			// TODO: handle exception
		}
		final LatLng kafanaLatLng = new LatLng(lat, lng);

		final GoogleMap gmap = getMap();
		gmap.setInfoWindowAdapter(new KafanaMapInfoAdapter(getActivity()
				.getLayoutInflater()));
		gmap.addMarker(new MarkerOptions().position(kafanaLatLng)
				.title(kafana.getNaziv())
				.snippet(kafana.getOpis() + "#" + kafana.getRadnoVreme())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));

		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(kafanaLatLng, 10));
		gmap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
		gmap.setMyLocationEnabled(true);
		gmap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				final LocationManager manager = (LocationManager) getActivity()
						.getSystemService(Context.LOCATION_SERVICE);

				if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					isGPS = buildAlertMessageNoGps();
				} else
					isGPS = true;
				if (isGPS) {
					if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						LatLng fromPosition = new LatLng(gmap.getMyLocation()
								.getLatitude(), gmap.getMyLocation()
								.getLongitude());
						GMapV2Direction md = new GMapV2Direction();
						Document doc = md.getDocument(fromPosition,
								kafanaLatLng, GMapV2Direction.MODE_DRIVING);

						if(doc != null){
        						ArrayList<LatLng> directionPoint = md.getDirection(doc);
        						PolylineOptions rectLine = new PolylineOptions().width(
        								3).color(Color.RED);
        
        						for (int i = 0; i < directionPoint.size(); i++) {
        							rectLine.add(directionPoint.get(i));
        						}
        
        						gmap.addPolyline(rectLine);
						} else
						    Toast.makeText(getActivity(),
								R.string.problemi_sa_internet_konekcijom,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(getActivity(),
								R.string.gps_nije_ukljucen,
								Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(getActivity(), R.string.ukljucite_gps,
							Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	}

	boolean isGPS = false;
	boolean isEnabled = false;

	private boolean buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder
				.setMessage(R.string.gps_upozorenje)
				.setCancelable(false)
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								isEnabled = true;
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Ne",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
		return isEnabled;
	}
}
