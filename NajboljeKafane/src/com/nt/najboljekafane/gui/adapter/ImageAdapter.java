/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nt.najboljekafane.gui.adapter;

import com.nt.najboljekafane.util.ImageDownloader;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private String[] urls;

	private final ImageDownloader imageDownloader = new ImageDownloader();

	public ImageAdapter(String[] urls) {
		this.urls = urls;
	}

	public int getCount() {
		return urls.length;
	}

	public String getItem(int position) {
		return urls[position];
	}

	public long getItemId(int position) {
		return urls[position].hashCode();
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = new ImageView(parent.getContext());
		}

		imageDownloader.download(urls[position], (ImageView) view);

		return view;
	}

	public ImageDownloader getImageDownloader() {
		return imageDownloader;
	}
}
