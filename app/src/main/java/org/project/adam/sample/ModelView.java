package org.project.adam.sample;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;

@EViewGroup(R.layout.model_item)
public class ModelView extends LinearLayout {
    @ViewById(R.id.id)
    TextView id;
    @ViewById(R.id.name)
    TextView name;

    public ModelView(Context context) {
        super(context);
    }

    public void setId(final String id) {
        this.id.setText(id);
    }
    public void setName(final String name) {
        this.name.setText(name);
    }

}