package com.iotwear.wear.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.FavouritesActivity;
import com.iotwear.wear.gui.adapter.FavouritesAnimationAdapter;
import com.iotwear.wear.gui.adapter.FavouritesColorAdapter;
import com.iotwear.wear.model.Animation;
import com.iotwear.wear.model.PiControl;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.led.SetAnimationTask;
import com.iotwear.wear.task.led.SetColorTask;
import com.iotwear.wear.util.DataManager;

public class CustomizeDialogFavourites extends Dialog implements OnClickListener {  
    Button okButton;  
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    Button deleteBtn;
    Button acivateBtn;
    boolean pressed = false;
    PiDevice mItem;
    FavouritesActivity mActivity;
    PiControl controller;
    Animation selectedAnimation;
    FavouritesAnimationAdapter animationAdapter;
    FavouritesColorAdapter colorAdapter;
    boolean mAnim;
    int selectedColor;
    
    public CustomizeDialogFavourites(FavouritesActivity activity, PiControl control, boolean anim) {  
        super(activity);  
        controller = control;
        mActivity = activity;
        mAnim = anim;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_favourites);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        deleteBtn = (Button) v.findViewById(R.id.delete_btn_favourites);
        acivateBtn = (Button) v.findViewById(R.id.activate_btn_favourites);  
	mTitle = (TextView) findViewById(R.id.dialogTitle);
	mMessage = (TextView) findViewById(R.id.favourites_message);
          
        deleteBtn.setOnClickListener(this);
        acivateBtn.setOnClickListener(this);
        
	
    }  
    @Override  
    public void onClick(View v) {  
        /** When OK Button is clicked, dismiss the dialog */  
        
        if (v == deleteBtn) { 
            if(mAnim){
                DataManager.getInstance().deleteAnimation(
    		    selectedAnimation);
    	    	animationAdapter.refreshAnimationList();
            }
            else{
        	DataManager.getInstance().deleteColor(
			    selectedColor);
		    colorAdapter.refreshColorsList();
            }
            
        }
        if (v == acivateBtn) { 
            if(mAnim){
        	new SetAnimationTask(
		    mActivity, controller)
		    .execute(selectedAnimation);
            }
            else{
        	new SetColorTask(mActivity,
			    controller).execute(selectedColor);
        	
            }
        }
        dismiss();
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
    public void setAnimAdapter(FavouritesAnimationAdapter animationAdapter) {
	this.animationAdapter = animationAdapter;
	
    }
    public Animation getSelectedAnimation() {
        return selectedAnimation;
    }
    public void setSelectedAnimation(Animation selectedAnimation) {
        this.selectedAnimation = selectedAnimation;
    }
    public int getSelectedColor() {
        return selectedColor;
    }
    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }
    public FavouritesColorAdapter getColorAdapter() {
        return colorAdapter;
    }
    public void setColorAdapter(FavouritesColorAdapter colorAdapter) {
        this.colorAdapter = colorAdapter;
    }  
    
    
    
}  
