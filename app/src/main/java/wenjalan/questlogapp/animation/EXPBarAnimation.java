package wenjalan.questlogapp.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class EXPBarAnimation extends Animation {

    private ProgressBar expBar;
    private float finish;
    private float start;

    public EXPBarAnimation(ProgressBar expBar, float finish, float start) {
        super();
        this.expBar = expBar;
        this.finish = finish;
        this.start = start;
    }

    @Override
    protected void applyTransformation(float time, Transformation t) {
        super.applyTransformation(time, t);
        float progress = start + (finish - start) * time;
        expBar.setProgress((int) progress);
    }

}
