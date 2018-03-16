package wenjalan.questlogapp.animation;
// does the animation to add a Task field in CreateQuest

import android.app.ActionBar;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class AddTaskAnimation extends Animation {

    private View view;
    private int finalHeight;

    public AddTaskAnimation(View view) {
        this.view = view;

        // measure the final height
        // view.measure(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        // TODO: Use a measured height instead of a static one
        this.finalHeight = 192; //view.getMeasuredHeight();

        // set the starting height to 1
        view.getLayoutParams().height = 1;

        // set the view to visible
        view.setVisibility(View.VISIBLE);

        // set the duration
        int duration = (int) (finalHeight / view.getContext().getResources().getDisplayMetrics().density);
        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == 1) {
            view.getLayoutParams().height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        }
        else {
            int newHeight = (int) (finalHeight * interpolatedTime);
            view.getLayoutParams().height = newHeight;
        }
        view.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

}
