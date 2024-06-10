package com.vikram.xlauncher.views;

import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

public class XRecyclerView extends RecyclerView{
  public XRecyclerView(Context context) {
    super(context);
  }

  public XRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setMargins(int top, int right, int bottom, int left) {
    ViewGroup.MarginLayoutParams marginParams =
        (ViewGroup.MarginLayoutParams) this.getLayoutParams();
    marginParams.setMargins(left, top, right, bottom);
    this.setLayoutParams(marginParams);
  }
}
