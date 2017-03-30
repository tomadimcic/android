package com.nt.najboljekafane.gui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.gui.KafanaActivity;
import com.nt.najboljekafane.model.Kafana;

public class KafanaInfoFragment extends Fragment {

	public static final String EXTRA_KAFANA = "kafana";

	Activity activity;

	Kafana kafana;

	TextView kafanaNaziv, kafanaAdresa, kafanaEmail, kafanaTelefon, kafanaRadnoVreme ;
	RatingBar ocenaAtmosfere, ocenaKafane;
	ImageButton callBtn;
	View view;
	TextView textview;
	LayoutInflater inflater;

	public static KafanaInfoFragment newInstance(Kafana kafana) {
		KafanaInfoFragment fragment = new KafanaInfoFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_KAFANA, kafana);
		fragment.setArguments(b);
		return fragment;
	}

	public KafanaInfoFragment() {

	}
	
	AlertDialog alert;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();

		kafana = ((Kafana) getArguments().getSerializable(EXTRA_KAFANA));
		kafanaNaziv = (TextView) activity.findViewById(R.id.topic);
		kafanaAdresa = (TextView) activity.findViewById(R.id.adresa);
		kafanaEmail = (TextView) activity.findViewById(R.id.email);
		kafanaTelefon = (TextView) activity.findViewById(R.id.telefon);
		ocenaKafane = (RatingBar) activity.findViewById(R.id.ocena_kafane);
		kafanaRadnoVreme = (TextView) activity.findViewById(R.id.radno_vreme);
		ocenaAtmosfere = (RatingBar) activity
				.findViewById(R.id.ocena_atmosfere);

		kafanaNaziv.setText(kafana.getNaziv());
		kafanaAdresa.setText(kafana.getGrad() + "," + kafana.getAdresa());
		kafanaEmail.setText(kafana.getEmail());
		kafanaTelefon.setText(kafana.getTelefon());
		ocenaKafane.setRating(kafana.getOcenaKafane());
		kafanaRadnoVreme.setText(kafana.getRadnoVreme());
		ocenaAtmosfere.setRating(kafana.getOcenaAtmosfere());

		callBtn = (ImageButton) activity.findViewById(R.id.callBtn);

		callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    if(kafana.getTelefon().equals("")){
				Toast.makeText(
					getActivity(),
					R.string.nema_informacija_broj_telefona,
					Toast.LENGTH_SHORT).show();
			    }else{
				if (isTelephonyEnabled()) {
					Intent i = new Intent(Intent.ACTION_CALL);
					i.setData(Uri.parse("tel:" + kafana.getTelefon()));
					startActivity(i);
				}

			    }
			}
		});

		ImageButton opis = (ImageButton) activity.findViewById(R.id.opis);
		
	        

		opis.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			    inflater= LayoutInflater.from(getActivity());
			        view=inflater.inflate(R.layout.info_dialog, null);
			        textview=(TextView)view.findViewById(R.id.textmsg);
			    textview.setText(kafana.getOpis().trim());
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle(kafana.getNaziv());
				builder.setView(view);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int which)
			            {
			        	dialog.dismiss();
			            }
			        });
				AlertDialog alert = builder.create();
				alert.show();

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_kafana, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private boolean isTelephonyEnabled() {
		TelephonyManager tm = (TelephonyManager) activity
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return tm != null
				&& tm.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

}
