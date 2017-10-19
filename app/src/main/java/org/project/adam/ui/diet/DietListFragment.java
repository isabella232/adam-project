package org.project.adam.ui.diet;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoreWhen;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;
import org.androidannotations.annotations.res.StringRes;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Meal;

import java.io.IOException;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;
import timber.log.Timber;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_diet_list)
public class DietListFragment extends BaseFragment implements DietListAdapter.DietSelectorListener {

    private static final int SELECT_FILE_RESULT_CODE = 666;
    private static final int DIET_DETAIL_RESULT_CODE = 777;

    DietListViewModel dietListViewModel;

    @Bean
    DietListAdapter listAdapter;
    @Bean
    DietUtils dietUtils;
    @Bean
    MealLoader mealLoader;

    @ViewById(R.id.item_list)
    RecyclerView items;

    @StringRes(R.string.select_file_to_load)
    String fileSelectionTitle;

    @StringRes(R.string.enter_diet_name_title)
    String enterDietNameTitle;

    @StringRes(R.string.enter_diet_name_content)
    String enterDietNameContent;

    @StringRes(R.string.enter_diet_name_ok_btn)
    String enterDietNameOk;

    @DimensionPixelSizeRes(R.dimen.medium_margin)
    int verticalMargin;

    @AfterViews
    void setUpRepoAdapter() {
        items.setAdapter(listAdapter);
        items.setHasFixedSize(true);
        items.addItemDecoration(new VerticalSpaceItemDecoration(verticalMargin));
        listAdapter.setDietSelectorListener(this);
    }

    @AfterViews
    void setUpViewModel() {
        dietListViewModel = ViewModelProviders.of(getActivity()).get(DietListViewModel.class);
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Intent.normalizeMimeType("text/plain"));
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, fileSelectionTitle),
            SELECT_FILE_RESULT_CODE);
    }

    @OnActivityResult(SELECT_FILE_RESULT_CODE)
    void onFileSelectedResult(int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                Context context = getContext();
                List<Meal> meals = mealLoader.parseMealsFromCsv(context.getContentResolver()
                    .openInputStream(data.getData()));
                createDiet(context, meals);
            } catch (IOException f) {
                Timber.w(f, "Error while reading file");
            }
        } else {
            Timber.w("Invalid result got from file selection");
        }
    }

    @OnActivityResult(DIET_DETAIL_RESULT_CODE)
    void onDietDetailBack(int resultCode, @OnActivityResult.Extra(value = DietDetailActivity.NEW_CURRENT_DIET_ID_EXTRA) int newCurrentDietId) {
        if (resultCode == Activity.RESULT_OK) {
            listAdapter.reload();
        }
    }

    @Override
    @IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
    public void dietSelected(Diet diet) {
        DietDetailActivity_.intent(this).dietId(diet.getId()).startForResult(DIET_DETAIL_RESULT_CODE);
    }


    private void createDiet(final Context context, final List<Meal> meals) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(enterDietNameTitle);
        alert.setMessage(enterDietNameContent);
        final EditText userInput = new EditText(context);
        alert.setView(userInput);
        alert.setPositiveButton(enterDietNameOk, null); //set later, to handle dismiss manually

        AlertDialog alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dietName = userInput.getText().toString();
                        if (dietName.trim().isEmpty()) {
                            userInput.setError("Name required!");
                        } else {
                            dialog.dismiss();
                            dietListViewModel.createDiet(Diet.builder()
                                    .name(dietName)
                                    .build(),
                                meals.toArray(new Meal[meals.size()]));
                        }
                    }
                });
            }
        });
        alertDialog.show();

    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    private class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        int verticalSpaceHeight;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            final int childAdapterPosition = parent.getChildAdapterPosition(view);
            if (listAdapter.getItemViewType(childAdapterPosition) == DietListAdapter.CURRENT_TYPE) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}
