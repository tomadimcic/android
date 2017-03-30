package com.iotwear.wear.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.actionbar.ActionBarHandler;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory;
import com.iotwear.wear.gui.actionbar.ActionBarHandlerFactory.Mode;
import com.iotwear.wear.model.Animation;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.task.led.SetAnimationTask;
import com.iotwear.wear.task.led.SetColorTask;
import com.iotwear.wear.util.Constants;
import com.iotwear.wear.util.DataManager;
import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.colorpicker.SVBar;

public class AnimationActivity extends BaseActivity implements OnClickListener, ValueSendingInterface {

	Animation animation;

	EditText name;
	Button delete, save, addColor, set;
	Spinner turnedOn, crossfade;
	//Spinner = turnedOff;
	LinearLayout colorContainer;
	View selectedItem = null;

	PiControl controller;

	boolean isNewAnimation;
	Button clicked;
	int clickedColor;
	private int currentColor;
	private boolean isSendingColor;
	long start = System.currentTimeMillis();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View content = LayoutInflater.from(this).inflate(
				R.layout.activity_animation, null);
		contentFrame.addView(content);

		controller = (PiControl) getIntent().getSerializableExtra(
				Constants.EXTRA_CONTROLLER);
		// TODO Get animation from bundle, just like PiDevice and set views and
		// buttons according to it...if null,

