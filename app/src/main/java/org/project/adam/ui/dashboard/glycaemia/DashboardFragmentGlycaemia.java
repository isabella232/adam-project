package org.project.adam.ui.dashboard.glycaemia;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.util.ui.RecyclerViewAdapterBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_dashboard_glycaemia)
public class DashboardFragmentGlycaemia extends BaseFragment {

    @ViewById(R.id.glycaemia_items)
    RecyclerView glycaemiaItemsView;
    private GlycaemiaViewModel glycaemiaViewModel;
    private GlycaemiaListAdapter glycaemiaListAdapter;

    @AfterViews
    void init() {
        glycaemiaListAdapter = new GlycaemiaListAdapter(getActivity());
        glycaemiaItemsView.setAdapter(glycaemiaListAdapter);
        glycaemiaViewModel = ViewModelProviders.of(this).get(GlycaemiaViewModel.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date todayBegin = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrowBegin = calendar.getTime();
        glycaemiaViewModel.findGlycaemiaBetween(todayBegin, tomorrowBegin)
            .observe(this, new Observer<List<Glycaemia>>() {
                @Override
                public void onChanged(@Nullable List<Glycaemia> glycaemias) {
                    List<Glycaemia> glycaemiaList =new ArrayList<>();
                    Glycaemia fakeGlycaemiaForAddView = new Glycaemia();
                    glycaemiaList.add(fakeGlycaemiaForAddView);
                    glycaemiaList.addAll(glycaemias);
                    glycaemiaListAdapter.update(glycaemiaList);
                }
            });
    }

    private class GlycaemiaListAdapter extends RecyclerViewAdapterBase {

        private static final int ADD_TYPE = 1;
        private static final int GLYCAEMIA_TYPE = 2;
        private FragmentActivity activity;

        public GlycaemiaListAdapter(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        protected View onCreateItemView(ViewGroup parent, int viewType) {
            if (viewType == ADD_TYPE) {
                GlycaemiaAddView addView = GlycaemiaAddView_.build(activity);
                addView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlycaemiaActivity_.intent(DashboardFragmentGlycaemia.this).start();
                    }
                });
                return addView;
            }
            return GlycaemiaItemView_.build(activity);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (getItemViewType(i) == GLYCAEMIA_TYPE) {
                ((GlycaemiaItemView) viewHolder.itemView).bind((Glycaemia) items.get(i));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return ADD_TYPE;
            }
            return GLYCAEMIA_TYPE;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
