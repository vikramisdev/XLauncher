package com.vikram.xlauncher.utils;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
  private Context context;

  public Utils(Context c) {
    this.context = c;
  }

  @SuppressWarnings("deprecation")
  public DisplayMetrics getDisplayMetrics() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics;
  }

  public WindowManager getWindowManager() {
    return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  }

  public float getScreenHeight() {
    return getDisplayMetrics().heightPixels;
  }

  public float getScreenWidth() {
    return getDisplayMetrics().widthPixels;
  }

  public static void setViewShape(
      Context context, View view, int backgroundColor, float[] cornerRadiusDp) {
    // Convert corner radius from dp to pixels
    float cornerRadiusPxTopLeft =
        cornerRadiusDp[0] * context.getResources().getDisplayMetrics().density;
    float cornerRadiusPxTopRight = cornerRadiusPxTopLeft;

    float cornerRadiusPxBottomLeft =
        cornerRadiusDp[0] * context.getResources().getDisplayMetrics().density;
    float cornerRadiusPxBottomRight = cornerRadiusPxBottomLeft;

    // Define the radii for the corners (top-left, top-right, bottom-right, bottom-left)
    float[] outerRadii =
        new float[] {
          cornerRadiusPxTopLeft, cornerRadiusPxTopLeft, // Top-left
          cornerRadiusPxTopRight, cornerRadiusPxTopRight, // Top-right
          cornerRadiusPxBottomLeft, cornerRadiusPxBottomLeft, // Bottom-right
          cornerRadiusPxBottomRight, cornerRadiusPxBottomRight // Bottom-left
        };

    // Create a RoundRectShape with the defined outer radii
    RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);

    // Create a ShapeDrawable with the RoundRectShape
    ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
	if(backgroundColor != 0) {
		shapeDrawable.getPaint().setColor(backgroundColor); // Set the background color
	}
    

    // Set the background of the view to the ShapeDrawable
    view.setBackground(shapeDrawable);
  }

  public void setViewShape(Context context, View view, int backgroundColor, float cornerRadiusPx) {
    float[] mCornerRadiusPx = {cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx};
    setViewShape(context, view, backgroundColor, mCornerRadiusPx);
  }
}
