# Platform behavior

The library follows public Android resources. Material 3 is a useful comparison, not the specification of every returned value.

| Android API | Raw palette | Semantic roles |
| --- | --- | --- |
| Below 31 | `null` | `null` |
| 31–33 | Five families, 13 stops each | Derived from available stops; static baseline error roles |
| 34 | Same | Public role resources; inverse/tint aliases and black scrim |
| 35–36 | Adds error family | Same role strategy as 34 |
| 37+ | Six families | Dedicated platform resources also used for inverse roles, tint, and scrim |

There is no whole-palette fallback for unsupported devices. Compatibility role derivation does contain explicit fallback rules. Calling those values universally “raw OS colors” would be inaccurate.

## Day/night resolution

AOSP Android 17 maps unsuffixed fixed resources to different day and night resources. For example, the baseline `system_primary_fixed` resolves to `#B9CBFF` by day and `#C1CEF7` by night. The name “fixed” does not guarantee equal resource values across configurations. Raw ramps can also differ: baseline accent1 step 600 is `#4B5D8B` by day and `#505E81` by night.

`dynamicColorsOrNull` reads each role set with its own night-mode configuration. `dynamicPaletteOrNull` intentionally reads only the supplied configuration. See AOSP [day colors](https://android.googlesource.com/platform/frameworks/base/+/94b4c163b7dfe5ce3607f7bb8456f9573f7de57d/core/res/res/values/colors.xml) and [night colors](https://android.googlesource.com/platform/frameworks/base/+/94b4c163b7dfe5ce3607f7bb8456f9573f7de57d/core/res/res/values-night/colors.xml). Wallpaper overlays may replace these baseline values; equal colors on a particular device do not invalidate the configuration distinction.

## Differences from Material 3

On API 34+, this library reads platform error roles. The inspected AndroidX implementation leaves those roles at its static defaults. On API 37+, dedicated inverse resources can differ from the opposite-mode primary/surface aliases still used by the inspected Material 3 implementation. Neither difference is silently normalized away.

On API 31–33, Material 3 synthesizes additional surface tones. This library uses the nearest available platform stop instead:

| Role | Material 3 tone, light / dark | Library tone, light / dark |
| --- | --- | --- |
| Background, surface | 98 / 6 | 99 / 10 |
| Surface bright | 98 / 24 | 99 / 20 |
| Surface dim | 87 / 6 | 90 / 10 |
| Container lowest | 100 / 4 | 100 / 0 |
| Container low | 96 / 10 | 95 / 10 |
| Container | 94 / 12 | 95 / 10 |
| Container high | 92 / 17 | 90 / 20 |
| Container highest | 90 / 22 | 90 / 20 |

These use the neutral-variant ramp. The difference can be visible, particularly dark container-lowest becoming black. This is a documented approximation, not byte-for-byte Material 3 parity.

Compare the pinned [AndroidX implementation](https://android.googlesource.com/platform/frameworks/support/+/9b80fd8e09ea54f0408f9171133bfd412f8a9aef/compose/material3/material3/src/androidMain/kotlin/androidx/compose/material3/DynamicTonalPalette.android.kt). The review also checked remote AndroidX commit `7c0fa4c8b34df92135809889ec3f7b209e735561`: its styled-attribute implementation retains the relevant aliases and error behavior.

## Updates, contrast, and performance

AOSP regenerates color overlays after wallpaper/style/contrast changes. Compose listens through `LocalConfiguration`; the Views adapter listens while attached and posts a coalesced refresh after configuration delivery. Core values are immutable snapshots, not live objects. There is no global Context cache.

The OS owns semantic contrast choices. Raw fixed-tone ramps alone do not establish accessible text/background pairings; select appropriate semantic roles and validate your actual UI. No algorithm here interpolates colors or promises a particular contrast ratio on every vendor implementation.

Models snapshot their constructor callbacks once into private arrays, with structural equality. Reads allocate bounded snapshots; Compose retains them between configuration changes. Individual `color()` extensions convert a requested ARGB value. The Compose theme adapter builds and remembers a native `DynamicColorScheme`. No benchmark-based speed claim is made. The core has no Compose stability annotation or consumer-wide stability guarantee.
