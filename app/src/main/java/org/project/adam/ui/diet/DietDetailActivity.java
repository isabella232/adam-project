package org.project.adam.ui.diet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseActivity;
import org.project.adam.R;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Lunch;

import java.util.List;

@EActivity(R.layout.activity_diet_detail)
public class DietDetailActivity extends BaseActivity {

    static final String NEW_CURRENT_DIET_ID_EXTRA = "new_current_diet_id";

    @Extra
    int dietId;

    @Bean
    DietUtils dietUtils;

    @Bean
    LunchListAdapter lunchListAdapter;

    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.remove)
    Button remove;
    @ViewById(R.id.set_current)
    Button setCurrent;

    @ViewById(R.id.item_list)
    RecyclerView lunches;

    DietDetailViewModel dietDetailViewModel;

    @AfterViews
    void setUpViewModel() {
        dietDetailViewModel = ViewModelProviders.of(this).get(DietDetailViewModel.class);
        dietDetailViewModel.loadDiet(dietId);
        dietDetailViewModel.getDiet()
            .observe(this, new Observer<Diet>() {
                @Override
                public void onChanged(@Nullable Diet diet) {
                    if (diet != null) {
                        update(diet);
                    }
                }
            });

        dietDetailViewModel.getItemCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                if (count != null) {
                    updateRemoveButton(count);
                }
            }
        });

        dietDetailViewModel.getLunches()
            .observe(this, new Observer<List<Lunch>>() {
                @Override
                public void onChanged(@Nullable List<Lunch> lunches) {
                    lunchListAdapter.update(lunches);
                }
            });
    }

    @AfterViews
    void setUpAdapter() {
        lunches.setAdapter(lunchListAdapter);
        lunches.setHasFixedSize(true);
    }

    @AfterViews
    void updateRemoveButton (){
        remove.setVisibility(dietUtils.isCurrent(dietId) ? View.INVISIBLE : View.VISIBLE);
    }

    @AfterViews
    void updateSetCurrentButton (){
        setCurrent.setVisibility(dietUtils.isCurrent(dietId) ? View.INVISIBLE : View.VISIBLE);
    }


    private void updateRemoveButton(@Nullable Integer count) {
        remove.setVisibility(count > 1 && remove.getVisibility() == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
    }

    private void update(Diet diet) {
        name.setText(diet.getName());
    }

    @Click(R.id.remove)
    void removeDiet() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.remove_diet_confirmation_title)
            .setMessage(R.string.remove_diet_confirmation_message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dietDetailViewModel.removeDiet();
                    finish();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    @Click(R.id.set_current)
    public void dietSelected() {

        new AlertDialog.Builder(this)
            .setTitle(R.string.set_current_status_confirmation_title)
            .setMessage(R.string.set_current_status_confirmation_message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final Diet diet = dietDetailViewModel.getDiet().getValue();
                    dietUtils.setCurrent(diet);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(NEW_CURRENT_DIET_ID_EXTRA, diet.getId());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();

    }
}
