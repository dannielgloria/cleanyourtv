package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Two explicit steps (name, then members); cancel never changes Home. */
final class FolderEditor {
    private FolderEditor() { }

    private static AlertDialog.Builder dialog(Activity activity) {
        return new AlertDialog.Builder(activity, Appearance.light(activity)
                ? R.style.Theme_TvHome_MinimalLightDialog : R.style.Theme_TvHome_MinimalDialog);
    }

    static void edit(Activity activity, AppFolders.Folder folder, String initial,
                     List<AppAdapter.Entry> candidates, Runnable changed) {
        EditText name = new EditText(activity);
        name.setSingleLine(true);
        name.setTextColor(Appearance.text(activity));
        name.setHintTextColor(Appearance.text(activity));
        name.setHint(R.string.folder_name);
        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        if (folder != null) name.setText(folder.name);
        AlertDialog prompt = dialog(activity).setTitle(folder == null ? R.string.folder_create : R.string.folder_edit)
                .setView(name).setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null).create();
        prompt.setOnShowListener(ignored -> prompt.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String label = name.getText().toString().trim();
            if (!AppFolders.validName(label)) { name.setError(activity.getString(R.string.folder_name)); return; }
            prompt.dismiss();
            members(activity, folder, initial, label, candidates, changed);
        }));
        prompt.show();
    }

    private static void members(Activity activity, AppFolders.Folder folder, String initial, String name,
                                List<AppAdapter.Entry> candidates, Runnable changed) {
        List<AppFolders.Folder> folders = AppFolders.load(activity);
        Set<String> taken = new HashSet<>();
        for (AppFolders.Folder item : folders) if (folder == null || !item.id.equals(folder.id)) taken.addAll(item.packages);
        List<AppAdapter.Entry> apps = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (AppAdapter.Entry app : candidates) if (!taken.contains(app.pkg) && seen.add(app.pkg)) apps.add(app);
        apps.sort((a, b) -> a.label.compareToIgnoreCase(b.label));
        CharSequence[] names = new CharSequence[apps.size()];
        boolean[] selected = new boolean[apps.size()];
        for (int i = 0; i < apps.size(); i++) {
            names[i] = apps.get(i).label;
            selected[i] = folder == null ? apps.get(i).pkg.equals(initial) : folder.packages.contains(apps.get(i).pkg);
        }
        AlertDialog picker = dialog(activity).setTitle(name)
                .setMultiChoiceItems(names, selected, (d, index, checked) -> selected[index] = checked)
                .setPositiveButton(android.R.string.ok, null).setNegativeButton(android.R.string.cancel, null).create();
        picker.setOnShowListener(ignored -> picker.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            List<String> packages = new ArrayList<>();
            for (int i = 0; i < apps.size(); i++) if (selected[i]) packages.add(apps.get(i).pkg);
            // Preserve temporarily unavailable members while editing; they return with the app.
            if (folder != null) for (String pkg : folder.packages) if (!seen.contains(pkg)) packages.add(pkg);
            if (packages.isEmpty() || packages.size() > AppFolders.MAX_MEMBERS) {
                Toast.makeText(activity, R.string.folder_choose_apps, Toast.LENGTH_SHORT).show();
                return;
            }
            String id = folder == null ? UUID.randomUUID().toString().replace("-", "") : folder.id;
            List<AppFolders.Folder> current = AppFolders.load(activity);
            current.removeIf(item -> item.id.equals(id));
            current.add(new AppFolders.Folder(id, name, packages));
            if (!AppFolders.save(activity, current)) {
                Toast.makeText(activity, R.string.library_save_failed, Toast.LENGTH_LONG).show();
                return;
            }
            picker.dismiss();
            changed.run();
        }));
        picker.show();
    }

    static void remove(Activity activity, AppFolders.Folder folder, Runnable changed) {
        dialog(activity).setTitle(R.string.folder_delete).setMessage(R.string.folder_delete_hint)
                .setNegativeButton(android.R.string.cancel, null).setPositiveButton(android.R.string.ok, (d, which) -> {
                    List<AppFolders.Folder> folders = AppFolders.load(activity);
                    folders.removeIf(item -> item.id.equals(folder.id));
                    if (AppFolders.save(activity, folders)) changed.run();
                    else Toast.makeText(activity, R.string.library_save_failed, Toast.LENGTH_LONG).show();
                }).show();
    }
}
