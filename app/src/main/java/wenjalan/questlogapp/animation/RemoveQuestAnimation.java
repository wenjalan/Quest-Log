package wenjalan.questlogapp.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RemoveQuestAnimation extends Animation {

    private View view;
    private int startHeight;

    public RemoveQuestAnimation(View view) {
        this.view = view;

        // measure the height of the view
        startHeight = view.getMeasuredHeight();

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
