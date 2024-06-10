package com.vikram.xlauncher.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewGroupCompat;


public class XSearchView extends SearchView {
  private Context context;

  public XSearchView(Context context) {
    super(context);
    this.context = context;
  }

  public XSearchView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public XSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setMargins(
      int top, int right, int bottom, int left) {
    ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams)this.getLayoutParams();
	marginParams.setMargins(left, top, right, bottom);
	this.setLayoutParams(marginParams);
  }
}
