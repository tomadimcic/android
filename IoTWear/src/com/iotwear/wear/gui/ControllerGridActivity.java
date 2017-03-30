package com.iotwear.wear.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.gui.adapter.ControllerGridAdapter;
import com.iotwear.wear.gui.dialog.CustomizeDialog;
import com.iotwear.wear.gui.dialog.CustomizeDialogBlindsTwo;
import com.iotwear.wear.gui.dialog.CustomizeDialogDeleteDevice;
import com.iotwear.wear.gui.dialog.CustomizeDialogDim;
import com.iotwear.wear.gui.dialog.CustomizeDialogSwitch;
import com.iotwear.wear.gui.dialog.CustomizeDialogTaster;
import com.iotwear.wear.model.BlindsControl;
import com.iotwear.wear.model.DimControl;
import com.iotwear.wear.model.GroupControl;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.model.SwitchControl;
import com.iotwear.wear.model.TasterControl;
import com.iotwear.wear.model.PiControl.PiControlType;
import com.iotwear.wear.task.SendSingleControlData;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;

public class ControllerGridActivity extends BaseActivity implements
		ValueSendingInterface {

	public PiDevice selectedDevice;
	public GridView controllerGrid;
	public ControllerGridAdapter controllerAdapter;

	public int currentDimValue;
	public boolean isSendingDimValue;
	boolean pressed = false;

	@Override
	public ActionBarHandler createActionBarHandler() {
		return ActionBarHandlerFactory.createActionBarHandler(this, "Device",
				Mode.NORMAL);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setMenu();
		selectedDevice = (PiDevice) getIntent().getExtras().getSerializable(
				Constants.EXTRA_DEVICE);
		if(selectedDevice != null)
		    getActionBarHandler().setTitle(selectedDevice.getName());
		View content = View.inflate(this, R.layout.activity_controllers_grid,
				null);
		contentFrame.addView(content);

		controllerGrid = (GridView) findViewById(R.id.controller_grid);
		controllerAdapter = new ControllerGridAdapter(
				selectedDevice.getControlList(), this);
		controllerGrid.setAdapter(controllerAdapter);

		controllerGrid
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						PiControl item = selectedDevice.getControlList().get(
								position);// controllerAdapter.getItem(position);
						item.setHostDevice(selectedDevice);
						switch (item.getPiControlType().ordinal()) {

						// LED
						case 0:
							Intent i = new Intent(ControllerGridActivity.this,
									LEDControllerActivity.class);
							i.putExtra(Constants.EXTRA_CONTROLLER, item);
							startActivity(i);
							break;

						// SWITCH
						case 1:
							SwitchControl switchItem = (SwitchControl) item;
							switchItem.setHostDevice(selectedDevice);

							showSwitchControlDialog(switchItem);
							break;
						// DIMMER
						case 2:
							DimControl dimItem = (DimControl) item;
							dimItem.setHostDevice(selectedDevice);
							showDimControlDialog(dimItem);
							break;
						case 3:
							GroupControl groupItem = (GroupControl) item;
							groupItem.setHostDevice(selectedDevice);
							showGroupDialog(groupItem);
							break;
						case 4:
							TasterControl tasterItem = (TasterControl) item;
							tasterItem.setHostDevice(selectedDevice);
							showTasterControlDialog(tasterItem);
							break;
						case 5:
							BlindsControl blindsFourItem = (BlindsControl) item;
							blindsFourItem.setHostDevice(selectedDevice);
							showBlindsFourControlDialog(blindsFourItem);
							break;
						case 6:
						    	BlindsControl blindsTwoItem = (BlindsControl) item;
						    	blindsTwoItem.setHostDevice(selectedDevice);
						    	showBlindsTwoControlDialog(blindsTwoItem);
							break;
						case 7:
							Intent i1 = new Intent(ControllerGridActivity.this,
									ICControlActivity.class);
							i1.putExtra(Constants.EXTRA_CONTROLLER, item);
							startActivity(i1);
							break;
						case 9:
							Intent i2 = new Intent(ControllerGridActivity.this,
									DMXControlActivity.class);
							i2.putExtra(Constants.EXTRA_CONTROLLER, item);
							startActivity(i2);
							break;
						default:
							break;
						}

					}

				});
	}
	
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
        	super.onResume();
        	selectedDevice.setControlList(selectedDevice.getControlList());
        	controllerAdapter = new ControllerGridAdapter(
        		selectedDevice.getControlList(), this);
        	controllerGrid.setAdapter(controllerAdapter);
        	controllerAdapter.notifyDataSetChanged();
        	controllerAdapter.notifyDataSetInvalidated();
	}

	public void showDimControlDialog(final DimControl item) {
	    
	    CustomizeDialogDim customizeDialog = new CustomizeDialogDim(ControllerGridActivity.this, this, item);  
            customizeDialog.setTitle(item.getName());   
            customizeDialog.show();
	}

	public void showSwitchControlDialog(final SwitchControl item) {
		
		CustomizeDialogSwitch customizeDialog = new CustomizeDialogSwitch(ControllerGridActivity.this, item);  
	            customizeDialog.setTitle(item.getName());   
	            customizeDialog.show();
	}

	public void showGroupDialog(GroupControl item) {
	    
	    CustomizeDialogGroup1 customizeDialog = new CustomizeDialogGroup1(ControllerGridActivity.this, item, selectedDevice, this);  
            customizeDialog.setTitle(item.getName());   
            customizeDialog.show();
	}
	
	public void showTasterControlDialog(final TasterControl item) {
	    CustomizeDialogTaster customizeDialog = new CustomizeDialogTaster(ControllerGridActivity.this, item);  
            customizeDialog.setTitle(item.getName());   
            customizeDialog.show();
	}
	
	public void showBlindsFourControlDialog(final BlindsControl item) {
	    
	    CustomizeDialog customizeDialog = new CustomizeDialog(ControllerGridActivity.this, item);  
            customizeDialog.setTitle(item.getName());   
            customizeDialog.show();
	    
	}
	
	public void showBlindsTwoControlDialog(final BlindsControl item) {
	    CustomizeDialogBlindsTwo customizeDialog = new CustomizeDialogBlindsTwo(ControllerGridActivity.this, item);  
            customizeDialog.setTitle(item.getName());   
            customizeDialog.show();
	}

	protected String[] calculateId(String id, int type) {
	    String[] ids = null;
	    String first = id.split("-")[0];
	    String last = id.split("-")[1];
	    if(type == 5){
		ids = new String[4];
		int second = Integer.parseInt(first) + 1;
		int third = Integer.parseInt(first) + 2;
		ids[0] = first;
		ids[1] = Integer.toString(second);
		ids[2] = Integer.toString(third);
		ids[3] = last;
	    }
	    if(type == 6){
		ids = new String[2];
		ids[0] = first;
		ids[3] = last;
	    }
	    return ids;
	}

	public void setMenu() {
		ImageView add = (ImageView) getLayoutInflater().inflate(
				R.layout.actionbar_menu_item, null);
		add.setId(R.id.actionbar_add);
		add.setImageDrawable(getResources().getDrawable(
				R.drawable.actionbar_add_selector));
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ControllerGridActivity.this,
						GroupControlActivity.class);
				i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
				startActivityForResult(i, Constants.REQUEST_ADD_NEW_GROUP);
			}
		});
		getActionBarHandler().addMenuItem(add);
		
		
		ImageView alarm = (ImageView) getLayoutInflater().inflate(
			R.layout.actionbar_menu_item, null);
		alarm.setId(R.id.actionbar_alarm);
		alarm.setImageDrawable(getResources().getDrawable(
        			R.drawable.actionbar_alarm_selector));
		alarm.setOnClickListener(new View.OnClickListener() {
        
        		@Override
        		public void onClick(View v) {
        			Intent i = new Intent(ControllerGridActivity.this,
        				AlarmControlActivity.class);
        			i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
        			startActivityForResult(i, Constants.REQUEST_ALARM);
        		}
        	});
        	getActionBarHandler().addMenuItem(alarm);
		

		ImageView settings = (ImageView) getLayoutInflater().inflate(
				R.layout.actionbar_menu_item, null);
		settings.setId(R.id.actionbar_settings);
		settings.setImageDrawable(getResources().getDrawable(
				R.drawable.actionbar_settings_selector));
		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ControllerGridActivity.this,
						DeviceControlsEditActivity.class);
				i.putExtra(Constants.EXTRA_DEVICE, selectedDevice);
				startActivityForResult(i,
						Constants.REQUEST_DEVICE_MULTICONTROLLER_EDIT);
			}
		});
		getActionBarHandler().addMenuItem(settings);
		
		ImageView delete = (ImageView) getLayoutInflater().inflate(
				R.layout.actionbar_menu_item, null);
		delete.setId(R.id.actionbar_delete);
		delete.setImageDrawable(getResources().getDrawable(
				R.drawable.actionbar_cancel_selector));
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDeleteDialog();
			}
		});

		getActionBarHandler().addMenuItem(delete);
	}

	public void showDeleteDialog() {
	    CustomizeDialogDeleteDevice customizeDialog = new CustomizeDialogDeleteDevice(ControllerGridActivity.this, this, selectedDevice);  
            customizeDialog.setTitle(selectedDevice.getName());   
            customizeDialog.setMessage("This device will be deleted. Are you sure?");
            customizeDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUEST_DEVICE_MULTICONTROLLER_EDIT
				|| requestCode == Constants.REQUEST_ADD_NEW_GROUP
				|| requestCode == Constants.REQUEST_EDIT_GROUP) {
			if (resultCode == RESULT_OK) {
				selectedDevice = (PiDevice) data
						.getSerializableExtra(Constants.EXTRA_DEVICE);
				controllerAdapter = new ControllerGridAdapter(
						selectedDevice.getControlList(), this);
				controllerGrid.setAdapter(controllerAdapter);

			}
		}
	}

	@Override
	public void setSending(boolean isSending) {
		isSendingDimValue = isSending;

	}

	@Override
	public int getCurrentValue() {
		return currentDimValue;
	}

	@Override
	public Activity getActivity() {
		return this;
	}
	
	public class CustomizeDialogGroup1 extends Dialog implements OnClickListener {  
	    Button okButton;  
	    Context mContext;  
	    TextView mTitle = null;  
	    TextView mMessage = null;  
	    View v = null;  
	    Button groupButton1;
	    Button groupButton2;
	    Button groupButton3;
	    Button groupButton4;
	    boolean pressed = false;
	    GroupControl mItem;
	    PiDevice mDevice;
	    ControllerGridActivity mActivity;
	    
	    public CustomizeDialogGroup1(Context context, GroupControl item, PiDevice device, ControllerGridActivity activity) {  
	        super(context);  
	        mContext = context; 
	        mItem = item;
	        mDevice = device;
	        mActivity = activity;
	        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        /** Design the dialog in main.xml file */  
	        setContentView(R.layout.dialog_control_group);  
	        v = getWindow().getDecorView();  
	        v.setBackgroundResource(android.R.color.transparent);  
	        groupButton1 = (Button) v.findViewById(R.id.button_turn_on_group);
	        groupButton2 = (Button) v.findViewById(R.id.button_turn_off_group);  
	        groupButton3 = (Button) v.findViewById(R.id.button_edit_group);
	        groupButton4 = (Button) v.findViewById(R.id.button_delete_group);
		mTitle = (TextView) findViewById(R.id.dialogTitle);   
	        okButton = (Button) findViewById(R.id.OkButtonGroup); 
	        okButton.setOnClickListener(this);  
	        groupButton1.setOnClickListener(this);
	        groupButton2.setOnClickListener(this);
	        groupButton3.setOnClickListener(this);
	        groupButton4.setOnClickListener(this);
	        
		
	    }  
	    @Override  
	    public void onClick(View v) {  
	        /** When OK Button is clicked, dismiss the dialog */  
	        if (v == okButton)  
	            dismiss();
	        if (v == groupButton1) { 
	            GroupControl ctrl = new GroupControl();
			ctrl = mItem;
			for(PiControl pi : ctrl.getGroupControlList()){
				if(pi.getPiControlType() == PiControlType.SWITCH){
					((SwitchControl)pi).setTurnedOn(true);
				}
				else{
					((DimControl)pi).setDimValue(100);
				}
			}
			new SendSingleControlData(mContext, mItem)
					.execute();
	        }
	        if (v == groupButton2) { 
	            GroupControl ctrl = new GroupControl();
			ctrl = mItem;
			for(PiControl pi : ctrl.getGroupControlList()){
				if(pi.getPiControlType() == PiControlType.SWITCH){
					((SwitchControl)pi).setTurnedOn(false);
				}
				else{
					((DimControl)pi).setDimValue(0);
				}
			}
			new SendSingleControlData(mContext, mItem)
					.execute();
	        }
	        if(v == groupButton3){
	            Intent i = new Intent(mContext,
				GroupControlActivity.class);
	            i.putExtra(Constants.EXTRA_DEVICE, mDevice);
	            i.putExtra(Constants.EXTRA_GROUP_CONTROL, mItem);
	            mActivity.startActivityForResult(i, Constants.REQUEST_EDIT_GROUP);
	            dismiss();
	        }
	        if(v == groupButton4){
	            mDevice.getControlList().remove(mItem);
			boolean res = DataManager.getInstance().saveDevice(
					mDevice);
			if (res) {
			    selectedDevice = mDevice;
			    controllerAdapter.notifyDataSetChanged();
			    dismiss();
			} else {
				Toast.makeText(mContext,
						"Error deleting device.",
						Toast.LENGTH_SHORT).show();
			}
	        }
	    }  
	    @Override  
	    public void setTitle(CharSequence title) {  
	        super.setTitle(title);  
	        mTitle.setText(title);  
	    }  
	    @Override  
	    public void setTitle(int titleId) {  
	        super.setTitle(titleId);  
	        mTitle.setText(mContext.getResources().getString(titleId));  
	    }  
	    /**  
	     * Set the message text for this dialog's window.  
	     *   
	     * @param message  
	     *      - The new message to display in the title.  
	     */  
	    public void setMessage(CharSequence message) {  
	        mMessage.setText(message);  
	        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
	    }  
	    /**  
	     * Set the message text for this dialog's window. The text is retrieved from the resources with the supplied  
	     * identifier.  
	     *   
	     * @param messageId  
	     *      - the message's text resource identifier <br>  
	     * @see <b>Note : if resourceID wrong application may get crash.</b><br>  
	     *   Exception has not handle.  
	     */  
	    public void setMessage(int messageId) {  
	        mMessage.setText(mContext.getResources().getString(messageId));  
	        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());  
	    }  
	    
	}  

}
