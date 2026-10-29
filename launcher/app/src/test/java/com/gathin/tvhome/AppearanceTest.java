package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AppearanceTest {
    @Test public void dimAllowsBothEnds() {
        assertEquals(0, Appearance.dim(0, 25));
        assertEquals(90, Appearance.dim(90, 25));
    }
    @Test public void invalidDimFollowsTheme() {
        assertEquals(25, Appearance.dim(-1, 25));
        assertEquals(40, Appearance.dim(91, 40));
    }
}
