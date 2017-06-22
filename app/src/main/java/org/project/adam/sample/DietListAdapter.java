package org.project.adam.sample;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.project.adam.persistence.Diet;
import org.project.adam.util.ui.RecyclerViewAdapterBase;
import org.project.adam.util.ui.ViewWrapper;

@EBean
class DietListAdapter extends RecyclerViewAdapterBase<Diet, ModelView> {

    @RootContext
    Context context;

    @Override
    protected ModelView onCreateItemView(ViewGroup parent, int viewType) {
        return ModelView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ModelView> holder, int position) {
        final Diet diet = items.get(position);
        holder.getView().setId(diet.getId());
        holder.getView().setName(diet.getName());
    }

}