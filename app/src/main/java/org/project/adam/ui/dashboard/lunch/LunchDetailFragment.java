package org.project.adam.ui.dashboard.lunch;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Lunch;

@EFragment(R.layout.fragment_lunch)
public class LunchDetailFragment extends BaseFragment {

    @ViewById(R.id.content)
    TextView content;

    private Lunch lunch;

    public void bind(Lunch lunch) {
        this.lunch = lunch;
    }

    @AfterViews
    void init() {
        content.setText(lunch.getContent());
    }
}
