package org.project.adam;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.persistence.Diet;
import org.project.adam.ui.dashboard.DashboardFragment_;
import org.project.adam.ui.data.DataFragment_;
import org.project.adam.ui.diet.DietListFragment_;
import org.project.adam.ui.preferences.PrefActivity_;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends BaseActivity {

    static final int DEFAULT_DIET_ID = -1;

    @ViewById(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Pref
    protected Preferences_ prefs;

    @AfterViews
    void setUpTabs() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.tab_dashboard:
                            showDashBoard();
                            break;

                        case R.id.tab_data:
                            showData();
                            break;

                        case R.id.tab_diet:
                            showDiets();
                            break;
                    }
                    return true;
                }
            });
        showWelcomeSection();
    }

    private void showWelcomeSection() {
        final Integer currentDietId = prefs.currentDietId().get();
        if(currentDietId == DEFAULT_DIET_ID){
            showDiets();
        } else {
            AppDatabase.getDatabase(this).dietDao().find(currentDietId)
                .observe(this, new Observer<Diet>() {
                @Override
                public void onChanged(@Nullable Diet diet) {
                    if (diet == null){
                        showDiets();
                    } else {
                        showDashBoard();
                    }
                }
            });
        }

    }

    private boolean currentDietIsSelected() {
        Integer currentDietId = prefs.currentDietId().get();
        return currentDietId != DEFAULT_DIET_ID
            && AppDatabase.getDatabase(this).dietDao().find(currentDietId).getValue() != null;
    }

    private void showDashBoard() {
        showFragment(DashboardFragment_.builder().build());
    }

    private void showData() {
        showFragment(DataFragment_.builder().build());
    }

    private void showDiets() {
        showFragment(DietListFragment_.builder().build());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow();
    }

    @OptionsItem(R.id.action_settings)
    public void menuSettings() {
        PrefActivity_.intent(this).start();
    }

}
