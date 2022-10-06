package com.hit.game012.startupsequence;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public void initAnimation (long duration, long offset){
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setInterpolator(new DecelerateInterpolator());
        fadein.setDuration(duration);
        if (offset != 0) fadein.setStartOffset(offset);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadein);
        this.setAnimation(animation);
    }
}
