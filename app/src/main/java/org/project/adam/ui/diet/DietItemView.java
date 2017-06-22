package org.project.adam.ui.diet;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;
import org.project.adam.persistence.Diet;

@EViewGroup(R.layout.diet_item)
public class DietItemView extends RelativeLayout {

    @ViewById(R.id.name)
    TextView name;

    @Bean
    DietUtils dietUtils;

    public DietItemView(Context context) {
        super(context);
    }

    public void bind(final Diet diet) {
        this.name.setText(diet.getName());
        final boolean isCurrent = dietUtils.isCurrent(diet);
    }


}