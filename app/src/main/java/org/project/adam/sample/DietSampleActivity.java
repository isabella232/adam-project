package org.project.adam.sample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseActivity;
import org.project.adam.R;
import org.project.adam.persistence.Diet;

import java.util.List;

@EActivity(R.layout.activity_model_sample)
public class DietSampleActivity extends BaseActivity {

    DietListViewModel dietListViewModel;

    @Bean
    DietListAdapter listAdapter;

    @ViewById(R.id.item_list)
    RecyclerView items;

    @AfterViews
    void setUpRepoAdapter() {
        items.setAdapter(listAdapter);
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

    @Click(R.id.add_item)
    void addItem (){
        dietListViewModel.addItem(Diet.builder().name("test").current(true).build());
    }


}


