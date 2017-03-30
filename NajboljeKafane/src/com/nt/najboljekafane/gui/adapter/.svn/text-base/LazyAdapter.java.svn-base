package com.nt.najboljekafane.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.util.ImageLoader;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<String> imageUrlList;
	private LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	public LazyAdapter(Activity a, ArrayList<String> imageUrlList) {
		activity = a;
		this.imageUrlList = imageUrlList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return imageUrlList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater
					.inflate(R.layout.adapter_galerije_item, null);

		ImageView image = (ImageView) convertView.findViewById(R.id.image);
		imageLoader.DisplayImage(imageUrlList.get(position), image);
		return convertView;
	}
}
