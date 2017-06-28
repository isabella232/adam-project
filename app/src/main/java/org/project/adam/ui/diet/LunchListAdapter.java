package org.project.adam.ui.diet;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.project.adam.persistence.Meal;
import org.project.adam.ui.util.RecyclerViewAdapterBase;
import org.project.adam.ui.util.ViewWrapper;

@EBean
class LunchListAdapter extends RecyclerViewAdapterBase<Meal, MealItemView> {

    @RootContext
    Context context;

    @Override
    protected MealItemView onCreateItemView(ViewGroup parent, int viewType) {
        final MealItemView item = MealItemView_.build(context);
        return item;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<MealItemView> holder, int position) {
        final Meal meal = items.get(position);
        final MealItemView view = holder.getView();
        view.bind(meal);
    }
}