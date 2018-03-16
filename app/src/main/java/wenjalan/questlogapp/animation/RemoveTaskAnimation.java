package wenjalan.questlogapp.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RemoveTaskAnimation extends Animation {

    private View view;
    private int startHeight;

    public RemoveTaskAnimation(View view) {
        this.view = view;
        this.startHeight = 192;

        // copied from AddTaskAnimation
        // set the duration
        setDuration(QuestLogAnimation.DURATION_SHORT);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == 1) {
            view.setVisibility(View.GONE);
        }
        else {
            int newHeight = startHeight - (int) (startHeight * interpolatedTime);
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

}
