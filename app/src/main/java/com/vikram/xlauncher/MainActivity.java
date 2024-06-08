package com.vikram.xlauncher;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.search.SearchBar;
import com.vikram.xlauncher.adapters.HomepageAdapter;
import com.vikram.xlauncher.adapters.RecyclerViewAdapter;
import com.vikram.xlauncher.models.AppModel;
import com.vikram.xlauncher.utils.PackageUtils;
import com.vikram.xlauncher.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
  private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
  private int topMargin = 0;
  private List<AppModel> appList;
  private PackageUtils packageUtils;
  private int GRID_COL = 4, GRID_ROW = 6;
  private RecyclerView rvHomepage;
  private HomepageAdapter homepageAdapter;

  @Override
  protected void onCreate(@NotNull Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Make the activity extend into the status bar area
    Window window = getWindow();
    window
        .getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

    List<String> homepageList = new ArrayList<String>();
    homepageList.add("First page");
    homepageList.add("Second Page");
	homepageList.add("Third Page");

    Utils utils = new Utils(this);
    packageUtils = new PackageUtils(this);
    LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
    SearchBar appDrawerSearchBar = findViewById(R.id.app_drawer_searchbar);
    RecyclerView recyclerView = findViewById(R.id.app_drawer_all_apps);
    rvHomepage = findViewById(R.id.homepage_container);
    homepageAdapter = new HomepageAdapter(this, homepageList);

    rvHomepage.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvHomepage.setAdapter(homepageAdapter);
	// set listeners to expand the views to normal dimnesions
	

    int horizontalMarginPx = getResources().getDimensionPixelSize(R.dimen.item_horizontal_margin);
    recyclerView.addItemDecoration(new CenterCardItemDecoration(horizontalMarginPx));

    ItemTouchHelper.Callback callback = new ItemMoveCallback(homepageAdapter);
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(rvHomepage);

    // Attach the PagerSnapHelper to make the RecyclerView behave like a ViewPager
    PagerSnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(rvHomepage);

    RecyclerViewAdapter recyclerViewAdapter =
        new RecyclerViewAdapter(this, packageUtils.getAppList());
    recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    recyclerView.setAdapter(recyclerViewAdapter);

    float[] cornersRadiusPx = {20, 20, 20, 20};
    utils.setViewShape(this, bottomSheet, Color.WHITE, cornersRadiusPx);

    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    bottomSheetBehavior.setPeekHeight(200);

    // Set the height of the bottom sheet to match the parent
    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    bottomSheet.setLayoutParams(layoutParams);

    // Configure the BottomSheetBehavior
    bottomSheetBehavior.setHideable(false); // Prevent it from being hidden
    bottomSheetBehavior.setSkipCollapsed(true); // Skip the collapsed state
    bottomSheetBehavior.setState(
        BottomSheetBehavior.STATE_COLLAPSED); // Start in the collapsed state

    bottomSheetBehavior.addBottomSheetCallback(
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NotNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
              bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
          }

          @Override
          public void onSlide(@NotNull View bottomSheet, float slideOffset) {
            int state = bottomSheetBehavior.getState();

            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                    appDrawerSearchBar.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
            ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) appDrawerSearchBar.getLayoutParams();

            params.topMargin = ((1500 - bottomSheet.getTop()) / 20) + 20;
            Log.w("TOP MARGIN:::", String.valueOf(bottomSheet.getTop()));

            appDrawerSearchBar.setLayoutParams(params);
          }
        });

    // Make the bottom sheet extend under the status bar
    ViewCompat.setOnApplyWindowInsetsListener(
        bottomSheet,
        (v, insets) -> {
          bottomSheet.setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
          return insets.consumeSystemWindowInsets();
        });
  }

  public int increment(int variable, int by) {
    return variable + by;
  }

  public int decrement(int variable, int by) {
    return variable - by;
  }

  public LayoutInflater inflater() {
    return getLayoutInflater().from(this);
  }
}
