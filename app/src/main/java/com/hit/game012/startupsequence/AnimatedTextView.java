package com.hit.game012.startupsequence;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/**
 * Class to add animation sequence to TextView element.
 * Inherit from TextView.
 */
public class AnimatedTextView extends androidx.appcompat.widget.AppCompatTextView{
    public AnimatedTextView(@NonNull Context context) {
        super(context);
    }

    public AnimatedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * Adding and starting fade-in animation on current ImageView
     *
     * @param duration of the fade-in animation in milliseconds
     * @param offset bewfore animation starts in milliseconds
     */
    public void initFadeInAnimation(long duration, long offset){
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(duration);
        if (offset != 0) fadeIn.setStartOffset(offset);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        this.setAnimation(animation);
    }
}
