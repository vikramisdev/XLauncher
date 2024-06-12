package com.vikram.xlauncher;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.vikram.xlauncher.adapters.AppDrawerAppsAdapter;
import com.vikram.xlauncher.adapters.AppDrawerSearchbarResultsAdapter;
import com.vikram.xlauncher.adapters.HomepageAdapter;
import com.vikram.xlauncher.layouts.XRelativeLayout;
import com.vikram.xlauncher.layouts.XSearchView;
import com.vikram.xlauncher.models.AppModel;
import com.vikram.xlauncher.utils.PackageUtils;
import com.vikram.xlauncher.utils.PreferenceManager;
import com.vikram.xlauncher.utils.Utils;
import com.vikram.xlauncher.utils.WallpaperUtils;
import com.vikram.xlauncher.views.XRecyclerView;
import com.vikram.xlauncher.views.XTextView;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

  public BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
  private List<AppModel> appList, searchbarResultsApps;
  private PackageUtils packageUtils;
  public XRecyclerView rvHomepagePages, rvAppDrawerApps, rvAppDrawerSearchbarResults;
  private HomepageAdapter homepageAdapter;
  private ImageView imHomepageWallpaperButton, imHomepageSettingsButton;
  private static final int PICK_IMAGE = 53;
  public CoordinatorLayout clHomepageContainer;
  private PreferenceManager preferenceManager;
  private Utils utils;
  private WallpaperUtils wallpaperUtils;
  private XSearchView svAppDrawerSearchBar;
  private XTextView searchResults;
  private XRelativeLayout rlAppDrawerContainer;
  private List<String> list10;

  @Override
  protected void onCreate(@NotNull Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    // Initialize utils

    utils = new Utils(this);
    wallpaperUtils = new WallpaperUtils(this);
    packageUtils = new PackageUtils(this);
    preferenceManager = new PreferenceManager(this);

    appList = packageUtils.getAppList();

    searchbarResultsApps = new ArrayList<AppModel>();

    // make the screen to fullscreen so views go under status bar
    utils.setDeviceFullScreen();

    // intialize views
    rlAppDrawerContainer = findViewById(R.id.app_drawer_container);
    svAppDrawerSearchBar = findViewById(R.id.app_drawer_searchbar);
    rvAppDrawerApps = findViewById(R.id.app_drawer_apps);
    rvHomepagePages = findViewById(R.id.homepage_pages);
    imHomepageWallpaperButton = findViewById(R.id.homepage_wallapaper_button);
    imHomepageSettingsButton = findViewById(R.id.homepage_settings_button);
    clHomepageContainer = findViewById(R.id.homepage_container);
    rvAppDrawerSearchbarResults = findViewById(R.id.app_drawer_searchbar_results);

    // call component methods in order
    setupHomepagePages();
    setupAppDrawer();
    setupHomepageWallpaperButton();

    // set launcher to main_layout
    wallpaperUtils.setLauncherWallpaper(clHomepageContainer);
  }

  @Override
  public void onBackPressed() {
    showToast(String.valueOf(bottomSheetBehavior.getState()));
    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      rvAppDrawerSearchbarResults.setVisibility(View.INVISIBLE);
      svAppDrawerSearchBar.setIconified(true);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
      Uri selectedImage = data.getData();
      // Handle the selected image (e.g., display it or set it as wallpaper)
    }
  }

  // app component methods
  public void setupHomepagePages() {
    List<String> homepagePagesList = new ArrayList<>();
    homepagePagesList.add("First page");
    homepagePagesList.add("Second Page");
    homepageAdapter = new HomepageAdapter(this, this, homepagePagesList);
    rvHomepagePages.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvHomepagePages.setAdapter(homepageAdapter);

    ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemMoveCallback(homepageAdapter));
    touchHelper.attachToRecyclerView(rvHomepagePages);
    PagerSnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(rvHomepagePages);
  }

  public void setupAppDrawer() {
    float[] cornersRadiusPx = {20, 20, 20, 20};
    float[] searchBarCornerRadius = {40, 40, 40, 40};

    if (rvAppDrawerSearchbarResults != null) {

      rvAppDrawerSearchbarResults.setLayoutManager(
          new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

      rvAppDrawerSearchbarResults.setPadding(10, 130, 10, 10);
    }

    rlAppDrawerContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

    utils.setViewShape(this, rlAppDrawerContainer, Color.WHITE, cornersRadiusPx);
    utils.setViewShape(this, svAppDrawerSearchBar, Color.BLACK, searchBarCornerRadius);
    utils.setViewShape(
        this, rvAppDrawerSearchbarResults, Color.rgb(245, 245, 245), cornersRadiusPx);

    svAppDrawerSearchBar.setOnQueryTextListener(
        new XSearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            if (!searchbarResultsApps.isEmpty()) {
              packageUtils.launchApp(searchbarResultsApps.get(0).getPackageName());
            }
            return true;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            // clear each time so that no previous views are there
            searchbarResultsApps.clear();

            for (int i = 0; i < appList.size(); i++) {
              if (appList.get(i).getAppName().toLowerCase().startsWith(newText.toLowerCase())) {
                searchbarResultsApps.add(appList.get(i));
              }
            }

            rvAppDrawerSearchbarResults.setAdapter(
                new AppDrawerSearchbarResultsAdapter(MainActivity.this, searchbarResultsApps));
            return true;
          }
        });

    svAppDrawerSearchBar.setOnQueryTextFocusChangeListener(
        new SearchView.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
              rvAppDrawerSearchbarResults.setVisibility(View.VISIBLE);
              searchbarResultsApps.clear();
              svAppDrawerSearchBar.setIconified(false);

              rvAppDrawerSearchbarResults.setAdapter(
                  new AppDrawerSearchbarResultsAdapter(MainActivity.this, searchbarResultsApps));
            } else {
              rvAppDrawerSearchbarResults.setVisibility(View.INVISIBLE);
              svAppDrawerSearchBar.setIconified(true);
            }
          }
        });

    svAppDrawerSearchBar.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            showToast(String.valueOf(bottomSheetBehavior.getState()));
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            svAppDrawerSearchBar.setIconified(false);
            rvAppDrawerSearchbarResults.setVisibility(View.VISIBLE);
          }
        });

    bottomSheetBehavior = BottomSheetBehavior.from(rlAppDrawerContainer);
    bottomSheetBehavior.setPeekHeight(200);

    bottomSheetBehavior.setHideable(false);
    bottomSheetBehavior.setSkipCollapsed(true);
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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
            int topMargin = ((1500 - bottomSheet.getTop()) / 20) + 20;
            int sideMargin = ((1500 - bottomSheet.getTop()) / 40) + 20;
            svAppDrawerSearchBar.setMargins(topMargin, sideMargin, 10, sideMargin);
            rvAppDrawerSearchbarResults.setMargins(topMargin - 20, 15, 15, 15);
          }
        });

    // Make the bottom sheet extend under the status bar
    ViewCompat.setOnApplyWindowInsetsListener(
        rlAppDrawerContainer,
        (v, insets) -> {
          rlAppDrawerContainer.setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
          return insets.consumeSystemWindowInsets();
        });

    ItemTouchHelper.Callback callback =
        new ItemTouchHelper.Callback() {
          @Override
          public int getMovementFlags(RecyclerView arg0, ViewHolder arg1) {
            int dragFlags =
                ItemTouchHelper.UP
                    | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT
                    | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, 0);
          }

          @Override
          public boolean onMove(
              @NotNull RecyclerView recyclerView,
              @NotNull RecyclerView.ViewHolder viewHolder,
              @NotNull RecyclerView.ViewHolder target) {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
          }

          @Override
          public void onSwiped(ViewHolder arg0, int arg1) {}
        };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(rvAppDrawerApps);

    rvAppDrawerApps.setLayoutManager(new GridLayoutManager(this, 4));
    rvAppDrawerApps.setAdapter(new AppDrawerAppsAdapter(this, appList));
  }

  public void setupHomepageWallpaperButton() {
    if (imHomepageWallpaperButton != null) {
      imHomepageWallpaperButton.setOnClickListener(view -> utils.openMediaPicker());
    }
  }

  public void setDeviceWallpaper(Drawable imageDrawable) {
    clHomepageContainer.setBackground(imageDrawable);
  }

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void registerObserver(Uri uri, android.database.ContentObserver observer) {
    getContentResolver().registerContentObserver(uri, false, observer);
  }
}
