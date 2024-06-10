package com.vikram.xlauncher.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import java.io.IOException;

public class WallpaperUtils {
  private Context context;
  private Utils utils;

  public WallpaperUtils(Context c) {
    this.context = c;
    this.utils = new Utils(c);
  }

  public Drawable getDeviceWallpaper() {
    return context.getWallpaper();
  }

  public WallpaperManager getWallpaperManager() {
    return WallpaperManager.getInstance(context);
  }

  // set wallpaper to the wallpaper view in the launcher
  public void setLauncherWallpaper(View view) {
    view.setBackgroundDrawable(getDeviceWallpaper());
  }

  public void setDeviceWallpaper(Uri uri) {
    try {
      context.setWallpaper(utils.uriToBitmap(context, uri));
    } catch (IOException e) {
    }
  }
}
