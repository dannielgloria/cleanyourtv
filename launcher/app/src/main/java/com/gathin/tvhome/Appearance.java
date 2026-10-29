package com.gathin.tvhome;

import android.content.Context;

import java.util.Calendar;

/** Local choices for the opt-in glass header theme; no services or backdrop capture. */
final class Appearance {
    static final String FINISH = "appearance_finish";
    static final String DIM = "appearance_dim";
    static final String PALETTE = "appearance_palette";
    private Appearance() {}
    static int dim(int chosen, int theme) {
        return chosen >= 0 && chosen <= 90 ? chosen : theme;
    }
    static boolean solid(Context context) {
        return context.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE)
                .getInt(FINISH, 0) == 2;
    }

    /** Theme, dark, light, or light by day and dark after 19:00/before 07:00. */
    static boolean light(Context context) {
        int choice = context.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE)
                .getInt(PALETTE, 0);
        if (choice == 2) return true;
        if (choice != 3) return false;
        return lightAtHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    static boolean lightAtHour(int hour) { return hour >= 7 && hour < 19; }

    static int background(Context context, int theme) {
        return light(context) ? 0xFFE7EDF5 : theme;
    }

    static int panel(Context context) {
        return light(context) ? 0xFFF4F7FB : 0xFF101824;
    }

    static int text(Context context) {
        return light(context) ? 0xFF132033 : 0xFFFFFFFF;
    }
}
