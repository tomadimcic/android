package com.iotwear.wear.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iotwear.wear.R;
import com.iotwear.wear.model.TasterControl;
import com.iotwear.wear.task.SendSingleControlData;

public class CustomizeDialogTaster extends Dialog implements OnClickListener {  
    Button okButton;  
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    ImageButton tasterButton;
    boolean pressed = false;
    long startTime;
    
    public CustomizeDialogTaster(Context context, final TasterControl item) {  
        super(context);  
        mContext = context; 
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_control_taster);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        tasterButton = (ImageButton) v.findViewById(R.id.button_taster);  
	mTitle = (TextView) findViewById(R.id.dialogTitle);   
        okButton = (Button) findViewById(R.id.OkButtonTaster); 
        okButton.setOnClickListener(this);  
        
        tasterButton.setBackgroundResource(R.drawable.taster);
	tasterButton.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            if (arg1.getAction()==MotionEvent.ACTION_DOWN){
	        	if(!pressed){
	        	    startTime = System.currentTimeMillis();
	        	    tasterButton.setBackgroundResource(R.drawable.taster_pritisnut);
	        	    item.setTurnedOn(true);
	        	    new SendSingleControlData(mContext, item).execute();
	        	    pressed = true;
	        	}
	        	else{
	        	    System.out.println("Pritisnuto");
	        	}
	            }
	            else if (arg1.getAction() == MotionEvent.ACTION_UP){
	        	if((System.currentTimeMillis() - startTime) < 400){
	        	    try {
				Thread.sleep(400);
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
	        	}
	        	tasterButton.setBackgroundResource(R.drawable.taster);
	        	item.setTurnedOn(false);
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
    
}  
