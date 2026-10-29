package com.gathin.tvhome;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.TextView;

/** Static vector artwork and layered gradients: no blur, extra animation or backdrop capture. */
final class HeaderGlass {
    private HeaderGlass() {}

    static void apply(TextView button, int icon) {
        button.setBackgroundResource(R.drawable.header_glass_bg);
        if (Appearance.solid(button.getContext())) {
            button.setBackgroundResource(R.drawable.header_solid_bg);
        }
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
        button.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
        button.setTextColor(Color.WHITE);
    }

    /** Keep optional network status colours, with a white icon over the focused glass surface. */
    static ColorStateList focusWhite(ColorStateList status) {
        return new ColorStateList(new int[][] {{android.R.attr.state_focused}, {}},
                new int[] {Color.WHITE, status.getDefaultColor()});
    }
}
