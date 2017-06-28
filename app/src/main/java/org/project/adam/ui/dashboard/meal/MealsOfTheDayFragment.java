package org.project.adam.ui.dashboard.meal;

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
import org.project.adam.persistence.Meal;
import org.project.adam.ui.IndicatorCircleView_;
import org.project.adam.util.DateFormatters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_meals_of_the_day)
public class MealsOfTheDayFragment extends BaseFragment {

    @ViewById(R.id.date)
    TextView date;

    @ViewById(R.id.selected_meal_time_of_day)
    TextView selectedMealTimeOfDay;

    @ViewById(R.id.meal_detail)
    ViewPager mealDetailViewPager;

    @ViewById(R.id.circleIndicator)
    IndicatorCircleView_ circleView;

    @Pref
    Preferences_ preferences;

    private MealListViewModel mealListViewModel;

    private MealDetailAdapter mealDetailAdapter;



    @AfterViews
    void init() {
        mealDetailAdapter = new MealDetailAdapter();
        mealDetailViewPager.setAdapter(mealDetailAdapter);
        circleView.setViewPager(mealDetailViewPager);

        mealListViewModel = ViewModelProviders.of(this).get(MealListViewModel.class);
        mealListViewModel.findFromDiet(preferences.currentDietId().get())
            .observe(this, new Observer<List<Meal>>() {
                @Override
                public void onChanged(@Nullable List<Meal> meals) {
                    mealDetailAdapter.update(meals);
                    circleView.setMeals(meals);
                    setNextMeal(meals);
                }
            });
    }

    void setNextMeal(List<Meal> meals) {
        if (meals.isEmpty()) {
            return;
        }
        Calendar currentTime = Calendar.getInstance();
        int currentTimeInMinutes = currentTime.get(Calendar.MINUTE) + currentTime.get(Calendar.HOUR_OF_DAY) * 60;
        int nextMealPage = meals.size() - 1;

        for (int i = meals.size() - 1; i >= 0; --i) {
            if (meals.get(i).getTimeOfDay() + 15 > currentTimeInMinutes) {
                nextMealPage = i;
            }
        }

        circleView.setNextMealPosition(nextMealPage);
        mealDetailViewPager.setCurrentItem(nextMealPage);

    }

    @Override
    public void onResume() {
        super.onResume();
        displayCurrentTime();
    }

    private void displayCurrentTime() {
        date.setText(DateFormatters.formatDay(new Date()));
    }

    @PageSelected(R.id.meal_detail)
    void displayCurrentMealTime() {
        selectedMealTimeOfDay.setText(DateFormatters.formatMinutesOfDay(mealDetailAdapter.getCurrentMeal().getTimeOfDay()));
    }

    @Click(R.id.next_meal)
    void onDisplayNextMeal() {
        mealDetailViewPager.setCurrentItem(mealDetailViewPager.getCurrentItem() + 1);
    }

    @Click(R.id.previous_meal)
    void onDisplayPreviousMeal() {
        mealDetailViewPager.setCurrentItem(mealDetailViewPager.getCurrentItem() - 1);
    }

    private class MealDetailAdapter extends FragmentPagerAdapter {

        List<MealDetailFragment> mealFragments = new ArrayList<>();
        private List<Meal> meals;

        public MealDetailAdapter() {
            super(MealsOfTheDayFragment.this.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            return mealFragments.get(i);
        }

        @Override
        public int getCount() {
            return mealFragments.size();
        }

        public void update(List<Meal> meals) {
            this.meals = meals;
            mealFragments.clear();
            for (Meal meal : meals) {
                MealDetailFragment fragment = MealDetailFragment_.builder().build();
                fragment.bind(meal);
                mealFragments.add(fragment);
            }
            notifyDataSetChanged();
            if (!meals.isEmpty()) {
                displayCurrentMealTime();
            }
        }

        public Meal getCurrentMeal() {
            return meals.get(mealDetailViewPager.getCurrentItem());
        }
    }
}
