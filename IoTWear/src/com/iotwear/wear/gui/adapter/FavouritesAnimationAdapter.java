package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.model.Animation;
import com.iotwear.wear.util.DataManager;

public class FavouritesAnimationAdapter extends BaseAdapter {

	Activity activity;
	ArrayList<Animation> animations;

	public FavouritesAnimationAdapter(Activity activity) {
		this.activity = activity;
		animations = new ArrayList<Animation>();
		animations = DataManager.getInstance().getAnimationList();
	}

	public void refreshAnimationList() {
		animations = DataManager.getInstance().getAnimationList();
		notifyDataSetChanged();
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
		Animation item = animations.get(position);
		Log.i("TAG", item.toJson().toString());
		if (convertView == null) {
			convertView = View.inflate(activity,
					R.layout.item_favourites_animation, null);
			holder = new ViewHolder();
			holder.color = convertView.findViewById(R.id.color);
			holder.name = (TextView) convertView
					.findViewById(R.id.animationName);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();

		holder.name.setText(item.getName());
		if (item.getColorList().size() != 0)
			holder.color.setBackgroundColor(item.getColorList().get(0));
		return convertView;
	}

	class ViewHolder {
		View color;
		TextView name;
	}

}
