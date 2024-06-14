package com.vikram.xlauncher.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import com.vikram.xlauncher.MainActivity;
import com.vikram.xlauncher.R;
import com.vikram.xlauncher.adapters.AppDrawerAppsAdapter.ViewHolder;
import com.vikram.xlauncher.models.AppModel;
import com.vikram.xlauncher.utils.PackageUtils;
import com.vikram.xlauncher.utils.Utils;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AppDrawerAppsAdapter extends RecyclerView.Adapter<AppDrawerAppsAdapter.ViewHolder> {

  private List<AppModel> appList;
  private Context context;
  private Utils utils;
  private MainActivity activity;
  private PackageUtils packageUtils;
  private PopupWindow popupWindow;

  public AppDrawerAppsAdapter(MainActivity activity, List<AppModel> appList) {
    this.context = (Context) activity;
    this.appList = appList;
    this.utils = new Utils((Context) activity);
    this.activity = activity;
    this.packageUtils = new PackageUtils(context);
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
    // Log.d("POSITION:: ", app.getAppName());

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
            // showPopupMenu(v.getContext(), v, app);
            showOptionsPopup(v.findViewById(R.id.app_icon));

            return true;
          }
        });
  }

  private void showOptionsPopup(View anchorView) {
    // Retrieve the LayoutInflater service
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Inflate the custom layout/view
    View popupView = inflater.inflate(R.layout.popup_window, null);
    ImageView arrowDown = popupView.findViewById(R.id.app_menu_triangle);

    // Create the popup window
    final PopupWindow popupWindow =
        new PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true);

    // Disable clipping for the popup window
    popupWindow.setClippingEnabled(false);

    // Calculate the position to show the popup above the anchor view
    int[] anchorViewLocation = new int[2];
    anchorView.getLocationOnScreen(anchorViewLocation);

    int anchorViewX = anchorViewLocation[0];
    int anchorViewY = anchorViewLocation[1];

    // Measure the popup window to get its dimensions
    popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    int popupWindowWidth = popupView.getMeasuredWidth();
    int popupWindowHeight = popupView.getMeasuredHeight();

    // Determine column position and adjust popup position
    int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    int columnWidth = screenWidth / 4; // Assuming 4 columns

    if (anchorViewX < columnWidth * 2) {
      // For left two columns, show popup to the right of the anchor view
      popupWindow.showAtLocation(
          anchorView, Gravity.NO_GRAVITY, anchorViewX, anchorViewY - popupWindowHeight);

      // Calculate the X position of the triangle relative to the popup window
      int triangleX =
          anchorViewX + (anchorView.getWidth() / 2) - (arrowDown.getMeasuredWidth() / 2);

      // Ensure the triangle is within bounds
      if (triangleX < anchorViewX) {
        triangleX = anchorViewX;
      } else if (triangleX + arrowDown.getMeasuredWidth() > anchorViewX + popupWindowWidth) {
        triangleX = anchorViewX + popupWindowWidth - arrowDown.getMeasuredWidth();
      }

      // Set the X position of the triangle
      int relativeTriangleX = triangleX - anchorViewX - 20;
      arrowDown.setX(relativeTriangleX);
    } else {
      // For right two columns, show popup to the left of the anchor view
      popupWindow.showAtLocation(
          anchorView,
          Gravity.NO_GRAVITY,
          anchorViewX - popupWindowWidth + anchorView.getWidth(),
          anchorViewY - popupWindowHeight);

      // Set the X position of the triangle to the rightmost side of the popup window
      arrowDown.setX(popupWindowWidth - arrowDown.getMeasuredWidth() - 10); // rounded cornered menu fix
    }

    // Create enter animation (slide up)
    Animation slideUp =
        new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f);
    slideUp.setDuration(100);
    slideUp.setFillAfter(true);

    // Create exit animation (slide down)
    Animation slideDown =
        new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f);
    slideDown.setDuration(100);
    slideDown.setFillAfter(true);

    // Apply the enter animation when the popup window is shown
    popupView.startAnimation(slideUp);

    // Set the exit animation when the popup window is dismissed
    popupWindow.setOnDismissListener(() -> popupView.startAnimation(slideDown));

    // Optional: Dismiss the popup when touched outside
    popupView.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            popupView.startAnimation(slideDown);
            return true;
          }
          return false;
        });
  }

  @Override
  public int getItemCount() {
    return appList.size();
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

  public View copyAppView(View view) {
    TextView viewText = view.findViewById(R.id.app_name);
    ImageView viewImage = view.findViewById(R.id.app_icon);

    View newView = activity.getLayoutInflater().inflate(R.layout.app_item, null, false);

    ImageView newImageView = newView.findViewById(R.id.app_icon);
    TextView newTextView = newView.findViewById(R.id.app_name);

    newTextView.setText(viewText.getText());
    newImageView.setImageDrawable(viewImage.getDrawable());
    return newView;
  }
}
