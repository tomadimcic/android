package com.nt.najboljekafane.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.adapters.KafaneListAdapter.StringAccentRemover;
import com.nt.najboljekafane.model.Pesma;

public class PesmeListAdapter extends BaseAdapter {

	ArrayList<Pesma> pesmeList;
	LayoutInflater inflater;
	ArrayList<Pesma> arraylistPesme;

	public PesmeListAdapter(ArrayList<Pesma> pesmeList, Activity activity) {
	    arraylistPesme = new ArrayList<Pesma>();
		this.pesmeList = pesmeList;
		inflater = activity.getLayoutInflater();
		arraylistPesme.addAll(pesmeList);
	}

	@Override
	public int getCount() {
		return pesmeList.size();
	}

	@Override
	public Object getItem(int position) {
		return pesmeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Pesma item = pesmeList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_pesme_item, null);

			holder = new ViewHolder();

			holder.pesma = (TextView) convertView.findViewById(R.id.pesme_name);
			holder.pesmaIzvodjac = (TextView) convertView
					.findViewById(R.id.pesma_izvodjac);
			holder.brojGlasova = (TextView) convertView
					.findViewById(R.id.broj_glasova);
			convertView.setTag(holder);
			holder.lista = (RelativeLayout) convertView
					.findViewById(R.id.pesme_row);
		}

		holder = (ViewHolder) convertView.getTag();

		String nazivPesme = item.getNaziv();
		if (nazivPesme.length() > 30) {
			holder.pesma.setText(nazivPesme.substring(0, 30) + "...");
		} else {

			holder.pesma.setText(nazivPesme);
		}

		holder.pesmaIzvodjac.setText(item.getIzvodjac());
		holder.brojGlasova.setText(String.valueOf(item.getBrojglasova()));

		holder.lista.setBackgroundResource(R.drawable.background_lista);

		return convertView;
	}

	// Filter Class
	public void filterPesme(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		pesmeList.clear();
		if (charText.length() == 0) {
			pesmeList.addAll(arraylistPesme);
		} else {
			for (Pesma pesma : arraylistPesme) {
				if (StringAccentRemover.removeAccents(pesma.getNaziv()).toLowerCase(Locale.getDefault())
						.contains(StringAccentRemover.removeAccents(charText))) {
					pesmeList.add(pesma);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void filterPesmeByIzvodjac(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		pesmeList.clear();
		if (charText.length() == 0) {
			pesmeList.addAll(arraylistPesme);
		} else {
			for (Pesma pesma : arraylistPesme) {
				if (StringAccentRemover.removeAccents(pesma.getIzvodjac()).toLowerCase(Locale.getDefault())
						.contains(StringAccentRemover.removeAccents(charText))) {
					pesmeList.add(pesma);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void filterFavorites(String charText) {
		//charText = charText.toLowerCase(Locale.getDefault());
		pesmeList.clear();
		if (charText.length() == 0) {
			pesmeList.addAll(arraylistPesme);
		} else {
			for (Pesma pesma : arraylistPesme) {
			    if (pesma.getOmiljena() != null) {
				if (pesma.getOmiljena().equals("da")) {
					pesmeList.add(pesma);
				}
			    }
			}
		}
		notifyDataSetChanged();
	}

	class ViewHolder {
		TextView pesma;
		TextView pesmaIzvodjac;
		TextView brojGlasova;
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
