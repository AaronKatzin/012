package com.hit.game012.startupsequence;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class to add animation sequence to ImageView element.
 * Inherit from ImageView.
 */
public class AnimatedImageView extends androidx.appcompat.widget.AppCompatImageView {


    public AnimatedImageView(@NonNull Context context) {
        super(context);

    }

    public AnimatedImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public AnimatedImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public void initPadlockAnimation(){
        this.setVisibility(VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        this.setAnimation(animation);
    }
}
