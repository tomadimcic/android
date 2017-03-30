package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.adapter.FavouritesColorAdapter.ViewHolder;
import com.iotwear.wear.util.DataManager;

public class FavouritesPictureAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<String> pictures;

    public FavouritesPictureAdapter(Activity activity) {
	this.activity = activity;
	pictures = new ArrayList<String>();
	pictures = fillArray();
    }
    
    private ArrayList<String> fillArray() {
	pictures.add("smile");
	pictures.add("flat");
	pictures.add("sad");
	pictures.add("gradient1");
	pictures.add("gradient2");
	pictures.add("gradient3");
	pictures.add("gradient4");
	pictures.add("hart1");
	pictures.add("hart2");
	pictures.add("hart3");
	pictures.add("peace");
	pictures.add("american");
	pictures.add("france");
	pictures.add("germany");
	pictures.add("serbia");
	pictures.add("monster1");
	pictures.add("monster2");
	pictures.add("monster3");
	pictures.add("monster4");
	pictures.add("flower");
	pictures.add("apple");
	pictures.add("android");
	pictures.add("packman");
	pictures.add("correctb");
	pictures.add("redXb");
	pictures.add("correct");
	pictures.add("redX");
	pictures.add("fb");
	pictures.add("twitt");
	pictures.add("mail");
	pictures.add("batman");
	return pictures;
    }

    @Override
    public int getCount() {
	return pictures.size();
    }

    @Override
    public Object getItem(int position) {
	return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	String picturee = pictures.get(position);
	if (convertView == null) {
	    convertView = View.inflate(activity,
		    R.layout.item_favourites_picture, null);
	    holder = new ViewHolder();
	    holder.picture = (TextView) convertView.findViewById(R.id.picture);

	    holder.picture.setText(picturee);
	    convertView.setTag(holder);
	}

	holder = (ViewHolder) convertView.getTag();

	return convertView;
    }

    class ViewHolder {
	TextView picture;
    }

}
