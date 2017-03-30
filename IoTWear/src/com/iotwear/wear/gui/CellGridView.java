package com.iotwear.wear.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CellGridView extends GridView {

    public CellGridView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