		if (controller == null) {
			// TODO check if animation is null...if it is, this should happen,
			// if not, proceed and init for that animation...
			Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, DeviceListActivity.class);
			startActivity(i);
			finish();
		} else {
			getActionBarHandler().setTitle(controller.getName());
		}

		set = (Button) findViewById(R.id.setAnimation);
		save = (Button) findViewById(R.id.saveAnimation);
		delete = (Button) findViewById(R.id.deleteAnimation);
		name = (EditText) findViewById(R.id.nameEditText);
		addColor = (Button) findViewById(R.id.addNewColor);
		turnedOn = (Spinner) findViewById(R.id.secsOnSpinner);
		//turnedOff = (Spinner) findViewById(R.id.secsOffSpinner);
		crossfade = (Spinner) findViewById(R.id.crossfadeSpinner);
		colorContainer = (LinearLayout) findViewById(R.id.colorContainer);

		set.setOnClickListener(this);
		save.setOnClickListener(this);
		delete.setOnClickListener(this);
		addColor.setOnClickListener(this);

		animation = (Animation) getIntent().getSerializableExtra(
				Constants.EXTRA_ANIMATION);
		if (animation == null) {
			animation = new Animation();
			isNewAnimation = true;
			delete.setVisibility(View.GONE);
		} else {
			delete.setVisibility(View.VISIBLE);
			initAnimationValues();
		}
		
		turnedOn.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parent,
			    View view, int position, long id) {
			if(!animation.getColorList().isEmpty()){
			    if(selectedItem == null)
				Toast.makeText(AnimationActivity.this,
					"Please select color!",Toast.LENGTH_SHORT).show();
			    else{
			    int pos = getColorPosition(selectedItem.getId());
			    animation.getOnList().set(pos, Double.parseDouble(turnedOn
					.getSelectedItem().toString()));
			    }
			}
			
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		    }
		});
		
		crossfade.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parent,
			    View view, int position, long id) {
			if(!animation.getColorList().isEmpty()){
			    if(selectedItem == null)
				Toast.makeText(AnimationActivity.this,
					"Please select color!",Toast.LENGTH_SHORT).show();
			    else{
			    int pos = getColorPosition(selectedItem.getId());
			    animation.getCrossfadeList().set(pos, Double.parseDouble(crossfade
					.getSelectedItem().toString()));
			    }
			}
			
		    }
		    
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		    }
		});
	}

	public void initAnimationValues() {
		initColorList();

	}

	public void initColorList() {
		colorContainer.removeAllViews();
		for (int i = 0; i < animation.getColorList().size(); i++) {
			final int color = animation.getColorList().get(i);
			View container = View.inflate(this, R.layout.item_animation_color,
					null);
			final Button item = (Button) container.findViewById(R.id.colorItem);
			item.setBackgroundColor(color);
			item.setId(getColorViewId(i));
			item.setOnLongClickListener(new OnLongClickListener() {
			    
			    @Override
			    public boolean onLongClick(View v) {
				final View selectedView = v;
				selectedItem = v;
				int pos = getColorPosition(selectedView.getId());
				    Double pom = animation.getOnList().get(pos);
				    int p = 0;
				    for (int i = 0; i<turnedOn.getAdapter().getCount(); i++) {
					if(Double.parseDouble(turnedOn.getItemAtPosition(i).toString()) == pom)
					    p = i;
				    }
				    turnedOn.setSelection(p);
				    
				    Double pom1 = animation.getCrossfadeList().get(pos);
				    for (int i = 0; i<crossfade.getAdapter().getCount(); i++) {
					if(Double.parseDouble(crossfade.getItemAtPosition(i).toString()) == pom1)
					    p = i;
				    }
				    crossfade.setSelection(p);
				final CharSequence[] items = { "Edit color" };
				//final CharSequence[] items = { "Edit color", "Delete color" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AnimationActivity.this);
				builder.setTitle("Color options").setItems(items,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									showColorDialog(getColorPosition(selectedView
											.getId()));
								}

							}
						});
				builder.show();
				return true;
			    }
			});
			
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				    
				    final View selectedView = v;
				    v.setSelected(true);
				    //v.setBackgroundColor(Color.LTGRAY);
				    if(clicked != null){
					GradientDrawable drawable = new GradientDrawable();
					drawable.setShape(GradientDrawable.RECTANGLE);
					drawable.setStroke(5, Color.WHITE);
					drawable.setColor(clickedColor);
					clicked.setBackgroundDrawable(drawable);
				    }
				    GradientDrawable drawable = new GradientDrawable();
				    drawable.setShape(GradientDrawable.RECTANGLE);
				    drawable.setStroke(5, Color.DKGRAY);
				    drawable.setColor(color);
				    item.setBackgroundDrawable(drawable);
				    selectedItem = v;
				    int pos = getColorPosition(selectedView.getId());
				    Double pom = animation.getOnList().get(pos);
				    int p = 0;
				    for (int i = 0; i<turnedOn.getAdapter().getCount(); i++) {
					if(Double.parseDouble(turnedOn.getItemAtPosition(i).toString()) == pom)
					    p = i;
				    }
				    turnedOn.setSelection(p);
				    
				    Double pom1 = animation.getCrossfadeList().get(pos);
				    for (int i = 0; i<crossfade.getAdapter().getCount(); i++) {
					if(Double.parseDouble(crossfade.getItemAtPosition(i).toString()) == pom1)
					    p = i;
				    }
				    crossfade.setSelection(p);
				    clicked = item;
				    clickedColor = color;
				    
				}
			});
			colorContainer.addView(container);
		}
	}

	public int getColorPosition(int id) {
		switch (id) {
		case R.id.animation_color_1:
			return 0;
		case R.id.animation_color_2:
			return 1;
		case R.id.animation_color_3:
			return 2;
		case R.id.animation_color_4:
			return 3;
		case R.id.animation_color_5:
			return 4;
		case R.id.animation_color_6:
			return 5;
		case R.id.animation_color_7:
			return 6;
		case R.id.animation_color_8:
			return 7;
		case R.id.animation_color_9:
			return 8;
		case R.id.animation_color_10:
			return 9;
		default:
			return -1;
		}
	}

	public int getColorViewId(int i) {
		switch (i) {
		case 0:
			return R.id.animation_color_1;
		case 1:
			return R.id.animation_color_2;
		case 2:
			return R.id.animation_color_3;
		case 3:
			return R.id.animation_color_4;
		case 4:
			return R.id.animation_color_5;
		case 5:
			return R.id.animation_color_6;
		case 6:
			return R.id.animation_color_7;
		case 7:
			return R.id.animation_color_8;
		case 8:
			return R.id.animation_color_9;
		case 9:
			return R.id.animation_color_10;
		default:
			return 0;
		}
	}

	public void showColorDialog(final int position) {
		View dialog = View.inflate(this, R.layout.dialog_color_picker, null);
		final ColorPicker picker = (ColorPicker) dialog
				.findViewById(R.id.picker);
		Button white = (Button) dialog.findViewById(R.id.white_btn);
		Button black = (Button) dialog.findViewById(R.id.black_btn);
		final SVBar opacity = (SVBar) dialog.findViewById(R.id.svbar);
		picker.addSVBar(opacity);
		start = System.currentTimeMillis();
		if(position != -1)
		    picker.setColor(animation.getColorList().get(position));
		// TODO Ako hoces da odabir boja bude na lampi, onda ovde implementirati
		// istu logiku kao na ColorPickerActivity
		picker.setOnColorChangedListener(new OnColorChangedListener() {
		
    			@Override
    			public void onColorChanged(int color) {
    			    currentColor = color;

    			    if (!isSendingColor && controller != null && (System.currentTimeMillis() - start > 300)) {
    				start = System.currentTimeMillis();
    				new SetColorTask(AnimationActivity.this, controller).execute(color);
    				picker.setColor(color);
    			    }
    		
    				}
		 	});
		
		white.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			currentColor = Color.WHITE;
			new SetColorTask(AnimationActivity.this, controller).execute(Color.WHITE);
			picker.setColor(Color.WHITE);
			
			
		    }
		});
		
		black.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			currentColor = Color.BLACK;
			new SetColorTask(AnimationActivity.this, controller).execute(Color.BLACK);
			picker.setColor(Color.BLACK);
			
		    }
		});

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("Add animation color")
				.setView(dialog)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int selColor = picker.getColor();
								if (position == -1) {
									animation.getColorList().add(selColor);
									animation.getOnList().add(1.0);
									animation.getCrossfadeList().add(0.0);
									turnedOn.setSelection(4);
									crossfade.setSelection(0);
								} else {
									animation.getColorList().set(position,
											selColor);
								}
								initColorList();
								dialog.dismiss();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setAnimation:
			setAnimation();
			break;
		case R.id.saveAnimation:
			saveAnimation();
			break;
		case R.id.deleteAnimation:
			deleteAnimation();
			break;

		case R.id.addNewColor:
			showColorDialog(-1);
			break;
		}
	}

	public void saveAnimation() {
		if (name.length() < 2) {
			Toast.makeText(this, "You must give animation a name.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		else if (animation.getColorList().size() == 0) {
			Toast.makeText(this,
					"You must add at least one color to animation.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!isNewAnimation)
			DataManager.getInstance().deleteAnimation(animation);

		animation.setName(name.getText().toString());
		/*animation.setLightOnDuration((Double.parseDouble(turnedOn
				.getSelectedItem().toString())));
		animation.setLightOffDuration((Double.parseDouble(turnedOff
				.getSelectedItem().toString())));

		animation.setCrossfadeDuration((Integer.parseInt(crossfade
				.getSelectedItem().toString())));*/

		DataManager.getInstance().addAnimation(animation);
		Toast.makeText(this, "Animation saved", Toast.LENGTH_SHORT).show();
		finish();
	}

	public void setAnimation() {

		if (animation.getColorList().size() == 0) {
			Toast.makeText(this,
					"You must add at least one color to animation.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		animation.setName(name.getText().toString());
		/*animation.setLightOnDuration((Double.parseDouble(turnedOn
				.getSelectedItem().toString())));
		animation.setLightOffDuration((Double.parseDouble(turnedOff
				.getSelectedItem().toString())));

		animation.setCrossfadeDuration((Integer.parseInt(crossfade
				.getSelectedItem().toString())));*/

		new SetAnimationTask(this, controller).execute(animation);
		// finish();
	}

	public void deleteAnimation() {
		DataManager.getInstance().deleteAnimation(animation);
		finish();
	}

	@Override
	public ActionBarHandler createActionBarHandler() {
		return ActionBarHandlerFactory.createActionBarHandler(this,
				"Animation", Mode.SLIDING);
	}
	
	@Override
	    public void setSending(boolean isSendingColor) {
		this.isSendingColor = isSendingColor;
	    }

	    @Override
	    public int getCurrentValue() {
		return currentColor;
	    }

	    @Override
	    public Activity getActivity() {
		return this;
	    }


}
