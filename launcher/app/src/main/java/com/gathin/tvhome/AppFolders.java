package com.gathin.tvhome;

import android.content.Context;
import android.util.AtomicFile;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Owner-created Home folders, independent of theme files, shortcuts and tile ordering. */
final class AppFolders {
    private static final String HEADER = "tvhome-folders 1";
    static final int MAX_FOLDERS = 24, MAX_MEMBERS = 64;

    static final class Folder {
        final String id, name;
        final List<String> packages;
        Folder(String id, String name, List<String> packages) {
            this.id = id;
            this.name = name;
            this.packages = Collections.unmodifiableList(new ArrayList<>(packages));
        }
    }

    private AppFolders() { }

    static String encode(List<Folder> folders) {
        StringBuilder text = new StringBuilder(HEADER).append('\n');
        for (Folder folder : folders) text.append(folder.id).append('\t').append(folder.name)
                .append('\t').append(String.join(",", folder.packages)).append('\n');
        return text.toString();
    }

    /** Reject a damaged record as a whole, so an app never silently moves between folders. */
    static List<Folder> decode(String text) {
        if (text == null || text.length() > 262144) return null;
        String[] lines = text.split("\n", -1);
        if (!lines[0].equals(HEADER)) return null;
        List<Folder> result = new ArrayList<>();
        Set<String> ids = new HashSet<>(), members = new HashSet<>();
        for (int i = 1; i < lines.length; i++) {
            if (i == lines.length - 1 && lines[i].isEmpty()) break;
            String[] fields = lines[i].split("\t", -1);
            if (fields.length != 3 || !fields[0].matches("[a-f0-9]{32}") || !ids.add(fields[0])
                    || !validName(fields[1]) || result.size() >= MAX_FOLDERS) return null;
            List<String> packages = new ArrayList<>();
            for (String pkg : fields[2].split(",", -1)) {
                if (!pkg.matches("[a-zA-Z][a-zA-Z0-9_]*(\\.[a-zA-Z0-9_]+)+")
                        || pkg.length() > 255 || !members.add(pkg) || packages.size() >= MAX_MEMBERS) return null;
                packages.add(pkg);
            }
            result.add(new Folder(fields[0], fields[1], packages));
        }
        return result;
    }

    static boolean validName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 40) return false;
        for (int i = 0; i < name.length(); i++) {
            int type = Character.getType(name.charAt(i));
            if (Character.isISOControl(name.charAt(i)) || type == Character.FORMAT
                    || type == Character.LINE_SEPARATOR || type == Character.PARAGRAPH_SEPARATOR) return false;
        }
        return true;
    }

    private static AtomicFile file(Context ctx) {
        return new AtomicFile(new File(ctx.getFilesDir(), "app-folders.txt"));
    }

    static List<Folder> load(Context ctx) {
        AtomicFile file = file(ctx);
        try {
            if (!file.getBaseFile().exists() && !new File(file.getBaseFile() + ".bak").exists()) return new ArrayList<>();
            List<Folder> folders = decode(new String(file.readFully(), StandardCharsets.UTF_8));
            if (folders != null) return folders;
        } catch (IOException e) {
            Log.w("TvHome.Folders", "Could not read folders", e);
        }
        return new ArrayList<>();
    }

    static boolean save(Context ctx, List<Folder> folders) {
        String text = encode(folders);
        if (decode(text) == null) return false;
        AtomicFile file = file(ctx);
        FileOutputStream out = null;
        try {
            out = file.startWrite();
            out.write(text.getBytes(StandardCharsets.UTF_8));
            file.finishWrite(out);
            return true;
        } catch (IOException e) {
            if (out != null) file.failWrite(out);
            Log.w("TvHome.Folders", "Could not save folders", e);
            return false;
        }
    }
}
