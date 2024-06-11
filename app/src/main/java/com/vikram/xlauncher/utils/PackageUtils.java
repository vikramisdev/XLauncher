package com.vikram.xlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import com.vikram.xlauncher.models.AppModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PackageUtils {
  private PackageManager packageManager;
  private Context context;
  private List<AppModel> appList = new ArrayList<>();

  public PackageUtils(Context c) {
    this.context = c;
  }

  public List<AppModel> getAppList() {
    packageManager = context.getPackageManager();

    // Intent to filter launcher activities
    Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
    launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    // Get all activities that can handle the launcher intent
    List<ResolveInfo> launcherActivities = packageManager.queryIntentActivities(launcherIntent, 0);

    // Retrieve installed packages
    List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

    for (PackageInfo packageInfo : packages) {
      ApplicationInfo appInfo = packageInfo.applicationInfo;

      // Check if the app has a launcher activity
      boolean hasLauncherActivity = hasLauncherActivity(appInfo.packageName, launcherActivities);

      if (hasLauncherActivity) {
        String appName = appInfo.loadLabel(packageManager).toString();
        Drawable appIcon = appInfo.loadIcon(packageManager);
        double version = packageInfo.versionCode;
        String packageName = appInfo.packageName;
        boolean canUninstall = canUninstall(appInfo);

        AppModel appModel = new AppModel(appName, appIcon, version, packageName, canUninstall);
        appList.add(appModel);
      }
    }

    // Sort appList alphabetically
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

  // Method to check if an app has a launcher activity
  private boolean hasLauncherActivity(String packageName, List<ResolveInfo> launcherActivities) {
    for (ResolveInfo resolveInfo : launcherActivities) {
      if (resolveInfo.activityInfo.packageName.equals(packageName)) {
        return true;
      }
    }
    return false;
  }

  public boolean canUninstall(ApplicationInfo appInfo) {
    return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
  }

  // Method to sort appList in reverse order
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

  public void launchApp(String packageName) {
    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
    if (launchIntent != null) {
      context.startActivity(launchIntent);
    }
  }
}
