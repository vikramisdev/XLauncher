package com.vikram.xlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
  private Context context;
  private AppCompatActivity activity;

  public Utils(Context c) {
    this.context = c;
    this.activity = (AppCompatActivity) c;
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

  public void setViewShape(
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
    if (backgroundColor != 0) {
      shapeDrawable.getPaint().setColor(backgroundColor); // Set the background color
    }

    // Set the background of the view to the ShapeDrawable
    view.setBackground(shapeDrawable);
  }

  public void setViewShape(Context context, View view, int backgroundColor, float cornerRadiusPx) {
    float[] mCornerRadiusPx = {cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx};
    setViewShape(context, view, backgroundColor, mCornerRadiusPx);
  }

  public Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    int width = drawable.getIntrinsicWidth();
    int height = drawable.getIntrinsicHeight();
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }

  public Drawable uriToDrawable(Context context, Uri uri) {
    try {
      InputStream inputStream = context.getContentResolver().openInputStream(uri);
      return Drawable.createFromStream(inputStream, uri.toString());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Drawable bitmapToDrawable(Resources resources, Bitmap bitmap) {
    return new BitmapDrawable(resources, bitmap);
  }

  public Bitmap uriToBitmap(Context context, Uri uri) {
    Bitmap bitmap = null;
    try {
      InputStream inputStream = context.getContentResolver().openInputStream(uri);
      bitmap = BitmapFactory.decodeStream(inputStream);
      inputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  public int dpToPx(float dp) {
    float density = context.getResources().getDisplayMetrics().density;
    return (int) (dp * density + 0.5f); // 0.5f is added for rounding to the nearest whole number
  }

  public void setDeviceFullScreen() {
    Window window = activity.getWindow();
    window
        .getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }

  public void openMediaPicker() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    activity.startActivityForResult(intent, 53);
  }
}
