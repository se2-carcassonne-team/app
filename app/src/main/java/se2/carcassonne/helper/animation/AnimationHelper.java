package se2.carcassonne.helper.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

public class AnimationHelper {

    private AnimationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void fadeIn(final View view, int duration, final Runnable onAnimationEnd) {
        Animation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(duration);

        view.setVisibility(View.VISIBLE);
        view.startAnimation(fadeInAnimation);

        if (onAnimationEnd != null) {
            fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // no implementation needed (yet)
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAnimationEnd.run();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // no implementation needed (yet)
                }
            });
        }
    }

    public static void startButtonAnimation(final Button button) {
        Animation blinkAnimation = new AlphaAnimation(1, 0);
        blinkAnimation.setDuration(800);
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        blinkAnimation.setRepeatMode(Animation.REVERSE);

        button.startAnimation(blinkAnimation);
    }
}
