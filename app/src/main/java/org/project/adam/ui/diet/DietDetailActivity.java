package org.project.adam.ui.diet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

@EActivity(R.layout.activity_diet_detail)
public class DietDetailActivity extends BaseActivity {

    @Extra
    int dietId;

    @Bean
    DietUtils dietUtils;

    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.remove)
    Button remove;

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
                    remove.setVisibility(count > 1 ? View.VISIBLE : View.INVISIBLE);
                }
            }
        });
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
                    dietUtils.clearCurrent();
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
                    dietUtils.setCurrent(dietDetailViewModel.getDiet().getValue());
                    finish();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();

    }
}
