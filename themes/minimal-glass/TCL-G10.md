# Optional TCL G10 configuration

This profile uses existing launcher features. It adds no Java code, permissions, dependencies, PIN screen or special multi-press gesture.

Tested on one TV reporting **TCL G10, Android TV 12, SDK 31, firmware STT2.230203.001**, using Ethernet. The retail model was reported only as Smart TV. This does not establish compatibility with every TCL model.

## What the profile changes

[config.tcl-g10.json](config.tcl-g10.json) is a complete theme configuration with:

- Five dock apps, in order: Netflix, YouTube, Prime Video, Disney+ and Apple TV. Missing apps are skipped according to launcher behavior.
- `live_tv_package: "com.tcl.tv"` for this TV's tuner and input integration.
- The app library, its top-bar button, OK-to-open behavior and the tile menu for Home visibility.
- `stock_home_redirect: "com.google.android.apps.tv.launcherx"` and `key_filter: true` for the optional Home button accessibility service.
- `power_on_home_always: true` opens Home after power-on, once the accessibility service is available. `consume_volume_keys: true` supplies held-volume repeat while key filtering is enabled.

The generic [config.json](config.json) does not include TCL Home redirection. The profile does not disable Google TV, setupwraith, Alexa, casting, firmware updates or streaming apps.

## Prepare and apply

Start with a locally built launcher already installed using the repository's installation procedure. Check the actual TV model, SDK, installed packages and original Home component first. Save packages, disabled packages, system properties, settings, accessibility state and the current theme locally before making changes. Keep that backup out of Git. Record the existing auto-start permission before changing it.

From the repository root, prepare a device-specific copy under the ignored `work/` directory:

```bash
mkdir -p work/tcl-g10-minimal-glass
cp themes/minimal-glass/config.tcl-g10.json work/tcl-g10-minimal-glass/config.json
cp -R themes/minimal-glass/wallpapers work/tcl-g10-minimal-glass/
export TV_HOST='<tv ip>'
tools/theme.sh work/tcl-g10-minimal-glass
```

Replace `<tv ip>` with the TV address. The full launcher installation script attempts to set default Home; `STOCK_LAUNCHER=none` preserves Google TV but does not guarantee that the firmware will accept a third-party default. On this TV the command reported success while Home still resolved to Google TV. Home redirection was used instead.

For this G10, the recognized startup operation was `APP_AUTO_START`, initially defaulting to `ignore`. The service's permission was set and persisted using the same operations as the existing installer:

```bash
tools/venv/bin/python -I tools/tv.py 'cmd appops set com.gathin.tvhome APP_AUTO_START allow'
tools/venv/bin/python -I tools/tv.py 'appops write-settings'
```

In the TV's system **Accessibility** settings, enable **Home button** (Spanish: **Botón Inicio**), possibly grouped under **Home/Inicio**. Preserve other accessibility services. Applying a theme config alone does not enable this service.

## Verify on the real remote

Verify Home from an app, held Volume Up/Down, the streaming-app buttons, Inputs, Settings, standby/wake and a full reboot. Do not treat an injected Home key as proof of the physical remote.

With `key_filter: false`, physical Home remained on Google TV. The final configuration uses `stock_home_redirect`, `key_filter: true`, `consume_volume_keys: true` and `power_on_home_always: true`, with the Home button service enabled. The owner confirmed physical Home and off/on operation. Google TV remains enabled and retains the system HOME role; the helper opens CleanYourTV over it. Behavior after disconnecting mains power has not been separately verified with this final configuration and may differ across firmware.

Key filtering affects held-volume repeat on some other TCL firmware. If that or another remote function regresses, disable the helper and restore the recorded configuration. Echo services were retained, but explicit voice commands were not verified in this session.

## Return to Google TV

Open the TV's system **Settings → Accessibility → Home button/Botón Inicio** and disable that service. Press Home. Google TV remains installed and enabled, so this does not reset the TV or delete accounts. Re-enable the service to restore Home redirection.

There is no PIN, seven-second Home gesture or four-press shortcut in this configuration. Returning through Accessibility is the existing supported method. Theme reset alone is not a complete rollback of accessibility or startup permissions.

For complete rollback, disable this helper while preserving other accessibility services, restore the previous theme from the local backup and restore the recorded auto-start mode. On the tested TV that mode was the default (`ignore`), so the recorded rollback uses:

```bash
tools/venv/bin/python -I tools/tv.py 'cmd appops set com.gathin.tvhome APP_AUTO_START default'
tools/venv/bin/python -I tools/tv.py 'appops write-settings'
```

Do not copy that default to a different TV without recording its original mode. If changing the Home selection, use the exact component saved in that TV's backup.

## Close debugging

After verification, turn off USB and network/wireless debugging, revoke debugging authorizations and reboot. Check the same TV address and port with:

```bash
python3 tools/adb-closed.py '<tv ip>'
```

An earlier session returned **connection refused** after debugging was disabled. Later development required enabling debugging again; disconnecting the development client alone does not close the TV's port. Follow the steps above and verify closure after testing. The theme and accessibility helper work without keeping debugging enabled.
