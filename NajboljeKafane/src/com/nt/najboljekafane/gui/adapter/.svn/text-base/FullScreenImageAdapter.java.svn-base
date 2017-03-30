package com.nt.najboljekafane.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.model.FacebookImage;
import com.nt.najboljekafane.util.ImageLoader;
import com.nt.najboljekafane.view.TouchImageView;

public class FullScreenImageAdapter extends PagerAdapter {

	public static final int NASE_FOTKE = 0;
	public static final int FOTKE_KORISNIKA = 1;

	private Activity activity;
	private ArrayList<FacebookImage> imageList;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<FacebookImage> imageList) {
		this.activity = activity;
		this.imageList = imageList;
		imageLoader = new ImageLoader(activity.getApplicationContext());
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TouchImageView imgDisplay;
		ImageView close;
		ImageView share;
		TextView date, name, comment;

		View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,
				container, false);

		imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
		close = (ImageView) viewLayout.findViewById(R.id.btnClose);
		share = (ImageView) viewLayout.findViewById(R.id.share_button);
		date = (TextView) viewLayout.findViewById(R.id.date);
		name = (TextView) viewLayout.findViewById(R.id.name);
		comment = (TextView) viewLayout.findViewById(R.id.comment);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		final FacebookImage item = imageList.get(position);
		if (item.name == null)
			imageLoader.DisplayImage(item.sizeImageMap.get(0),
					(ImageView) imgDisplay);
		else{
			imageLoader.DisplayImage(item.source, (ImageView) imgDisplay);
			date.setText(item.date);
			name.setText(item.name);
			comment.setText(item.comment);
		}

		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_SUBJECT, "Najbolje Kafane Foto");
				i.putExtra(Intent.EXTRA_TEXT, item.link);
				activity.startActivity(Intent.createChooser(i, "Podeli sliku"));
			}
		});

		((ViewPager) container).addView(viewLayout);

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}

}
