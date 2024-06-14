package com.vikram.xlauncher.extended;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class XBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {
    private Context context;

    public XBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    
    public void setStateCollapsed() {
        this.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    
    public void setStateExpanded() {
        this.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    
    public void setStateHalfExpanded() {
        this.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }
}