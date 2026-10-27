# Minimal Glass (draft)

Minimal home screen with five columns, a translucent dock, a restrained glass focus highlight and reduced motion. Wallpaper follows morning, afternoon and night, with a 25% dark overlay for white text. Existing configured apps and their order are preserved. No remote buttons or assistant services are configured by this theme.

From the repository root, with the launcher already installed and ADB authorized:

```bash
TV_HOST=<tv-ip> tools/theme.sh themes/minimal-glass
```

Replace `<tv-ip>` with the TV address. Applying a theme clears local layout and motion overrides. To return to the built-in appearance (not a backup of a previous theme):

```bash
TV_HOST=<tv-ip> tools/theme.sh --reset
```

## Wallpaper schedule

Without latitude and longitude configured in the launcher:

| Period | Wallpaper | Time |
|---|---|---|
| Morning | `wallpapers/01-morning.png` | 07:00–12:00 |
| Afternoon | `wallpapers/02-afternoon.png` | 12:00–19:00 |
| Night | `wallpapers/03-night.jpg` | 19:00–07:00 |

With coordinates configured, morning starts at sunrise and night starts 30 minutes after sunset. The launcher evaluates the wallpaper while Home is active; this is not a background scheduling service. The theme changes the backdrop, not the text palette.

## App library and Home visibility

The theme enables the launcher's existing App library, with one button in the top bar and no extra library pill below the grid. These are existing launcher options, not a new folder feature.

- Hold OK on a Home tile and choose **Remove from Home** to hide its shortcut. The app stays installed.
- Open **App library** to access installed launchable apps, including those removed from Home. Press OK to open an app.
- Hold OK on an app in the library and choose **Add to Home** to restore its shortcut.

Home visibility choices survive launcher restarts, theme changes and theme resets. The library is a single list, not a set of named folders. This configuration still needs a real-remote check on the TV after it is applied.

## Solid appearance

Set `dock_glass` to `false`, `dock_opacity` to `100` and `glass_style` to `"rim"`, then reapply the theme.

## Pending real-TV validation

This draft has not been tested on a TV. A real Home screenshot (`screenshot.jpg`, 1280×720, at most 300 KB) is still required before catalog inclusion. Generated concept images are not device screenshots. Validation: the Python suite ran 1,340 tests, with 12 skipped and three expected failures for this draft: missing real-TV screenshot, missing catalog row and the catalog theme count. All other tests passed. Add the screenshot, catalog row and updated count together after real-TV verification.

Verify D-pad navigation in all four directions, white-text contrast on each wallpaper and existing Echo control on the target TCL. Alexa compatibility depends on the TV's existing integration; this theme does not add Alexa support.

The three wallpaper files were supplied by the user and are preserved without modification. Before upstream submission, confirm redistribution rights and prepare 1920×1080 versions below about 300 KB, as required by the theme workflow. No sunset wallpaper is included.
