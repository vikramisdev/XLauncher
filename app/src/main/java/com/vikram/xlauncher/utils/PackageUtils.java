package com.vikram.xlauncher.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.vikram.xlauncher.models.AppModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PackageUtils {
  private PackageManager packageManager;
  private Context context;
  private List<AppModel> appList = new ArrayList<>();
  private Utils utils;

  public PackageUtils(Context c) {
    this.context = c;
    this.packageManager = context.getPackageManager();
    this.utils = new Utils(c);
  }

  public List<AppModel> getAppList() {
    // Clear existing appList before populating
    appList.clear();

    // Retrieve installed apps
    List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

    for (PackageInfo p : packages) {
      ApplicationInfo app = p.applicationInfo;

      // Check if the app has a launcher intent
      Intent launchIntent = packageManager.getLaunchIntentForPackage(app.packageName);
      if (launchIntent == null) {
        continue;
      }

      String appName = packageManager.getApplicationLabel(app).toString();
      Drawable appIcon = packageManager.getApplicationIcon(app);
      double version = 0; // To-do: Fetch app version if needed
      String packageName = app.packageName;
      boolean canUninstall = canUninstall(app);

      AppModel appModel = new AppModel(appName, appIcon, version, packageName, canUninstall);
      appList.add(appModel);
    }

    // Sort appList alphabetically by app name
    Collections.sort(
        appList,
        new Comparator<AppModel>() {
          @Override
          public int compare(AppModel appModel1, AppModel appModel2) {
            return appModel1.getAppName().compareToIgnoreCase(appModel2.getAppName());
          }
        });

    return appList;
  }

  // Method to check if an app can be uninstalled (not a system app)
  public boolean canUninstall(ApplicationInfo appInfo) {
    return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
  }

  // Method to launch an app using its package name
  public void launchApp(String packageName) {
    Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
    if (launchIntent != null) {
      context.startActivity(launchIntent);
    } else {
      Log.e("PackageUtils", "Launch intent not found for package: " + packageName);
    }
  }

  // Method to sort appList in reverse order (optional, if needed)
  public void sortAppListReverse() {
    Collections.sort(
        appList,
        new Comparator<AppModel>() {
          @Override
          public int compare(AppModel appModel1, AppModel appModel2) {
            return appModel2.getAppName().compareToIgnoreCase(appModel1.getAppName());
          }
        });
  }

  public void uninstallApp(String packageName) {
    Intent intent = new Intent(Intent.ACTION_DELETE);
    intent.setData(Uri.parse("package:" + packageName));
    
    // Verify that there is an activity to handle the intent
    if (intent.resolveActivity(context.getPackageManager()) != null) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle exception gracefully, for example, log it
            Log.e("ACTIVITY::", "Activity not found to handle uninstall intent", e);
        }
    } else {
        // Handle case where no activity can handle the uninstall intent
        Log.e("ACTIVITY::", "No activity found to handle uninstall intent");
    }
}
}
