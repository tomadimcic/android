package com.nt.najboljekafane.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.nt.najboljekafane.R;
import com.nt.najboljekafane.gui.adapter.FullScreenImageAdapter;
import com.nt.najboljekafane.model.FacebookImage;

public class FullScreenViewActivity extends Activity{

	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		ArrayList<FacebookImage> imageList = (ArrayList<FacebookImage>) getIntent().getSerializableExtra(GalerijaActivity.EXTRA_FACEBOOK_IMAGE_LIST);
		int position = getIntent().getIntExtra(GalerijaActivity.EXTRA_SELECTED_POSITION, 0);
		viewPager = (ViewPager) findViewById(R.id.pager);

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				imageList);

		viewPager.setAdapter(adapter);

		viewPager.setCurrentItem(position);
	}
}
