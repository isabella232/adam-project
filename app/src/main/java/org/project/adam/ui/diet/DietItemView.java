package org.project.adam.ui.diet;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;
import org.project.adam.persistence.Diet;

import lombok.Getter;

@EViewGroup(R.layout.diet_item)
public class DietItemView extends RelativeLayout {

    @ViewById(R.id.set_as_current)
    @Getter
    Button setAsCurrent;
    @ViewById(R.id.name)
    TextView name;

    @Bean
    DietUtils dietUtils;

    public DietItemView(Context context) {
        super(context);
    }

    public void bind(final Diet diet) {
        this.name.setText(diet.getName());
        this.setAsCurrent.setText(dietUtils.isCurrent(diet) ? R.string.current_diet : R.string.set_current_status);
    }


}