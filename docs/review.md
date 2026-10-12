# Source validation

The platform mapping was checked against these revisions:

- [AOSP frameworks/base](https://android.googlesource.com/platform/frameworks/base/+/94b4c163b7dfe5ce3607f7bb8456f9573f7de57d/): public resources, day/night colors, overlay generation, and configuration delivery.
- [AOSP frameworks/libs/systemui](https://android.googlesource.com/platform/frameworks/libs/systemui/+/11e04f60f563aed48e4ec080bd7bde06bae1b2f3/): Monet palettes and dynamic roles.
- [AndroidX local reference revision](https://android.googlesource.com/platform/frameworks/support/+/9b80fd8e09ea54f0408f9171133bfd412f8a9aef/): Material 3 color mappings.
- [AndroidX upstream revision](https://android.googlesource.com/platform/frameworks/support/+/7c0fa4c8b34df92135809889ec3f7b209e735561/): styled-attribute mappings.

The resource audit covered 162 platform constants: 65 introduced at API 31, 74 at API 34, 13 at API 35, and 10 at API 37. Runtime comparisons used Android SDK 37/37.1 and an Android 17 emulator (`CP31.260623.012`).

## Mapping decisions

| Area | Behavior |
| --- | --- |
| Light/dark resources | Read with separate configuration contexts. AOSP 17 fixed roles and raw ramps can differ between modes. |
| API 31–33 surfaces | Use the nearest available system tone. Material 3 can synthesize intermediate tones. |
| Error roles | Read platform semantic resources from API 34; use baseline values on API 31–33. Raw error ramps start at API 35. |
| API 37 inverse roles | Read dedicated resources rather than Material 3's opposite-mode aliases. |
| Unsupported devices | Return null below API 31; the app supplies its fallback. |
| Preview fixtures | Synthetic test data, used only when explicitly requested or in Compose inspection mode. |

On the reviewed emulator, light `inversePrimary` was `FFFDF6E3` versus Material 3's `FFCCC6B5`; `inverseOnSurface` was `FF9F9C99` versus `FFEAE5DE`. Platform values are preserved. See [platform behavior](platform-behavior.md) for the compatibility table and tone mapping.

## Tests

Unit tests compare Material 3 mappings in both modes on API 31, 33, 34, and 35. A synthetic configuration test covers differing fixed-role values. API 37 instrumentation compares library values with qualified platform resources.

Framework tests cover Compose previews, theme changes, fallback behavior, Views cancellation and shared listeners, and generated Data Binding layouts. Artifact checks cover all four publications and exported binding adapter metadata. Current validation results and release steps are in [releasing.md](releasing.md).

Emulator results do not cover every OEM overlay, wallpaper style, or contrast setting. The differing fixed/raw day/night regression is based on AOSP resources; those differences were not visible with the emulator's active wallpaper.
