package com.hit.game012.startupsequence;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
