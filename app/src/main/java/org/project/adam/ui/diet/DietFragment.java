package org.project.adam.ui.diet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Lunch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import timber.log.Timber;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_diet)
public class DietFragment extends BaseFragment implements DietListAdapter.DietSelectorListener {

    private static final int SELECT_FILE_RESULT_CODE = 666;

    DietListViewModel dietListViewModel;

    @Bean
    DietListAdapter listAdapter;
    @Bean
    DietUtils dietUtils;

    @ViewById(R.id.item_list)
    RecyclerView items;

    @Bean
    DietLoader dietLoader;

    @StringRes(R.string.select_file_to_load)
    protected String fileSelectionTitle;

    @AfterViews
    void setUpRepoAdapter() {
        items.setAdapter(listAdapter);
        items.setHasFixedSize(true);
    }

    @AfterViews
    void setUpDietSelector() {
        listAdapter.setDietSelectorListener(this);
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
    public void onAddDietClick() {
        Timber.d("onAddDietClick - launching file selector intent");
        Intent  intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Intent.normalizeMimeType("text/plain"));
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, fileSelectionTitle),
            SELECT_FILE_RESULT_CODE);
    }

    @Override
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void dietSelected(final Diet diet) {

        new AlertDialog.Builder(getContext())
            .setTitle(R.string.set_current_status_confirmation_title)
            .setMessage(R.string.set_current_status_confirmation_message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dietUtils.setCurrent(diet);
                    listAdapter.reload();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();

    }

    @Override
    public void removeDiet(final Diet diet) {
        new AlertDialog.Builder(getContext())
            .setTitle(R.string.remove_diet_confirmation_title)
            .setMessage(R.string.remove_diet_confirmation_message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                dietListViewModel.removeDiet(diet);
                dietUtils.clearCurrent ();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult - %d - %d", requestCode, resultCode);
        if(requestCode == SELECT_FILE_RESULT_CODE){
            if(resultCode == Activity.RESULT_OK){
                Timber.w("Got from result: %s", data.getDataString());
                try{
                    List<Lunch> lunches = dietLoader.parseLunchesFromCsv(getActivity().getContentResolver()
                        .openInputStream(data.getData()));

                }catch (IOException f){
                    Timber.w(f, "Error while reading file");
                }
            } else{
                Timber.w("Invalid result got from file selection");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
