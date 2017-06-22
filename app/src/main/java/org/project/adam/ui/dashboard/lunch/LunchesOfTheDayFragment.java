package org.project.adam.ui.dashboard.lunch;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PageSelected;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.BaseFragment;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Lunch;
import org.project.adam.ui.IndicatorCircleView_;
import org.project.adam.util.DateFormatters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_lunches_of_the_day)
public class LunchesOfTheDayFragment extends BaseFragment {

    @ViewById(R.id.date)
    TextView date;

    @ViewById(R.id.selected_lunch_time_of_day)
    TextView selectedLunchTimeOfDay;

    @ViewById(R.id.lunch_detail)
    ViewPager lunchDetailViewPager;

    @ViewById(R.id.circleIndicator)
    IndicatorCircleView_ circleView;

    @Pref
    Preferences_ preferences;

    private LunchListViewModel lunchListViewModel;
    private LunchDetailAdapter lunchDetailAdapter;



    @AfterViews
    void init() {
        lunchDetailAdapter = new LunchDetailAdapter();
        lunchDetailViewPager.setAdapter(lunchDetailAdapter);
        circleView.setViewPager(lunchDetailViewPager);

        lunchListViewModel = ViewModelProviders.of(this).get(LunchListViewModel.class);
        lunchListViewModel.findFromDiet(preferences.currentDietId().get())
            .observe(this, new Observer<List<Lunch>>() {
                @Override
                public void onChanged(@Nullable List<Lunch> lunches) {
                    lunchDetailAdapter.update(lunches);
                    circleView.setMeals(lunches);
                    setNextMeal(lunches);
                }
            });
    }

    void setNextMeal(List<Lunch> lunches) {
        if (lunches.isEmpty()) {
            return;
        }
        Calendar currentTime = Calendar.getInstance();
        int currentTimeInMinutes = currentTime.get(Calendar.MINUTE) + currentTime.get(Calendar.HOUR_OF_DAY) * 60;
        int nextMealPage = lunches.size() - 1;

        for (int i = lunches.size() - 1; i >= 0; --i) {
            if (lunches.get(i).getTimeOfDay() + 15 > currentTimeInMinutes) {
                nextMealPage = i;
            }
        }

        circleView.setNextMealPosition(nextMealPage);
        lunchDetailViewPager.setCurrentItem(nextMealPage);

    }

    @Override
    public void onResume() {
        super.onResume();
        displayCurrentTime();
    }

    private void displayCurrentTime() {
        date.setText(DateFormatters.formatDay(new Date()));
    }

    @PageSelected(R.id.lunch_detail)
    void displayCurrentLunchTime() {
        selectedLunchTimeOfDay.setText(DateFormatters.formatMinutesOfDay(lunchDetailAdapter.getCurrentLunch().getTimeOfDay()));
    }

    @Click(R.id.next_lunch)
    void onDisplayNextLunch() {
        lunchDetailViewPager.setCurrentItem(lunchDetailViewPager.getCurrentItem() + 1);
    }

    @Click(R.id.previous_lunch)
    void onDisplayPreviousLunch() {
        lunchDetailViewPager.setCurrentItem(lunchDetailViewPager.getCurrentItem() - 1);
    }

    private class LunchDetailAdapter extends FragmentPagerAdapter {

        List<LunchDetailFragment> mealFragments = new ArrayList<>();
        private List<Lunch> lunches;

        public LunchDetailAdapter() {
            super(LunchesOfTheDayFragment.this.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            return mealFragments.get(i);
        }

        @Override
        public int getCount() {
            return mealFragments.size();
        }

        public void update(List<Lunch> lunches) {
            this.lunches = lunches;
            mealFragments.clear();
            for (Lunch lunch : lunches) {
                LunchDetailFragment fragment = LunchDetailFragment_.builder().build();
                fragment.bind(lunch);
                mealFragments.add(fragment);
            }
            notifyDataSetChanged();
            if (!lunches.isEmpty()) {
                displayCurrentLunchTime();
            }
        }

        public Lunch getCurrentLunch() {
            return lunches.get(lunchDetailViewPager.getCurrentItem());
        }
    }
}
