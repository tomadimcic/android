package com.nt.najboljekafane.gui.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.model.Glas;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.task.SetOcenaKafaneTask;

public class KafanaOceniFragment extends Fragment {

    public static final String EXTRA_KAFANA = "kafana";



    Kafana kafana;
    TextView kafanaNaziv;
    RatingBar oceniKafanu, oceniAtmosferu;
    ImageButton oceni;
    DatabaseHandler db;
    
    public static KafanaOceniFragment newInstance(Kafana kafana) {
	KafanaOceniFragment frag = new KafanaOceniFragment();
	Bundle args = new Bundle();
	args.putSerializable(EXTRA_KAFANA, kafana);
	frag.setArguments(args);
	return frag;
    }

    public KafanaOceniFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_oceni, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	kafana = (Kafana) getArguments().getSerializable(EXTRA_KAFANA);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	kafanaNaziv = (TextView) getActivity().findViewById(R.id.ocenaNaziv);
	kafanaNaziv.setText(kafana.getNaziv());
	oceniKafanu = (RatingBar) getActivity().findViewById(R.id.ocenaKafane);
	oceniAtmosferu = (RatingBar) getActivity().findViewById(
		R.id.ocenaAtmofere);
	oceni = (ImageButton) getActivity().findViewById(R.id.oceni);

	oceni.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		System.out.println(oceniKafanu.getRating());
		System.out.println(oceniAtmosferu.getRating());
		if (oceniKafanu.getRating() == 0
			|| oceniAtmosferu.getRating() == 0) {
		    Toast.makeText(getActivity(), //
			    "Morate dati bar jednu zvezdicu!", //
			    Toast.LENGTH_SHORT).show();
		} else {

		    String timestampNow = (String) android.text.format.DateFormat
			    .format("dd-MM-yy kk:mm:ss", new java.util.Date());

		    boolean isGlasao = false;

		    String glasLastTimestamp = db.getGlasLastTimestamp(
				   "kafana",  kafana.getKafanaId(), db.getDeviceUUID());
			    db.close();
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
				isUspesno = new SetOcenaKafaneTask().execute(
				    String.valueOf(kafana.getKafanaId()),
				    String.valueOf(oceniKafanu.getRating()),
				    String.valueOf(oceniAtmosferu.getRating()),
				    String.valueOf(uuid))
				    .get();
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
			    Toast.makeText(getActivity(),
				    R.string.uspesno_ocenjivanje,
				    Toast.LENGTH_SHORT).show();
			    
			    /*int brojGlasova = Integer.parseInt(kafana.getBrojGlasova());

			    float novaOcenaAtmosfere = (kafana.getOcenaAtmosfere()*brojGlasova + oceniAtmosferu.getRating())/(brojGlasova + 1);
			    float novaOcenaKafane = (kafana.getOcenaKafane()*brojGlasova + oceniKafanu.getRating())/(brojGlasova + 1);
			    
			    kafana.setOcenaAtmosfere(novaOcenaAtmosfere);
			    kafana.setOcenaKafane(novaOcenaKafane);
			    String noviBrojGlasova = String.valueOf(brojGlasova + 1);
			    kafana.setBrojGlasova(noviBrojGlasova);
			    
			    db.updateKafanaByKafanaId(kafana);*/
			    // int brGlasova = p.getBrojglasova();
			    // p.setBrojglasova(brGlasova++);
			    // db.updateBrojGlasovaByPesmaId(p);
			    Glas glas = new Glas();
			    glas.setTip("kafana");
			    glas.setTipId(kafana.getKafanaId());
			    glas.setTimestamp(timestampNow);
			    glas.setDeviceUUID(db.getDeviceUUID());
			    db.addGlas(glas);
			    
			    db.close();
			} else
			    neuspesnoGlasanje();
		    } else
			Toast.makeText(
				getActivity(),
				R.string.dupla_ocena,
				Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    public void neuspesnoGlasanje() {
	Toast.makeText(getActivity(),
		R.string.neuspelo_ocenjivanje,
		Toast.LENGTH_SHORT).show();
	db.close();
    }

    public void setDb(DatabaseHandler db) {
	this.db = db;

    }
}
