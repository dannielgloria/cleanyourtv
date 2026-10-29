package com.gathin.tvhome;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

/** Reduced motion (config.xml reduced_motion): the one spring and the one time limit every animation uses then. */
final class Motion {

    /** The longest any reduced-motion animation runs, in milliseconds. */
    static final int REDUCED_MS = 90;
    /** Critically damped (no bounce) and stiff enough that any move, even across the screen, settles in 5 frames. */
    static final float REDUCED_STIFFNESS = 15000f;
    /** The spring stops once it is within this many pixels of its target. */
    static final float REDUCED_MIN_CHANGE = 2f;

    private Motion() {
    }

    /** A short panel entrance; reduced motion fades only, and disabled animations are immediate. */
    static void reveal(android.view.View view, boolean animations, boolean reducedMotion) {
        view.animate().cancel();
        view.setAlpha(animations ? 0f : 1f);
        view.setTranslationY(animations && !reducedMotion
                ? 12f * view.getResources().getDisplayMetrics().density : 0f);
        if (animations) view.animate().alpha(1f).translationY(0f)
                .setDuration(reducedMotion ? REDUCED_MS : 160)
                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();
    }

    static void reduce(SpringAnimation spring) {
        spring.setSpring(new SpringForce().setStiffness(REDUCED_STIFFNESS).setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY));
        spring.setMinimumVisibleChange(REDUCED_MIN_CHANGE);
    }
}
