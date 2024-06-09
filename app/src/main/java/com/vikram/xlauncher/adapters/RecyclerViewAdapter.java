package com.vikram.xlauncher.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import com.vikram.xlauncher.R;
import com.vikram.xlauncher.models.AppModel;
import com.vikram.xlauncher.utils.Utils;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private List<AppModel> appList;
  private Context context;
  private Utils utils;

  public RecyclerViewAdapter(Context context, List<AppModel> appList) {
    this.context = context;
    this.appList = appList;
    this.utils = new Utils(context);
  }

  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
    AppModel app = appList.get(position);
    holder.appName.setText(app.getAppName());
    holder.appIcon.setImageDrawable(app.getAppImage());

    holder.itemView.setOnClickListener(
        v -> {
          // Intent to launch the app
          AnimatorSet animatorSet =
              (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.scale_down);
          animatorSet.setTarget(holder.appIcon);
          animatorSet.start();

          // Delay to allow animation to complete
          v.postDelayed(
              new Runnable() {
                @Override
                public void run() {
                  // Revert to original size
                  AnimatorSet animatorSetReverse =
                      (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.scale_up);
                  animatorSetReverse.setTarget(holder.appIcon);
                  animatorSetReverse.start();
                }
              },
              100);

          Intent launchIntent =
              context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
          if (launchIntent != null) {
            context.startActivity(launchIntent);
          } else {
            // Handle the case when the app is not installed
            Toast.makeText(context, "App not installed", Toast.LENGTH_SHORT).show();
          }
        });

    holder.itemView.setOnLongClickListener(
        new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            showPopupMenu(v.getContext(), v, app);
            return true;
          }
        });
  }

  @Override
  public int getItemCount() {
    return appList.size();
  }

  private void showPopupMenu(Context context, View anchorView, AppModel app) {
    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenuStyle);
    PopupMenu popupMenu = new PopupMenu(wrapper, anchorView);
    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

    // Optionally, you can dynamically set menu item titles or icons based on app details
    // popupMenu.getMenu().findItem(R.id.menu_item_id).setTitle(app.getAppName());
	if(!app.canUninstall()) {
		popupMenu.getMenu().findItem(R.id.menu_app_uninstall).setTitle("Disable");
	}

    popupMenu.setOnMenuItemClickListener(
        new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
            // Handle menu item click here
            switch (item.getItemId()) {
              case R.id.menu_app_info:
                launchAppInfo(app.getPackageName());
                break;
              case R.id.menu_app_uninstall:
                // Perform action 2
                break;
                // Add more cases as needed
            }
            return true;
          }
        });

    popupMenu.show();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView appIcon;
    public TextView appName;

    public ViewHolder(View view) {
      super(view);
      appIcon = view.findViewById(R.id.app_icon);
      appName = view.findViewById(R.id.app_name);
    }
  }

  public void launchAppInfo(String packageName) {
    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + packageName));

    // Check if there's an activity that can handle this intent
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(intent);
    } else {
      // Handle if no activity can handle the intent
      Toast.makeText(context, "Unable to open app info for " + packageName, Toast.LENGTH_SHORT).show();
    }
  }
}
