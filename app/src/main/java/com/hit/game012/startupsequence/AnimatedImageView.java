package com.hit.game012.startupsequence;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hit.game012.R;

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
     * Adding and starting fade-in animation to current ImageView
     *
     * @param duration in milliseconds of the fade-in animation
     * @param offset in milliseconds before animation starts
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

    /**
     * Adding and starting fade-in fade-out animation to current ImageView
     */
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
