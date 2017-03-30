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
import com.iotwear.wear.gui.adapter.ControllerEditAdapter;
import com.iotwear.wear.model.PiControl;

public class CustomizeDialogIconChooser extends Dialog implements OnClickListener {  
    
    Context mContext;  
    TextView mTitle = null;  
    TextView mMessage = null;  
    View v = null;  
    ImageButton iconBtn1;
    ImageButton iconBtn2;
    ImageButton iconBtn3;
    ImageButton iconBtn4;
    ImageButton iconBtn5;
    ImageButton iconBtn6;
    boolean pressed = false;
    PiControl mItem;
    ControllerEditAdapter mAdapter;
    
    public CustomizeDialogIconChooser(Context context, PiControl item, ControllerEditAdapter adapter) {  
        super(context);  
        mContext = context; 
        mItem = item;
        mAdapter = adapter;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        /** Design the dialog in main.xml file */  
        setContentView(R.layout.edit_icons_dialog);  
        v = getWindow().getDecorView();  
        v.setBackgroundResource(android.R.color.transparent);  
        iconBtn1 = (ImageButton) v.findViewById(R.id.launcher_ikona);
        iconBtn2 = (ImageButton) v.findViewById(R.id.switch_ikona);
        iconBtn3 = (ImageButton) v.findViewById(R.id.dimmer_ikona);
        iconBtn4 = (ImageButton) v.findViewById(R.id.taster_ikona);  
        iconBtn5 = (ImageButton) v.findViewById(R.id.sijalica_ikona);
        iconBtn6 = (ImageButton) v.findViewById(R.id.roletne_ikona);
	mTitle = (TextView) findViewById(R.id.dialogTitle); 
	
	iconBtn1.setOnClickListener(this);
	iconBtn2.setOnClickListener(this);
	iconBtn3.setOnClickListener(this);
	iconBtn4.setOnClickListener(this);
	iconBtn5.setOnClickListener(this);
	iconBtn6.setOnClickListener(this);
        
    }  
    @Override  
    public void onClick(View v) { 
	int pom = 0;
        if (v == iconBtn1){
            mItem.setIcon(1);
            pom = 1;
        }
        if (v == iconBtn2){
            mItem.setIcon(2);
            pom = 2;
        }
        if (v == iconBtn3){ 
            mItem.setIcon(3);
            pom = 3;
        }
        if (v == iconBtn4){  
            mItem.setIcon(4);
            pom = 4;
        }
        if (v == iconBtn5){  
            mItem.setIcon(5);
            pom = 5;
        }
        if (v == iconBtn6){  
            mItem.setIcon(6);
            pom = 6;
        }
        mItem.setIcon(pom);
        mAdapter.notifyAdapter();
        dismiss();
        
    }  
    @Override  
    public void setTitle(CharSequence title) {  
        super.setTitle(title);  
        mTitle.setText("Choose icon");  
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