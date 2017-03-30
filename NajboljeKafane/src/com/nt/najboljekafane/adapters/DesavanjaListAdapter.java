package com.nt.najboljekafane.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.util.ImageLoader;

public class DesavanjaListAdapter extends BaseAdapter {

	ArrayList<Desavanje> desavanjaList;
	LayoutInflater inflater;
	ArrayList<Desavanje> arraylistDesavanja;
	private ImageLoader imageLoader;

	DatabaseHandler db;

	public DesavanjaListAdapter(ArrayList<Desavanje> desavanjaList,
			Activity activity, DatabaseHandler db) {
		arraylistDesavanja = new ArrayList<Desavanje>();
		this.desavanjaList = desavanjaList;
		this.db = db;
		inflater = activity.getLayoutInflater();
		arraylistDesavanja.addAll(desavanjaList);
		imageLoader = new ImageLoader(activity.getApplicationContext(),
				R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		return desavanjaList.size();
	}

	@Override
	public Object getItem(int position) {
		return desavanjaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Desavanje item = desavanjaList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_desavanja_item,
					null);
			// convertView = View.inflate(kafaneActivity,
			// R.layout.adapter_desavanja_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.desavanja_name);
			holder.kafana = (TextView) convertView
					.findViewById(R.id.desavanja_kafana_name);
			holder.grad = (TextView) convertView
					.findViewById(R.id.desavanja_grad);
			// holder.vreme = (TextView) convertView
			// .findViewById(R.id.desavanja_vreme);
			holder.lista = (RelativeLayout) convertView
					.findViewById(R.id.desavanja_row);
			holder.image = (ImageView) convertView
					.findViewById(R.id.desavanjaIcon);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.name.setText(item.getNaslov());
		holder.kafana.setText(item.getKafana());
		holder.grad.setText(item.getGrad() + ", " + item.getDatum());
		// holder.vreme.setText(item.getDatum());
		Kafana kafana = db.getKafanaByKafanaId(item.getKafanaId());

		if (!kafana.getThumb().equals("no_photo"))
			imageLoader.DisplayImage(
					"http://www.najboljekafane.rs/android/images/certificates/"
							+ kafana.getThumb(), holder.image);
		else
			holder.image.setBackgroundResource(R.drawable.ic_launcher);

		if (item.getVip() != null) {
			if (item.getVip().equalsIgnoreCase("da")) {
				// lista.setBackgroundColor(Color.parseColor("#8F8FBD"));
				holder.lista.setBackgroundResource(R.drawable.backgroundvip);
			} else
				holder.lista.setBackgroundResource(R.drawable.background_lista);
		}

		return convertView;
	}

	// Filter Class
	public void filterDesavanja(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		desavanjaList.clear();
		if (charText.length() == 0) {
			desavanjaList.addAll(arraylistDesavanja);
		} else {
			for (Desavanje desavanja : arraylistDesavanja) {
				if (desavanja.getNaslov().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					desavanjaList.add(desavanja);
				}
			}
		}
		notifyDataSetChanged();
	}

	public void filterDesavanjaByKafane(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		desavanjaList.clear();
		if (charText.length() == 0) {
			desavanjaList.addAll(arraylistDesavanja);
		} else {
			for (Desavanje desavanja : arraylistDesavanja) {
				if (desavanja.getKafana().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					desavanjaList.add(desavanja);
				}
			}
		}
		notifyDataSetChanged();
	}

	class ViewHolder {
		TextView grad;
		TextView name;
		TextView vreme;
		TextView kafana;
		ImageView image;
		RelativeLayout lista;
	}

	public void setDb(DatabaseHandler db) {
		this.db = db;

	}

}