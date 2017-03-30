package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iotwear.wear.R;
import com.iotwear.wear.util.DataManager;

public class FavouritesColorAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Integer> colors;

    public FavouritesColorAdapter(Activity activity) {
	this.activity = activity;
	colors = new ArrayList<Integer>();
	colors = DataManager.getInstance().getColors();
    }
    
    public void refreshColorsList() {
	colors = DataManager.getInstance().getColors();
	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
	return colors.size();
    }

    @Override
    public Object getItem(int position) {
	return colors.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	int color = colors.get(position);
	if (convertView == null) {
	    convertView = View.inflate(activity,
		    R.layout.item_favourites_color, null);
	    holder = new ViewHolder();
	    holder.color = convertView.findViewById(R.id.color);

	    convertView.setTag(holder);
	}

	holder = (ViewHolder) convertView.getTag();
	holder.color.setBackgroundColor(color);

	return convertView;
    }

    class ViewHolder {
	View color;
    }

}
