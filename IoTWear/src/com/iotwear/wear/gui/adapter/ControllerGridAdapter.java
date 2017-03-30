package com.iotwear.wear.gui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.model.PiControl;

public class ControllerGridAdapter extends BaseAdapter {

	private ArrayList<PiControl> controlList;
	private LayoutInflater inflater;
	private Activity activity;

	public ControllerGridAdapter(ArrayList<PiControl> controlList,
			Activity activity) {
		this.controlList = controlList;
		this.activity = activity;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return controlList.size();
	}

	@Override
	public PiControl getItem(int position) {
		return controlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final PiControl item = controlList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_controller_icon, null);
			ViewHolder holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.controller_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.controller_name);
			convertView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.title.setText(item.getName());

		holder.icon.setImageDrawable(activity.getResources().getDrawable(
			item.getIcon()));
		
		/*switch (item.getPiControlType().ordinal()) {
		case 1:
			holder.icon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_taster));
			break;
		case 2:
			holder.icon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_dimmer));
			break;
		case 3:
			holder.icon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_action_link));
			break;
		case 4:
			holder.icon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_taster));
			break;
		case 5:
			holder.icon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_roletne));
			break;
		case 6:
			holder.icon.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_roletne));
			break;
		}*/

		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
	}

}
