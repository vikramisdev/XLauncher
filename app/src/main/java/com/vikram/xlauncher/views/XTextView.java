package com.vikram.xlauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.textview.MaterialTextView;

public class XTextView extends TextView {

  public XTextView(Context context) {
    super(context);
  }

  public XTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public XTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setMargins(int top, int right, int bottom, int left) {
    ViewGroup.MarginLayoutParams marginParams =
        (ViewGroup.MarginLayoutParams) this.getLayoutParams();
    marginParams.setMargins(left, top, right, bottom);
    this.setLayoutParams(marginParams);
  }
}
