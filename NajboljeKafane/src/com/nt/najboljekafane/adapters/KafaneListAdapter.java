package com.nt.najboljekafane.adapters;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.util.ImageLoader;

public class KafaneListAdapter extends BaseAdapter {

	private ArrayList<Kafana> kafaneList;
	LayoutInflater inflater;
	ArrayList<Kafana> arraylistKafana;
	private ImageLoader imageLoader;

	public KafaneListAdapter(ArrayList<Kafana> kafaneList, Activity activity) {
	        arraylistKafana = new ArrayList<Kafana>();
		this.kafaneList = kafaneList;
		inflater = activity.getLayoutInflater();
		this.arraylistKafana.addAll(kafaneList);
		imageLoader = new ImageLoader(activity.getApplicationContext(),
				R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		return kafaneList.size();
	}

	@Override
	public Object getItem(int position) {
		return kafaneList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Kafana item = kafaneList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_kafane_item, null);

			holder = new ViewHolder();
			holder.kafaneName = (TextView) convertView
					.findViewById(R.id.kafane_name);
			holder.kafaneGrad = (TextView) convertView
					.findViewById(R.id.kafane_grad);
			holder.kafaneAdresa = (TextView) convertView
					.findViewById(R.id.kafane_adresa);
			holder.ocena = (TextView) convertView
					.findViewById(R.id.ocena_final);
			holder.brojGlasova = (TextView) convertView
					.findViewById(R.id.brojGlasova);
			holder.image = (ImageView) convertView
					.findViewById(R.id.kafaneIcon);
			holder.mapa = (TextView) convertView
					.findViewById(R.id.map);
			convertView.setTag(holder);
			holder.lista = (RelativeLayout) convertView
					.findViewById(R.id.kafane_row);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.kafaneName.setText(item.getNaziv());
		holder.kafaneGrad.setText(item.getGrad());
		holder.kafaneAdresa.setText(item.getAdresa());
		holder.brojGlasova.setText(item.getBrojGlasova());
		Float ocena_final = (item.getOcenaKafane() + item.getOcenaAtmosfere()) / 2;

		if(!item.getThumb().equals("no_photo"))
		    imageLoader.DisplayImage(
				"http://www.najboljekafane.rs/android/images/certificates/"
						+ item.getThumb(), holder.image);
		else
		    imageLoader.DisplayImage(
				"", holder.image);
		holder.ocena.setText(String.format("%.1f", ocena_final));

		if (item.getVip().equalsIgnoreCase("da")) {
			// holder.lista.setBackgroundColor(Color.parseColor("#8F8FBD"));
			holder.lista.setBackgroundResource(R.drawable.backgroundvip);
		} 
		else if (item.getThumb().equalsIgnoreCase("no_photo") || item.getThumb().equalsIgnoreCase("")) {
			// holder.lista.setBackgroundColor(Color.parseColor("#8F8FBD"));
			holder.lista.setBackgroundResource(R.drawable.background_lista_no_photo);
		} 
		
		else {
			holder.lista.setBackgroundResource(R.drawable.background_lista);
		}
		
		

		return convertView;
	}

	// Filter Class
	public void filterKafane(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		kafaneList.clear();
		if (charText.length() == 0) {
			kafaneList.addAll(arraylistKafana);
		} else {
			for (Kafana kafana : arraylistKafana) {
				if (StringAccentRemover.removeAccents(kafana.getNaziv()).toLowerCase(Locale.getDefault())
						.contains(StringAccentRemover.removeAccents(charText))) {
					kafaneList.add(kafana);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void filterKafaneByGrad(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		kafaneList.clear();
		if (charText.length() == 0) {
			kafaneList.addAll(arraylistKafana);
		} else {
			for (Kafana kafana : arraylistKafana) {
				if (kafana.getGrad().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					kafaneList.add(kafana);
				}
			}
		}
		notifyDataSetChanged();
	}

	class ViewHolder {
		TextView mapa;
		TextView brojGlasova;
		TextView kafaneName;
		TextView kafaneGrad;
		TextView kafaneAdresa;
		TextView ocena;
		ImageView image;
		RelativeLayout lista;
	}
	
	public static class StringAccentRemover {

	    @SuppressWarnings("serial")
	    private static final HashMap<Character, Character> accents  = new HashMap<Character, Character>(){
	        {
	            put('Ž', 'Z');
	            put('È', 'C');
	            put('Æ', 'C');
	            put('Š', 'S');
	            put('Ð', 'D');

	            put('æ', 'c');
	            put('è', 'c');
	            put('ž', 'z');
	            put('š', 's');
	            put('ð', 'd');
	        }
	    };
	    /**
	     * remove accented from a string and replace with ascii equivalent
	     */
	    public static String removeAccents(String s) {
	        char[] result = s.toCharArray();
	        for(int i=0; i<result.length; i++) {
	            Character replacement = accents.get(result[i]);
	            if (replacement!=null) result[i] = replacement;
	        }
	        return new String(result);
	    }

	}

}
