package com.nt.najboljekafane.gui;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.task.GetTextReklameTask;
import com.nt.najboljekafane.util.ImageLoader;

public class RezervacijeActivity extends SherlockActivity {

 
    public static final String BROJ_TELEFONA_ZA_REZERVACIJE = "+381649922005";
    
    ImageButton rezervacijeBtn;
    TextView reklamaText;
    ImageView reklamaIkona;
    ImageLoader imageLoader;
    String[] reklameResult;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.rezervacije);
	// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	getSupportActionBar().setBackgroundDrawable(
		new ColorDrawable(Color.parseColor("#b11944")));
	getSupportActionBar().setBackgroundDrawable(
		new ColorDrawable(Color.parseColor("#b11944")));
	getSupportActionBar().setStackedBackgroundDrawable(
		new ColorDrawable(Color.parseColor("#b11944")));
	
	imageLoader = new ImageLoader(this,
		R.drawable.reklama);
	
	rezervacijeBtn = (ImageButton) findViewById(R.id.pozovi);
	reklamaIkona = (ImageView) findViewById(R.id.reklama_ikona);
	reklamaText = (TextView) findViewById(R.id.reklama_text);
	
	rezervacijeBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_CALL);
		i.setData(Uri.parse("tel:" + BROJ_TELEFONA_ZA_REZERVACIJE));

		startActivity(i);
		
	    }
	});
	
	String reklama = null;
	try {
	    reklama = new GetTextReklameTask().execute().get();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	reklameResult = new String[3];
	
	if(reklama == null){
	    reklameResult[1] = "www.najboljekafane.rs";
	    reklamaIkona.setBackgroundResource(R.drawable.reklama);
	    reklameResult[2] = "";
	}else{
	    reklameResult = fromJson(reklama);
	    imageLoader.DisplayImage(
			"http://www.najboljekafane.rs/android/images/certificates/" + reklameResult[0], reklamaIkona);
	}
	
	reklamaIkona.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		if (!reklameResult[1].startsWith("http://") && !reklameResult[1].startsWith("https://"))
		    reklameResult[1] = "http://" + reklameResult[1];
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reklameResult[1]));
		startActivity(browserIntent);
		
	    }
	});
	
	reklamaText.setTypeface(null, Typeface.BOLD);
	reklamaText.setText(reklameResult[2]);


    }
    
    public String[] fromJson(String result){
	String[] reklameResult1 = new String[3];
	try {
	    JSONObject json = new JSONObject(result.substring(1, result.length()-1));
	    reklameResult1[0] = json.getString("url_slika");
	    reklameResult1[1] = json.getString("url_link");
	    reklameResult1[2] = json.getString("url_text");
	    //reklameResult1[2] = "Pozovite nas!";
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return reklameResult1;
    }

}
