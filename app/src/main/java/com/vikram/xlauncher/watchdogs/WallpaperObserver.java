package com.vikram.xlauncher.watchdogs;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.vikram.xlauncher.utils.WallpaperUtils;

public class WallpaperObserver extends ContentObserver {
  private Context context;
  private View wallpaperView;

  public WallpaperObserver(Context c, View view, Handler handler) {
    super(handler);
    this.context = c;
	this.wallpaperView = view;
  }

  @Override
  public void onChange(boolean selfChange) {
    super.onChange(selfChange);
    WallpaperUtils wallpaperUtils = new WallpaperUtils(context);
	wallpaperUtils.setLauncherWallpaper(wallpaperView);
  }
}
