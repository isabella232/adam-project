package org.project.adam.ui.diet;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Diet;

@EViewGroup(R.layout.diet_item)
public class DietItemView extends RelativeLayout {

    @ViewById(R.id.current_status)
    ImageView currentStatus;
    @ViewById(R.id.name)
    TextView name;

    @Pref
    Preferences_ preferences;

    public DietItemView(Context context) {
        super(context);
    }

    public void bind(final Diet diet) {
        this.name.setText(diet.getName());
        this.currentStatus.setVisibility(isCurrent(diet) ? VISIBLE : INVISIBLE);
    }

    private boolean isCurrent(Diet diet) {
        return preferences.currentMenuId().getOr(-1) == diet.getId();
    }

}