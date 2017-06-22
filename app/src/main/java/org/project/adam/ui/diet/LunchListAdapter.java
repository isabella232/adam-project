package org.project.adam.ui.diet;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.project.adam.persistence.Lunch;
import org.project.adam.util.ui.RecyclerViewAdapterBase;
import org.project.adam.util.ui.ViewWrapper;

@EBean
class LunchListAdapter extends RecyclerViewAdapterBase<Lunch, LunchItemView> {

    @RootContext
    Context context;

    @Override
    protected LunchItemView onCreateItemView(ViewGroup parent, int viewType) {
        final LunchItemView item = LunchItemView_.build(context);
        return item;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<LunchItemView> holder, int position) {
        final Lunch lunch = items.get(position);
        final LunchItemView view = holder.getView();
        view.bind(lunch);
    }
}