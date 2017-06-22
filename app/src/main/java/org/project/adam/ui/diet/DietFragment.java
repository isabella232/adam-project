package org.project.adam.ui.diet;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Diet;

import java.util.List;

import timber.log.Timber;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_diet)
public class DietFragment extends BaseFragment {

    DietListViewModel dietListViewModel;

    @Bean
    DietListAdapter listAdapter;

    @ViewById(R.id.item_list)
    RecyclerView items;

    @AfterViews
    void setUpRepoAdapter() {
        items.setAdapter(listAdapter);
        items.setHasFixedSize(true);
    }

    @AfterViews
    void setUpViewModel() {
        dietListViewModel = ViewModelProviders.of(this).get(DietListViewModel.class);
        dietListViewModel.getDiets()
            .observe(this, new Observer<List<Diet>>() {
                @Override
                public void onChanged(@Nullable List<Diet> diets) {
                    listAdapter.update(diets);
                }
            });
    }

    @Click(R.id.add_diet)
    public void onAddDietClick(){
        Timber.d("add diet clicked");
    }

}
