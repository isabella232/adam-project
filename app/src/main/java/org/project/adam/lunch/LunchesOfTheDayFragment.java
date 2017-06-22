package org.project.adam.lunch;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Lunch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bastien on 22/06/2017.
 */
@EFragment(R.layout.fragment_lunches_of_the_day)
public class LunchesOfTheDayFragment extends BaseFragment {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEEE dd MMMM");

    @ViewById(R.id.date)
    TextView date;

    @ViewById(R.id.hours_of_day)
    SeekBar hoursOfDay;

    @ViewById(R.id.lunch_detail)
    ViewPager lunchDetailViewPager;

    private LunchListViewModel lunchListViewModel;
    private LunchDetailAdapter lunchDetailAdapter;

    @AfterViews
    void init() {
        lunchDetailAdapter = new LunchDetailAdapter();
        lunchDetailViewPager.setAdapter(lunchDetailAdapter);

        lunchListViewModel = ViewModelProviders.of(this).get(LunchListViewModel.class);
        // TODO: read diet id from preferences
        lunchListViewModel.findFromDiet(1)
            .observe(this, new Observer<List<Lunch>>() {
                @Override
                public void onChanged(@Nullable List<Lunch> lunches) {
                    lunchDetailAdapter.update(lunches);
                }
            });
    }


    @Override
    public void onResume() {
        super.onResume();
        displayCurrentTime();
    }

    private void displayCurrentTime() {
        date.setText(DATE_FORMATTER.format(new Date()));
    }

    private class LunchDetailAdapter extends FragmentPagerAdapter {

        List<LunchDetailFragment> mealFragments = new ArrayList<>();

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
            mealFragments.clear();
            for (Lunch lunch : lunches) {
                LunchDetailFragment fragment = LunchDetailFragment_.builder().build();
                fragment.bind(lunch);
                mealFragments.add(fragment);
            }
            notifyDataSetChanged();
        }
    }
}
