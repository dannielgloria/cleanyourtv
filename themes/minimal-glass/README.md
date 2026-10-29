# Minimal Glass (draft)

Minimal home screen with five columns, a translucent dock, a restrained glass focus highlight and reduced motion. Wallpaper follows morning, afternoon and night, with a 25% dark overlay for white text. The base configuration preserves configured apps and their order and does not configure remote helpers. The optional [TCL G10 profile](TCL-G10.md) adds the tested dock, TCL Live TV package and Home redirection using existing launcher options.

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

## Uniform app cards and focus

`uniform_tiles: true` uses each app’s original icon, centred on equally sized rounded cards with a background tint sampled from the icon. App banners are replaced by their icons; module cards keep their supplied artwork.

`animations: true`, `reduced_motion: true` and `focus_overshoot: false` give the focused card a short, smooth 10% enlargement without bounce. `glass_style: "halo"` adds a crisp rim and a light outer glow. These existing options apply to both the base theme and the optional TCL profile. Set `uniform_tiles` to `false` to restore app banners.

## Glass header buttons

`glass_header: true` enables thin white line icons over layered translucent circles for Inputs, network, App library and Settings. The network glyph follows Ethernet or Wi-Fi. A stronger white rim marks the D-pad focus while the icon stays white. This uses static vectors and gradients, without blur or network access.

The option is off in the launcher's defaults. This theme enables it alongside `icon_only_buttons` and `icon_only_inputs`; labelled controls elsewhere keep their pill shape. Existing content descriptions, click actions and navigation are preserved. Optional network status colours still appear when unfocused, and festive skins take priority. Set `glass_header` to `false` to restore the existing header style.

A launcher rebuild is required before applying this updated theme; older builds ignore this new key. The header change is pending real-TV verification and a screenshot for `docs/switches.md`.

## App library and Home visibility

The theme enables the launcher's existing App library, with one button in the top bar and no extra library pill below the grid. These are existing launcher options, not a new folder feature.

- Hold OK on a Home tile and choose **Remove from Home** to hide its shortcut. The app stays installed.
- Open **App library** to access installed launchable apps, including those removed from Home. Press OK to open an app.
- Hold OK on an app in the library and choose **Add to Home** to restore its shortcut.

Home visibility choices survive launcher restarts, theme changes and theme resets. The library is a single list, not a set of named folders. Its top-bar button and opening the library were verified on the TCL described below; removing and restoring individual shortcuts still need owner verification.

## Solid appearance

Set `dock_glass` to `false`, `dock_opacity` to `100` and `glass_style` to `"rim"`, then reapply the theme.

## Validation and remaining work

A device-specific copy of this theme was installed on a **TCL G10, Android TV 12, firmware STT2.230203.001**. The night wallpaper, translucent dock and five-app layout were inspected in real TV captures. The library button and opening its app list were checked on the TV. Physical Home and held-volume repeat worked with the optional TCL profile, and the owner confirmed that standby/off-on returned to CleanYourTV. The Home helper and configuration survived a full reboot, but Google TV was initially visible; injected Home then opened CleanYourTV. Automatic CleanYourTV display immediately after a full reboot is not established.

The [TCL setup and rollback guide](TCL-G10.md) documents the optional profile, its accessibility helper and how to return to Google TV. Google TV and every previously enabled package were left enabled. The session ended with debugging disabled, authorizations revoked and the ADB port refusing connections. Private backups, device addresses and raw diagnostic files stay outside Git.

Launcher build validation: 240 JVM tests passed, lint had zero errors and the release APK was 530,097 bytes, below the 589,824-byte gate. The initial draft Python suite ran 1,340 tests with 12 skipped and three catalog failures: missing screenshot, missing catalog row and theme count. See the commit validation for subsequent runs.

Still pending: day/afternoon legibility, observing scheduled wallpaper transitions, removing/restoring library shortcuts, explicit Echo commands, and full D-pad edge coverage. Alexa depends on the TV's existing integration; this theme does not add Alexa support.

Before catalog inclusion, supply a real Home screenshot (`screenshot.jpg`, 1280×720, at most 300 KB), add the catalog row and update the theme count together. Captures from this session remain local; generated mockups are not device screenshots. The three user-supplied wallpapers are preserved without modification. Confirm redistribution rights and prepare 1920×1080 versions below about 300 KB before upstream submission. No sunset wallpaper is included.

## Library and appearance controls

With `glass_header` enabled, the library uses a vertical grid, a dark translucent panel and matching Home and Launcher settings buttons. Back or Home closes the library; the remote navigates the grid in both directions. App launch and hold actions are unchanged.

Launcher settings now offers **Appearance** (theme default, Glass or Solid) and **Wallpaper dimming** (theme default, 0–90%). The existing dock opacity choices also set the glass library panel opacity, with a 75% minimum for readable text. Solid uses opaque surfaces, a rim focus and disables the opacity choices until Glass or theme default is restored. The wallpaper remains visible outside the panels.

Choices are local to the TV. Follow theme clears them, and applying or resetting a theme clears them with the other appearance overrides. These controls and the library restyle are opt-in through `glass_header`; other themes retain the original library and settings. New labels are translated in Spanish; other locales currently use English for these four labels. Emulator checks do not replace the pending real-TV verification.

Appearance validation: Android TV 14 ARM64 emulator checked the library header navigation, Glass/Solid changes on returning Home, wallpaper dimming selection and restoring theme defaults. 242 JVM tests passed; lint has zero errors. The full Python suite ran 1,340 tests (12 skipped) with the same four draft catalog/screenshot failures. Multi-row library scrolling and physical-remote checks still need a TV with more installed apps.
