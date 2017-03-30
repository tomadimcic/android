package com.nt.najboljekafane.gui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nt.najboljekafane.model.FacebookImage;
import com.nt.najboljekafane.util.ImageLoader;
import com.nt.najboljekafane.util.Utils;
import com.nt.najboljekafane.view.SquareImageView;

public class GalleryGridAdapter extends BaseAdapter {

	public static final int NASE_FOTKE = 0;
	public static final int FOTKE_KORISNIKA = 1;

	private ArrayList<FacebookImage> imageList;
	private Context context;
	private int screenWidth;
	private ImageLoader imageLoader;
	private int galleryType;

	public GalleryGridAdapter(Context context,
			ArrayList<FacebookImage> imageList, int galleryType) {
		this.context = context;
		this.imageList = imageList;
		screenWidth = Utils.getScreenWidth(context);
		imageLoader = new ImageLoader(context.getApplicationContext());
		this.galleryType = galleryType;
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int position) {
		return imageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return imageList.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			SquareImageView imageView = new SquareImageView(context);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			int width = screenWidth / 3;
			imageView.setLayoutParams(new GridView.LayoutParams(width, width));
			convertView = imageView;
		}
		if (galleryType == NASE_FOTKE)
			imageLoader.DisplayImage(
					imageList.get(position).sizeImageMap.get(2),
					(ImageView) convertView);
		else
			imageLoader.DisplayImage(imageList.get(position).source,
					(ImageView) convertView);
		return convertView;
	}

}
