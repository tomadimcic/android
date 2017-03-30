package com.iotwear.wear.gui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.ControllerGridActivity;
import com.iotwear.wear.gui.dialog.CustomizeDialogIconChooser;
import com.iotwear.wear.gui.dialog.CustomizeDialogTaster;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.PiControl.PiControlType;

public class ControllerEditAdapter extends BaseAdapter {

    private PiDevice selectedDevice;
    private Activity activity;
    private LayoutInflater inflater;
    TextView textview;
    int i;

    public ControllerEditAdapter(Activity activity, PiDevice selectedDevice) {
	this.selectedDevice = selectedDevice;
	this.activity = activity;
	inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
	return selectedDevice.getControlList().size();
    }

    @Override
    public Object getItem(int position) {
	return selectedDevice.getControlList().get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	convertView = inflater.inflate(R.layout.item_controller_edit, null);

	final View view = convertView;
	
	holder = new ViewHolder();
	holder.icon = (ImageView) convertView
		.findViewById(R.id.controller_icon);
	holder.name = (EditText) convertView.findViewById(R.id.controller_name);
	holder.delete = (ImageView) convertView.findViewById(R.id.delete_control_button);
	convertView.setTag(holder);

	holder = (ViewHolder) convertView.getTag();
	final int pos = position;
	final PiControl item = selectedDevice.getControlList().get(position);
	
	holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    item.getIcon()));
	/*switch (item.getPiControlType().ordinal()) {
	// LED
	case 0:
	    holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    R.drawable.ic_launcher));
	    break;
	// SWITCH
	case 1:
	    holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    R.drawable.ic_switch));
	    break;
	// DIMER
	case 2:
	    holder.icon.setImageDrawable(activity.getResources().getDrawable(
		    R.drawable.ic_dimmer));
	    break;
	}*/

	holder.name.setText(item.getName());
	holder.name.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		selectedDevice.getControlList().get(pos).setName(s.toString());
		Log.i("TAG", "EditText val: " + s.toString());
		Log.i("TAG", "DeviceControl name val: "
			+ selectedDevice.getControlList().get(pos).getName());

	    }
	});
	
	holder.icon.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		CustomizeDialogIconChooser customizeDialog = new CustomizeDialogIconChooser(view.getContext(), item, ControllerEditAdapter.this);  
	            customizeDialog.setTitle(item.getName());   
	            customizeDialog.show();			
		
	    }

	});
	
	holder.delete.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		selectedDevice.getControlList().remove(pos);
		notifyAdapter();
		
	    }
	});
	
	if(item.getPiControlType() == PiControlType.GROUP){
	    holder.name.setEnabled(false);
	    holder.icon.setClickable(false);
	}

	return convertView;
    }
    
    public void notifyAdapter(){
	notifyDataSetChanged();
    }

    public PiDevice getDevice() {
	return selectedDevice;
    }

    class ViewHolder {
	ImageView icon;
	EditText name;
	ImageView delete;
    }
    
    public int[] getIcons(){
	int[] icons = {R.drawable.ic_launcher,
		R.drawable.ic_switch,
		R.drawable.ic_taster
	};
	return icons;
    }
}
