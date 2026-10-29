package com.gathin.tvhome;

import android.content.Context;

/** Local choices for the opt-in glass header theme; no services or backdrop capture. */
final class Appearance {
    static final String FINISH = "appearance_finish";
    static final String DIM = "appearance_dim";
    private Appearance() {}
    static int dim(int chosen, int theme) {
        return chosen >= 0 && chosen <= 90 ? chosen : theme;
    }
    static boolean solid(Context context) {
        return context.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE)
                .getInt(FINISH, 0) == 2;
    }
}
