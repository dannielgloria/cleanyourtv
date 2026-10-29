package com.gathin.tvhome;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class AppFoldersTest {
    private static final String ID = "0123456789abcdef0123456789abcdef";
    private static final String OTHER = "abcdef0123456789abcdef0123456789";

    @Test public void roundTripPreservesNamesAndMembershipOrder() {
        AppFolders.Folder folder = new AppFolders.Folder(ID, "Películas y series",
                Arrays.asList("com.example.first", "com.example.second"));
        List<AppFolders.Folder> result = AppFolders.decode(AppFolders.encode(Collections.singletonList(folder)));
        assertNotNull(result);
        assertEquals(folder.id, result.get(0).id);
        assertEquals(folder.name, result.get(0).name);
        assertEquals(folder.packages, result.get(0).packages);
    }

    @Test public void oneAppCannotBelongToTwoFolders() {
        assertNull(AppFolders.decode("tvhome-folders 1\n" + ID + "\tFirst\tcom.example.app\n"
                + OTHER + "\tSecond\tcom.example.app\n"));
    }

    @Test public void rejectsDuplicateIdsEmptyMembersAndMalformedPackages() {
        String row = ID + "\tTest\tcom.example.app\n";
        assertNull(AppFolders.decode("tvhome-folders 1\n" + row + row));
        assertNull(AppFolders.decode("tvhome-folders 1\n" + ID + "\tTest\t\n"));
        assertNull(AppFolders.decode("tvhome-folders 1\n" + ID + "\tTest\t../package\n"));
        assertNull(AppFolders.decode("wrong\n"));
        assertNull(AppFolders.decode(null));
    }

    @Test public void rejectsControlAndDirectionOverridesInNames() {
        for (String name : Arrays.asList("", "  ", "A\tB", "A\nB", "A\u202eB", "A\u2028B"))
            assertFalse(name, AppFolders.validName(name));
        assertFalse(AppFolders.validName(String.join("", Collections.nCopies(41, "x"))));
        assertTrue(AppFolders.validName("Niños 🎬"));
    }

    @Test public void emptyStoreMeansAllShortcutsRemainIndividual() {
        assertTrue(AppFolders.decode(AppFolders.encode(Collections.emptyList())).isEmpty());
    }

    @Test public void rejectsMoreThanTheMemberLimit() {
        java.util.ArrayList<String> members = new java.util.ArrayList<>();
        for (int i = 0; i <= AppFolders.MAX_MEMBERS; i++) members.add("com.example.app" + i);
        assertNull(AppFolders.decode(AppFolders.encode(Collections.singletonList(
                new AppFolders.Folder(ID, "Too many", members)))));
    }
}
