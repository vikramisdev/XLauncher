package com.vikram.xlauncher.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class XRelativeLayout extends RelativeLayout {
  private Context context;

  public XRelativeLayout(Context context) {
    super(context);
    this.context = context;
  }

  public XRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public XRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setMargins(
      int top, int right, int bottom, int left) {
    ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams)this.getLayoutParams();
	marginParams.setMargins(left, top, right, bottom);
	this.setLayoutParams(marginParams);
  }
}
