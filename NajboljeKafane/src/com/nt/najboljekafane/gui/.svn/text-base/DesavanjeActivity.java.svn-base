package com.nt.najboljekafane.gui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.model.Desavanje;

public class DesavanjeActivity extends SherlockActivity {

	TextView naziv;
	TextView grad;
	TextView vreme;
	TextView opis;
	TextView kafana;
	EditText editsearch;
	Desavanje desavanje;
	String kafanaTelefon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.desavanje_layout);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));

		naziv = (TextView) findViewById(R.id.desavanje_naziv);
		grad = (TextView) findViewById(R.id.desavanja_grad1);
		vreme = (TextView) findViewById(R.id.desavanja_vreme1);
		opis = (TextView) findViewById(R.id.desavanja_opis);
		kafana = (TextView) findViewById(R.id.desavanja_kafana);

		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		desavanje = (Desavanje) b.getSerializable("desavanja");
		kafanaTelefon = b.getString("telefon");

		naziv.setText(desavanje.getNaslov());
		grad.setText(desavanje.getGrad());
		vreme.setText(desavanje.getDatum());
		opis.setText(desavanje.getOpis());
		kafana.setText(desavanje.getKafana());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.desavanje_menu, menu);

		MenuItem dialer = menu.findItem(R.id.dialer);
		dialer.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
			    if(kafanaTelefon.equals("")){
				Toast.makeText(
					DesavanjeActivity.this,
					R.string.nema_informacija_broj_telefona,
					Toast.LENGTH_SHORT).show();
			    }else{
				Intent i = new Intent(Intent.ACTION_CALL);
				i.setData(Uri.parse("tel:" + kafanaTelefon));

				startActivity(i);
			    }
				return false;
			}
		});

		return true;
	}

	public void search(String query) {
		Intent i = new Intent(DesavanjeActivity.this, KafaneActivity.class);
		i.putExtra("search_input", query);
		startActivity(i);
	}

}
