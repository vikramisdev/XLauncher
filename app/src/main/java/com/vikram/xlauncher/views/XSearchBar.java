package com.vikram.xlauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.search.SearchBar;

public class XSearchBar extends SearchBar {
  private Context context;

  public XSearchBar(Context context) {
    super(context);
    this.context = context;
  }

  public XSearchBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public XSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setMargins(int top, int right, int bottom, int left) {
    ViewGroup.MarginLayoutParams marginParams =
        (ViewGroup.MarginLayoutParams) this.getLayoutParams();
    marginParams.setMargins(left, top, right, bottom);
    this.setLayoutParams(marginParams);
  }
}
