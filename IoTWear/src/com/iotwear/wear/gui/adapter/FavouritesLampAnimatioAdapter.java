package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotwear.wear.R;

public class FavouritesLampAnimatioAdapter extends BaseAdapter {

	Activity activity;
	ArrayList<String> animations;
	
	private ArrayList<String> fillArray() {
	    animations.add("Random");
	    animations.add("Anim1");
		return animations;
	    }

	public FavouritesLampAnimatioAdapter(Activity activity) {
	    this.activity = activity;
	    animations = new ArrayList<String>();
	    animations = fillArray();
	}

	@Override
	public int getCount() {
		return animations.size();
	}

	@Override
	public Object getItem(int position) {
		return animations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		String item = animations.get(position);
		if (convertView == null) {
		    convertView = View.inflate(activity,
			    R.layout.item_favourites_picture, null);
		    holder = new ViewHolder();
		    holder.name = (TextView) convertView.findViewById(R.id.picture);

		    holder.name.setText(item);
		    convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();

		return convertView;
	}

	class ViewHolder {
		View color;
		TextView name;
	}

}

