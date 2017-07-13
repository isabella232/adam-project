package org.project.adam.ui.dashboard.meal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PageSelected;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.project.adam.BaseFragment;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Meal;
import org.project.adam.ui.IndicatorCircleView_;
import org.project.adam.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_meals_of_the_day)
public class MealsOfTheDayFragment extends BaseFragment {

    @ViewById(R.id.date)
    protected TextView date;

    @ViewById(R.id.selected_meal_time_of_day)
    protected TextView selectedMealTimeOfDay;

    @ViewById(R.id.meal_detail)
    protected ViewPager mealDetailViewPager;

    @ViewById(R.id.circleIndicator)
    protected IndicatorCircleView_ circleView;

    @Pref
    protected Preferences_ preferences;

    @Bean
    protected DateFormatter dateFormatter;

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
                    displayNextMealOfDay();
                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayCurrentTime();
        displayNextMealOfDay();
    }

    private void displayNextMealOfDay() {
        int indexOfNextMeal = mealDetailAdapter.getNextMealIndex();
        if(indexOfNextMeal >= 0){
            circleView.setNextMealPosition(indexOfNextMeal);
            mealDetailViewPager.setCurrentItem(indexOfNextMeal);
        }
    }

    private void displayCurrentTime() {
        date.setText(dateFormatter.longFormatDay(LocalDate.now()));
    }

    @PageSelected(R.id.meal_detail)
    void displayCurrentMealTime() {
        selectedMealTimeOfDay.setText(dateFormatter.formatMinutesOfDay(
            mealDetailAdapter.getCurrentMeal().getTimeOfDay()));
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

        Meal getCurrentMeal() {
            return meals.get(mealDetailViewPager.getCurrentItem());
        }

        public int getNextMealIndex() {
            if (meals == null || meals.isEmpty()) {
                return -1;
            }
            LocalTime currentTime= LocalTime.now();
            int currentTimeInMiliseconds = currentTime.getMillisOfDay();
            int nextMealIndex = meals.size() - 1;

            for (int i = meals.size() - 1; i >= 0; --i) {
                if ((meals.get(i).getTimeOfDay().getMillisOfDay() + 15*60*1000) > currentTimeInMiliseconds) {
                    nextMealIndex = i;
                }
            }
            return nextMealIndex;
        }
    }
}
