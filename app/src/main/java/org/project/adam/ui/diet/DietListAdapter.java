package org.project.adam.ui.diet;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.project.adam.persistence.Diet;
import org.project.adam.util.ui.RecyclerViewAdapterBase;
import org.project.adam.util.ui.ViewWrapper;

@EBean
class DietListAdapter extends RecyclerViewAdapterBase<Diet, DietItemView> {

    @RootContext
    Context context;

    @Override
    protected DietItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DietItemView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<DietItemView> holder, int position) {
        final Diet diet = items.get(position);
        holder.getView().bind(diet);
    }

}