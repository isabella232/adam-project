package org.project.adam.ui.diet;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.project.adam.R;
import org.project.adam.persistence.Diet;
import org.project.adam.util.ui.RecyclerViewAdapterBase;
import org.project.adam.util.ui.ViewWrapper;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Setter;

@EBean
class DietListAdapter extends RecyclerViewAdapterBase<Diet, DietItemView> {

    interface DietSelectorListener {
        void dietSelected(Diet diet);
        void removeDiet(Diet diet);
    }

    @RootContext
    Context context;

    @Bean
    DietUtils dietUtils;

    @Setter
    DietSelectorListener dietSelectorListener;

    @Override
    protected DietItemView onCreateItemView(ViewGroup parent, int viewType) {
        final DietItemView item = DietItemView_.build(context);
        return item;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<DietItemView> holder, int position) {
        final Diet diet = items.get(position);
        final DietItemView view = holder.getView();
        view.bind(diet);

        if (dietSelectorListener == null) {
            return;
        }

        view.getSetAsCurrent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dietSelectorListener.dietSelected(diet);
            }
        });

        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.getMenuInflater().inflate(R.menu.diet_contextual_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.remove_diet){
                            dietSelectorListener.removeDiet(diet);
                            return true;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    public void reload() {
        update(items);
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