package org.project.adam.ui.diet;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.project.adam.persistence.Diet;
import org.project.adam.util.ui.RecyclerViewAdapterBase;
import org.project.adam.util.ui.ViewWrapper;

import java.util.ArrayList;
import java.util.Collection;

@EBean
class DietListAdapter extends RecyclerViewAdapterBase<Diet, DietItemView> {

    @RootContext
    Context context;

    @Bean
    DietUtils dietUtils;

    @Override
    protected DietItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DietItemView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<DietItemView> holder, int position) {
        final Diet diet = items.get(position);
        holder.getView().bind(diet);
    }

    @Override
    public void update(Collection<Diet> diets) {

        ArrayList<Diet> firstCurrentDietList = new ArrayList<>();

        for (Diet diet : diets) {
            if (dietUtils.isCurrent(diet)) {
                firstCurrentDietList.add(0, diet);
            } else {
                firstCurrentDietList.add(diet);
            }
        }

        super.update(firstCurrentDietList);
    }
}