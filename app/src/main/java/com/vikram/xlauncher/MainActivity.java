package com.vikram.xlauncher;

import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.vikram.xlauncher.adapters.HomepageAdapter;
import com.vikram.xlauncher.adapters.RecyclerViewAdapter;
import com.vikram.xlauncher.adapters.SearchResultsAdapter;
import com.vikram.xlauncher.layouts.XRelativeLayout;
import com.vikram.xlauncher.layouts.XSearchView;
import com.vikram.xlauncher.models.AppModel;
import com.vikram.xlauncher.utils.PackageUtils;
import com.vikram.xlauncher.utils.PreferenceManager;
import com.vikram.xlauncher.utils.Utils;
import com.vikram.xlauncher.utils.WallpaperUtils;
import com.vikram.xlauncher.views.XTextView;
import com.vikram.xlauncher.watchdogs.WallpaperObserver;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.*;

public class MainActivity extends AppCompatActivity {

  private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
  private int topMargin = 0;
  private List<AppModel> appList;
  private PackageUtils packageUtils;
  private int GRID_COL = 4, GRID_ROW = 6;
  private RecyclerView rvHomepage;
  private HomepageAdapter homepageAdapter;
  private ImageView homepageWallpaperBtn, homepageSettingsBtn;
  private static final int PICK_IMAGE = 50;
  public CoordinatorLayout activityMain;
  private PreferenceManager preferenceManager;
  private Utils utils;
  private WallpaperUtils wallpaperUtils;
  private List<String> searchList;
  private SearchResultsAdapter searchResultsAdapter;
  private XSearchView appDrawerSearchBar;
  private XTextView searchResults;

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
    // homepageList.add("Third Page");*/

    utils = new Utils(this);
    wallpaperUtils = new WallpaperUtils(this);
    packageUtils = new PackageUtils(this);
    preferenceManager = new PreferenceManager(this);
    XRelativeLayout bottomSheet = findViewById(R.id.bottom_sheet);
    appDrawerSearchBar = findViewById(R.id.app_drawer_searchbar);
    RecyclerView recyclerView = findViewById(R.id.app_drawer_all_apps);
    rvHomepage = findViewById(R.id.homepage_container);
    homepageAdapter = new HomepageAdapter(this, this, homepageList);
    homepageWallpaperBtn = findViewById(R.id.homepage_wallapaper_button);
    homepageSettingsBtn = findViewById(R.id.homepage_settings_button);
    activityMain = findViewById(R.id.activity_main);
	
	searchResults = findViewById(R.id.search_results);

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
    float[] searchBarCornerRadius = {40, 40, 40, 40};
    utils.setViewShape(this, bottomSheet, Color.WHITE, cornersRadiusPx);
    utils.setViewShape(this, appDrawerSearchBar, Color.BLACK, searchBarCornerRadius);
	utils.setViewShape(this, searchResults, Color.WHITE, cornersRadiusPx);

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

            int topMargin = ((1500 - bottomSheet.getTop()) / 20) + 20;

            appDrawerSearchBar.setMargins(topMargin, 15, 15, 15);
			
			searchResults.setMargins(topMargin, 15, 15, 15);
          }
        });

    appDrawerSearchBar.setOnQueryTextListener(
        new XSearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            // Handle search query submission (e.g., filter data)

            return true;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            // Handle text changes in search input (e.g., live filtering)

            return true;
          }
        });

    // Make the bottom sheet extend under the status bar
    ViewCompat.setOnApplyWindowInsetsListener(
        bottomSheet,
        (v, insets) -> {
          bottomSheet.setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
          return insets.consumeSystemWindowInsets();
        });

    if (homepageWallpaperBtn != null) {
      homepageWallpaperBtn.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openMediaPicker();
            }
          });
    }

    appDrawerSearchBar.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            appDrawerSearchBar.setIconified(false);
          }
        });

    wallpaperUtils.setLauncherWallpaper(activityMain);

    registerObserver(
        Settings.System.getUriFor(Settings.System.WALLPAPER_ACTIVITY),
        new WallpaperObserver(this, activityMain, new Handler()));
  }

  @Override
  public void onBackPressed() {
    // Check if the SearchView is expanded and collapse it if needed
    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      appDrawerSearchBar.setIconified(true);
    } else {
      super.onBackPressed();
    }
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

  private void openMediaPicker() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    startActivityForResult(intent, PICK_IMAGE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
      Uri selectedImage = data.getData();
      // Display the selected image in the ImageView

    }
  }

  public void setDeviceWallpaper(Drawable imageDrawable) {
    activityMain.setBackgroundDrawable(imageDrawable);
  }

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void openDialogToSetWallpaper(Uri uri) {}

  public void registerObserver(Uri uri, ContentObserver observer) {
    getContentResolver().registerContentObserver(uri, false, observer);
  }
}
