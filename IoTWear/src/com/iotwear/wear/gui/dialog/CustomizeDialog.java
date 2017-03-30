package com.iotwear.wear.gui.dialog;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.ControllerGridActivity;
import com.iotwear.wear.model.BlindsControl;
import com.iotwear.wear.task.SendSingleControlData;

import android.app.Dialog;  
import android.content.Context;  
import android.text.method.ScrollingMovementMethod;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.Window;  
import android.widget.Button;  
import android.widget.ImageButton;
import android.widget.TextView;  

/** Class Must extends with Dialog */  
/** Implement onClickListener to dismiss dialog when OK Button is pressed */  
public class CustomizeDialog extends Dialog implements OnClickListener {  
    Button okButton;  
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    ImageButton blindButton1;
    ImageButton blindButton2;
    ImageButton blindButton3;
    ImageButton blindButton4;
    boolean pressed = false;
    long startTime;
    
    public CustomizeDialog(Context context, final BlindsControl item) {  
        super(context);  
        mContext = context; 
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_control_blind_four);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        blindButton1 = (ImageButton) v.findViewById(R.id.strelica_na_gore_button);
	blindButton2 = (ImageButton) v.findViewById(R.id.strelica_na_dole_button);
	blindButton3 = (ImageButton) v.findViewById(R.id.rotacija_na_dole_button);
	blindButton4 = (ImageButton) v.findViewById(R.id.rotacija_na_gore_button);  
	mTitle = (TextView) findViewById(R.id.dialogTitle);   
        okButton = (Button) findViewById(R.id.OkButtonFour); 
        okButton.setOnClickListener(this);  
        
        blindButton1.setBackgroundResource(R.drawable.roletne_strelica_na_gore);
        blindButton2.setBackgroundResource(R.drawable.roletne_strelica_na_dole);
        blindButton3.setBackgroundResource(R.drawable.roletne_rotacija_na_dole);
        blindButton4.setBackgroundResource(R.drawable.roletne_rotacija_na_gore);
        
        final String[] ids = calculateId(item.getId(), 5);
	item.setIds(ids);
	
	blindButton1.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            if (arg1.getAction()==MotionEvent.ACTION_DOWN){
	        	if(!pressed){
	        	    startTime = System.currentTimeMillis();
	        	    blindButton1.setBackgroundResource(R.drawable.roletne_strelica_na_gore_pritisnuto);
	        	    item.setTurnedOn(true);
	        	    item.setCurrentId(0);
	        	    new SendSingleControlData(mContext, item).execute();
	        	    pressed = true;
	        	}
	            }
	            else if (arg1.getAction() == MotionEvent.ACTION_UP){
	        	if((System.currentTimeMillis() - startTime) < 100){
	        	    try {
				Thread.sleep(150);
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
	        	}
	        	blindButton1.setBackgroundResource(R.drawable.roletne_strelica_na_gore);
	        	item.setTurnedOn(false);
	        	item.setCurrentId(0);
			new SendSingleControlData(mContext, item)
					.execute();
			pressed = false;
	            }
	            return true;
	        }
	    });
	
	blindButton2.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            if (arg1.getAction()==MotionEvent.ACTION_DOWN){
	        	if(!pressed){
	        	    startTime = System.currentTimeMillis();
	        	    blindButton2.setBackgroundResource(R.drawable.roletne_strelica_na_dole_pritisnuto);
	        	    item.setTurnedOn(true);
	        	    item.setCurrentId(1);
	        	    new SendSingleControlData(mContext, item).execute();
	        	    pressed = true;
	        	}
	            }
	            else if (arg1.getAction() == MotionEvent.ACTION_UP){
	        	if((System.currentTimeMillis() - startTime) < 100){
	        	    try {
				Thread.sleep(150);
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
	        	}
	        	blindButton2.setBackgroundResource(R.drawable.roletne_strelica_na_dole);
	        	item.setTurnedOn(false);
	        	item.setCurrentId(1);
			new SendSingleControlData(mContext, item)
					.execute();
			pressed = false;
	            }
	            return true;
	        }
	    });
	
	blindButton3.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            if (arg1.getAction()==MotionEvent.ACTION_DOWN){
	        	if(!pressed){
	        	    startTime = System.currentTimeMillis();
	        	    blindButton3.setBackgroundResource(R.drawable.roletne_rotacija_na_dole_pritisnuto);
	        	    item.setTurnedOn(true);
	        	    item.setCurrentId(2);
	        	    new SendSingleControlData(mContext, item).execute();
	        	    pressed = true;
	        	}
	            }
	            else if (arg1.getAction() == MotionEvent.ACTION_UP){
	        	if((System.currentTimeMillis() - startTime) < 100){
	        	    try {
				Thread.sleep(150);
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
	        	}
	        	blindButton3.setBackgroundResource(R.drawable.roletne_rotacija_na_dole);
	        	item.setTurnedOn(false);
	        	item.setCurrentId(2);
			new SendSingleControlData(mContext, item)
					.execute();
			pressed = false;
	            }
	            return true;
	        }
	    });
	
	blindButton4.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            if (arg1.getAction()==MotionEvent.ACTION_DOWN){
	        	if(!pressed){
	        	    startTime = System.currentTimeMillis();
	        	    blindButton4.setBackgroundResource(R.drawable.roletne_rotacija_na_gore_pritisnuto);
	        	    item.setTurnedOn(true);
	        	    item.setCurrentId(3);
	        	    new SendSingleControlData(mContext, item).execute();
	        	    pressed = true;
	        	}
	            }
	            else if (arg1.getAction() == MotionEvent.ACTION_UP){
	        	if((System.currentTimeMillis() - startTime) < 100){
	        	    try {
				Thread.sleep(150);
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
	        	}
	        	blindButton4.setBackgroundResource(R.drawable.roletne_rotacija_na_gore);
	        	item.setTurnedOn(false);
	        	item.setCurrentId(3);
			new SendSingleControlData(mContext, item)
					.execute();
			pressed = false;
	            }
	            return true;
	        }
	    });
    }  
    @Override  
    public void onClick(View v) {  
        /** When OK Button is clicked, dismiss the dialog */  
        if (v == okButton)  
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
}  
