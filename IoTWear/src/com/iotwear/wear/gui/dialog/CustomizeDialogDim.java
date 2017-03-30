package com.iotwear.wear.gui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.iotwear.wear.R;
import com.iotwear.wear.gui.ControllerGridActivity;
import com.iotwear.wear.gui.ValueSendingInterface;
import com.iotwear.wear.model.DimControl;
import com.iotwear.wear.model.TasterControl;
import com.iotwear.wear.task.SendSingleControlData;

public class CustomizeDialogDim extends Dialog implements OnClickListener, ValueSendingInterface {  
    Button okButton;  
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    boolean pressed = false;
    public boolean isSendingDimValue;
    public int currentDimValue;
    ControllerGridActivity mActivity;
    boolean isSending;
    int previous;
    
    public CustomizeDialogDim(Context context, ControllerGridActivity activity, final DimControl item) {  
        super(context);  
        mContext = context; 
        mActivity = activity;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.dialog_control_dimmer);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);   
	mTitle = (TextView) findViewById(R.id.dialogTitle);   
        okButton = (Button) findViewById(R.id.OkButtonDim); 
        okButton.setOnClickListener(this);  
        currentDimValue = item.getDimValue();
        isSending = false;
	
        SeekBar dimBar = (SeekBar) findViewById(R.id.seekbar_dimmer);
        dimBar.setProgress(currentDimValue);
        dimBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        		// TODO Auto-generated method stub
        
        	}
        
        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        		// TODO Auto-generated method stub
        
        	}
        
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress,
        			boolean fromUser) {
        		item.setDimValue(progress);
        		if (!isSending && (previous != item.getDimValue())) {
        		    previous = item.getDimValue();
        		    isSending = true;
				isSendingDimValue = true;
        			new SendSingleControlData(
        				mContext,
        					item,
        					((ValueSendingInterface) mContext))
        					.execute();
        			
        			Thread locThread;
        			
        			locThread = new Thread(){
        			    @Override
        			        public void run() {
        			            try {	            	
        			            	synchronized(this){
        			            		wait(50);
        			            	}

        			            } catch(InterruptedException e) {} 
        			            finally {
        			        	isSending = false;
        			            }
        			    }
        			    
        			};
        			
        			locThread.start();
        			
        		}
        		
        		
        		
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
		return mActivity;
	}

    
    
}  
