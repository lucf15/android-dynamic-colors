<p align="center">
  <img src="docs/images/app-icon.svg" width="88" height="88" alt="Android Dynamic Colors icon">
</p>

<h1 align="center">Android Dynamic Colors</h1>

<p align="center"><strong>Your components. The user’s colors.</strong></p>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.lucf15/dynamic-colors"><img src="https://img.shields.io/maven-central/v/io.github.lucf15/dynamic-colors?label=Maven%20Central" alt="Maven Central version"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-Apache%202.0-blue" alt="Apache 2.0 license"></a>
</p>

Building a custom design system? Adding dynamic color to an existing screen? Bring Android’s system palette into the components you already own—in Compose, Views, or XML.

Read a single accent, theme an entire screen, or explore every available tone. You control the shapes, typography, and layout; Android Dynamic Colors supplies the color values and framework-specific update APIs.

**All 48 color roles are available from Android 12**, including roles whose platform resources arrived later. On Android 12–13, the library derives those roles from the system palette, with approximations for some surface shades. [See exactly what is backported →](#android-12-and-13-the-role-backport)

## Contents

[See it](#see-it) · [Colors explained](#colors-explained) · [Install](#install) · [First color](#your-first-dynamic-color) · [Compose](#compose) · [Fragments](#views-and-fragments) · [Custom Views](#custom-views) · [Data Binding](#xml-data-binding) · [Full palette](#palette-tones) · [Android 12 backport](#android-12-and-13-the-role-backport) · [Compatibility](#platform-behavior) · [Sample](#sample-app) · [Contributing](#development) · [License](#license)

## See it

**The same components, with a palette that follows the device.**

| System violet · light | System violet · dark |
| :---: | :---: |
| <img src="docs/images/components-violet-light.png" width="280" alt="Component showcase with violet system colors in light mode"> | <img src="docs/images/components-violet-dark.png" width="280" alt="The same components with violet system colors in dark mode"> |

<details>
<summary><strong>Compare Custom blue, System teal, and System terracotta</strong></summary>

| Palette | Light | Dark |
| --- | :---: | :---: |
| Custom · blue | <img src="docs/images/components-custom-light.png" width="260" alt="Custom blue palette in light mode"> | <img src="docs/images/components-custom-dark.png" width="260" alt="Custom blue palette in dark mode"> |
| System · teal | <img src="docs/images/components-teal-light.png" width="260" alt="Teal system palette in light mode"> | <img src="docs/images/components-teal-dark.png" width="260" alt="Teal system palette in dark mode"> |
| System · terracotta | <img src="docs/images/components-terracotta-light.png" width="260" alt="Terracotta system palette in light mode"> | <img src="docs/images/components-terracotta-dark.png" width="260" alt="Terracotta system palette in dark mode"> |

The Custom palette belongs to the sample. System screenshots use emulator seeds `#6750A4`, `#006A6A`, and `#A35139` with Tonal Spot. The library reads the resulting Android resources; it does not generate palettes from seeds.

</details>

- **Keep your design system.** Use native Compose `Color` values or Android ARGB integers in your own components. No Material UI dependency is required.
- **Choose the level you need.** Read 48 named roles per mode, or pick from 13 tones in each available color family.
- **Fit the framework.** Remember colors in Compose, collect them in a Fragment coroutine, or bind them directly in XML.
- **Keep your app colors as a fallback.** System colors are available on Android 12+. Older devices can use the palette you already ship.

## Colors explained

### A palette gives you shades; a role tells you where to use one

| Concept | Example | What it means |
| --- | --- | --- |
| **Palette family** | `Accent1`, `Neutral2` | A set of related shades from Android’s system resources. |
| **Tone** | `Tone.T40` | One shade in a family, ordered by lightness. A tone is not a UI purpose. |
| **Semantic role** | `primary`, `onPrimary`, `surface` | A color assigned to a purpose in a light or dark UI. |
| **Theme attribute** | `?attr/colorPrimary` | A value resolved from an Android XML theme. This library does not populate your theme attributes. |
| **Data Binding attribute** | `app:dynamicTextColor="@{ColorRole.OnSurface}"` | An adapter that reads a library role and applies it to one View. |

For example, the Android 12–13 compatibility mapping uses **Accent 1, tone 40** for light `primary` and **Accent 1, tone 80** for dark `primary`. Reading `primary` selects a role for the requested mode. Reading `Accent1 / T40` selects that specific tone instead.

**Use roles for ordinary UI.** Pair a background role with its foreground role: `primary` with `onPrimary`, `primaryContainer` with `onPrimaryContainer`, or `surface` with `onSurface`. The `on` prefix means text or icons drawn on that color. Validate contrast in your actual components, especially if you mix roles or use transparency.

**Use palette tones for your own color system.** Pick a family and tone when you need to map Android’s colors into your own tokens. A raw tone does not choose a matching foreground or adjust itself to the purpose of your component.

### What the library changes in your app

The core returns color values. Compose helpers expose them as state and through `DynamicColorsTheme`; Views helpers deliver updates to your callback; Data Binding adapters apply them to the specified View properties.

It does **not** replace your XML theme, set `?attr/colorPrimary`, install Compose `MaterialTheme`, or recolor every widget automatically. Apply the returned values in your components, or connect them to your existing theme. `ColorRole.Primary` is the enum identifier; `colors.primary` is the resolved color value. Neither is an Android resource ID.

### Choose the API for the job

| You need | Compose | Views | Core snapshot |
| --- | --- | --- | --- |
| Roles for one light/dark mode | `rememberDynamicColorSchemeOrNull()` | `collectDynamicColorRoles` / `observeDynamicColorRoles` | Read `.light` or `.dark` from `dynamicColorsOrNull(context)` |
| Both light and dark role sets | `rememberDynamicColorsOrNull()` | `collectDynamicColors` / `observeDynamicColors` | `dynamicColorsOrNull(context)` |
| Raw families and tones | `rememberDynamicPaletteOrNull()` | `collectDynamicPalette` / `observeDynamicPalette` | `dynamicPaletteOrNull(context)` |

Core readers return fresh, immutable snapshots and do not observe changes. Framework adapters handle updates. Compose schemes and `.color(...)` extensions return Compose `Color`; core roles and ramps contain Android ARGB `Int` values.

## Install

Add `google()` and `mavenCentral()` to your repositories, then choose the module for your UI:

| Integration | Dependency | Minimum Android |
| --- | --- | --- |
| Compose | `io.github.lucf15:dynamic-colors-compose:0.1.0` | API 23 |
| Views / View Binding | `io.github.lucf15:dynamic-colors-views:0.1.0` | API 21 |
| XML Data Binding | `io.github.lucf15:dynamic-colors-databinding:0.1.0` | API 21 |
| Color values only | `io.github.lucf15:dynamic-colors:0.1.0` | API 21 |

For example, in your app’s `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.lucf15:dynamic-colors-compose:0.1.0")
}
```

Each UI module includes the core. Add multiple adapters if your app mixes frameworks, and keep them on the same version. **The Compose artifact requires compile SDK 37.** The core has no Compose, Views adapter, or coroutine dependency.

## Your first dynamic color

Start with one component. This Compose label uses the device’s primary color, falling back to your own blue when system colors are unavailable:

```kotlin
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.lucf15.dynamiccolors.compose.rememberDynamicColorSchemeOrNull

@Composable
fun AccentLabel() {
    val colors = rememberDynamicColorSchemeOrNull()
    BasicText(
        text = "Made for your palette",
        color = colors?.primary ?: Color(0xFF2563EB),
    )
}
```

The helper follows light/dark mode and refreshes when the context or resource configuration changes. For a whole screen, provide a theme once.

## Compose

### Give your components a shared palette

Supply your app’s light and dark fallback schemes, then read colors anywhere inside `DynamicColorsTheme`:

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.lucf15.dynamiccolors.compose.DynamicColorScheme
import io.github.lucf15.dynamiccolors.compose.DynamicColorsTheme

@Composable
fun AppTheme(
    lightFallback: DynamicColorScheme,
    darkFallback: DynamicColorScheme,
    useSystemColors: Boolean = true,
    content: @Composable () -> Unit,
) {
    val dark = isSystemInDarkTheme()
    DynamicColorsTheme(
        fallback = if (dark) darkFallback else lightFallback,
        darkTheme = dark,
        useDynamicColors = useSystemColors,
        content = content,
    )
}

@Composable
fun StatusCard() {
    val colors = DynamicColorsTheme.colorScheme
    Box(Modifier.background(colors.primaryContainer)) {
        BasicText("Ready", color = colors.onPrimaryContainer)
    }
}
```

`lightFallback` and `darkFallback` are your app’s `DynamicColorScheme` values. Define them once with `DynamicColorScheme { role -> /* your Compose Color */ }`, or convert core `ColorRoles` with `toComposeColorScheme()`. The [sample’s complete mapping](sample/src/main/kotlin/io/github/lucf15/dynamiccolors/sample/theme/ComposeColors.kt) shows how to connect an existing design palette.

Set `useSystemColors = false` to use your own scheme on every device—this is how the sample’s **Custom / System** switch works. Nested providers can theme individual parts of a screen. Read `DynamicColorsTheme.colorScheme` inside a provider; reading it outside one reports a setup error.

### Already have a theme?

Use `rememberDynamicColorScheme(fallback, darkTheme)` or `rememberDynamicColorSchemeOrNull(darkTheme)` inside your existing theme code. The scheme contains native Compose `Color` values, and custom schemes retain their color spaces. `rememberDynamicColorsOrNull()` exposes both core light/dark sets if you need them.

`DynamicColorsTheme` provides colors to your components; it does not install `MaterialTheme` or restyle existing widgets.

### Preview both paths

Wrap your preview in `DynamicColorsPreview { ... }` to supply sample system colors. Use `DynamicColorsPreview(colors = null, palette = null) { ... }` to check your fallback. These fixtures apply only in inspection mode.

## Views and Fragments

### Let the View lifecycle own collection

Use the suspend `collectDynamicColorRoles` extension inside `repeatOnLifecycle`. The subscription closes when collection is cancelled and starts fresh when the layout becomes active again.

Add AndroidX Fragment and Lifecycle’s `lifecycle-runtime-ktx` dependency, then:

```kotlin
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.lucf15.dynamiccolors.views.collectDynamicColorRoles
import kotlinx.coroutines.launch

class ColorsFragment : Fragment(R.layout.fragment_colors) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.title)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                view.collectDynamicColorRoles { colors ->
                    view.setBackgroundColor(colors?.surface ?: Color.WHITE)
                    title.setTextColor(colors?.onSurface ?: Color.BLACK)
                }
            }
        }
    }
}
```

<details>
<summary>Layout for this Fragment: <code>res/layout/fragment_colors.xml</code></summary>

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/app_name" />
</FrameLayout>
```

</details>

Use **`viewLifecycleOwner`**: a Fragment can outlive its layout. With View Binding, capture the current binding locally in `onViewCreated()` and clear your binding field in `onDestroyView()` as usual.

The example uses a white/black fallback to keep setup small. Use your own light/dark palette in your app. Leave `darkTheme` unset to follow the View’s configuration, or pass `true` / `false` for an explicit mode.

<details>
<summary>Collecting multiple streams and handling updates</summary>

`collectDynamicColors` provides both modes; `collectDynamicPalette` provides raw ramps. Launch each collector in its own child coroutine when you need more than one stream.

Registration, callbacks, and cleanup run on `Dispatchers.Main.immediate`, even when collection starts on another dispatcher. Callbacks may suspend and run sequentially. A slow consumer receives only the latest pending update; callback failures propagate after cleanup.

See [Android’s lifecycle guidance](https://developer.android.com/topic/libraries/architecture/views/coroutines-views).

</details>

## Custom Views

### Observe while attached, close on detach

A reusable View can own an `AutoCloseable` subscription directly:

```kotlin
package com.example.app

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import io.github.lucf15.dynamiccolors.views.observeDynamicColorRoles

class DynamicLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : TextView(context, attrs, defStyleAttr) {
    private var subscription: AutoCloseable? = null
    private val fallback = textColors

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        subscription = observeDynamicColorRoles { colors ->
            if (colors == null) setTextColor(fallback)
            else setTextColor(colors.onSurface)
        }
    }

    override fun onDetachedFromWindow() {
        subscription?.close()
        subscription = null
        super.onDetachedFromWindow()
    }
}
```

Use `<com.example.app.DynamicLabel ... />` in a layout. For a Canvas component, update your `Paint` colors and call `invalidate()` in the callback. `observeDynamicColors` and `observeDynamicPalette` provide both modes or raw ramps.

<details>
<summary>What happens when several components observe colors?</summary>

Observe and close on the main thread. Subscriptions on the same View share one attachment listener, one pending refresh, and one snapshot read per kind per refresh. Attached Views share one application configuration callback.

Each handle closes independently. Closing the last removes the View listener. Detached observers pause and can resume on reattachment; closing a handle permanently releases its View and callback references. Register once per owner—repeated calls still create separate subscriptions.

</details>

## XML Data Binding

### Put the color role next to the component

Add `dynamic-colors-databinding`, enable `android.buildFeatures.dataBinding = true`, and import `ColorRole` in your layout:

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <import type="io.github.lucf15.dynamiccolors.ColorRole" />
    </data>
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#F5F5F7"
        android:text="@string/app_name"
        android:textColor="#1B1B1F"
        app:dynamicBackgroundTint="@{ColorRole.Surface}"
        app:dynamicTextColor="@{ColorRole.OnSurface}" />
</layout>
```

These `app:dynamic…` attributes are **Data Binding expressions**, not Android theme attributes. Use them inside a `<layout>` with Data Binding enabled; writing `?attr/dynamicTextColor` in a normal layout is not equivalent.

The adapter observes colors while the View is attached. On a device without system colors, the XML text color and background tint remain your fallback. See the [compiled sample layout](sample/src/main/res/layout/dynamic_color_label.xml).

Kotlin Data Binding needs annotation processing. This project uses `com.android.legacy-kapt` with AGP 9’s built-in Kotlin; older Kotlin Android projects use their usual kapt setup.

<details>
<summary>All attributes, fallbacks, and rebinding behavior</summary>

| Attribute | Target | Value |
| --- | --- | --- |
| `dynamicTextColor` | `TextView` and subclasses | Nullable `ColorRole` |
| `dynamicBackgroundTint` | Any View with a background | Nullable `ColorRole` |
| `dynamicImageTint` | `ImageView` and subclasses | Nullable `ColorRole` |
| `dynamicDarkTheme` | Each affected View | Nullable `Boolean`; null follows configuration |
| `dynamicTextColorFallback` | `TextView` | Optional ARGB color |
| `dynamicBackgroundTintFallback` | `View` | Optional ARGB color |
| `dynamicImageTintFallback` | `ImageView` | Optional ARGB color |

A fallback can reference an app resource: `app:dynamicTextColorFallback="@{@color/my_text_fallback}"`. Transparent zero is valid. Without a fallback, unavailable system colors preserve the original color/tint state list. A dynamic role applies a single color to all widget states; setting its expression to `null` restores the original state list, including disabled/pressed colors. Tint adapters preserve existing drawables and shapes.

Repeated binding updates reuse one subscription per View. Detach unregisters configuration observation; reattachment refreshes it. Removing the last role closes the subscription. `dynamicDarkTheme` applies to that View, not its descendants. Use `binding.lifecycleOwner = viewLifecycleOwner` when your expressions read LiveData; color observation itself follows attachment. `binding.unbind()` stops expression updates but does not reset an attached View's dynamic colors.

</details>

## Palette tones

### Build your own color tokens

Named roles such as `primary`, `surface`, and `onSurface` give colors a UI purpose. Raw tones let you choose the exact shade for your own tokens—for example, a brand accent, a chart, or a custom illustration.

In Compose:

```kotlin
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.Tone
import io.github.lucf15.dynamiccolors.compose.color
import io.github.lucf15.dynamiccolors.compose.rememberDynamicPaletteOrNull

// Inside a composable:
val palette = rememberDynamicPaletteOrNull()
val accent = palette?.color(PaletteFamily.Accent1, Tone.T40)
```

For Android ARGB values, read the core directly:

```kotlin
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.Tone
import io.github.lucf15.dynamiccolors.dynamicPaletteOrNull

val palette = dynamicPaletteOrNull(context)
val accentArgb: Int? = palette?.accent1?.get(Tone.T40)
val errorArgb: Int? = palette?.get(PaletteFamily.Error)?.get(Tone.T40)
```

| Family | Android resource prefix | Available from |
| --- | --- | --- |
| Accent 1, 2, 3 | `system_accent1_`, `system_accent2_`, `system_accent3_` | Android 12 / API 31 |
| Neutral 1, 2 | `system_neutral1_`, `system_neutral2_` | Android 12 / API 31 |
| Error | `system_error_` | Android 15 / API 35 |

Every ramp contains **13 tones**: `T0`, `T10`, `T20`, `T30`, `T40`, `T50`, `T60`, `T70`, `T80`, `T90`, `T95`, `T99`, `T100`. There are **65 raw colors on API 31–34** and **78 on API 35+**. `error` is null before API 35; missing tones/families are not synthesized.

Tone numbers increase with lightness: **T0 is black, T100 is white**. `T40` means tone 40; it is not 40% opacity, and it does not select a hue. The family supplies the hue/chroma characteristics. Only the 13 listed stops are exposed—there is no `T42` or arbitrary-tone generator.

Android resource suffixes run in the opposite direction: suffix = `(100 - tone) × 10`. For example, `palette.accent1[Tone.T40]` reads `android.R.color.system_accent1_600`, while `Tone.T90` reads `system_accent1_100`. The [sample explorer](sample/README.md) shows every available swatch and copies its resource name and ARGB value when tapped.

Pass `darkTheme = true/false` to `rememberDynamicPaletteOrNull` when previewing a specific resource mode. Raw ramps follow the requested configuration and can differ by mode on Android 17. In Views, use `collectDynamicPalette` or `observeDynamicPalette` for updates.

<details>
<summary><strong>Explore the palette in the sample</strong></summary>

<img src="docs/images/system-palette.png" width="300" alt="Palette explorer with tone numbers and hexadecimal color values">

Tap a swatch to copy its Android resource name and ARGB value. The explorer shows every family available on the device.

</details>

## Platform behavior

### Availability at a glance

**Raw palette availability and role availability are separate.** A missing platform resource does not necessarily mean a missing library role.

| Android version | Raw palette | Semantic role sets |
| --- | --- | --- |
| Android 11 and earlier / API < 31 | `null` | `null`; use your app’s fallback colors |
| Android 12–13 / API 31–33 | Five families, 65 colors | All 48 roles per mode, derived from available tones; static baseline error colors |
| Android 14 / API 34 | Five families, 65 colors | All 48 roles; platform role resources plus inverse/tint/scrim compatibility mappings |
| Android 15–16 / API 35–36 | Six families, 78 colors, including Error | Same role strategy as API 34 |
| Android 17+ / API 37+ | Six families, 78 colors | All 48 roles from platform resources, including dedicated inverse/tint/scrim resources |

For example, **`colors.light.error` exists on Android 12**, but **`palette.error` is null until Android 15**. The first is a semantic role with a compatibility fallback; the second is a raw system family that we do not synthesize.

### Android 12 and 13: the role backport

Android 12–13 expose tonal palette resources but lack the newer platform semantic role resources. The library builds complete light and dark role sets from those available shades. Consumers can use `surfaceContainerLow`, `primaryFixed`, and the other roles without checking their individual platform introduction versions.

The backport uses three rules:

1. **Map roles to system tones.** For example, light `primary` uses Accent 1 / T40; dark `primary` uses Accent 1 / T80. Container, foreground, fixed, outline, and inverse roles have explicit mappings too.
2. **Use available stops for surface shades.** When the desired tone is absent, use an existing stop. We do not run a CAM16/HCT solver to generate an intermediate color.
3. **Supply baseline error colors.** The four error roles use fixed Material 3 baseline values because platform error-role resources are unavailable on these versions.

**The backport supplies missing roles using existing palette tones.** It also does not backport dynamic colors below Android 12 or add Android resources/theme attributes to the OS.

Here is the surface approximation compared with the inspected Material 3 implementation. Values are **light / dark** tones from the Neutral 2 (neutral-variant) family:

| Role | Material 3 tone | This library’s available tone |
| --- | --- | --- |
| `background`, `surface` | 98 / 6 | 99 / 10 |
| `surfaceBright` | 98 / 24 | 99 / 20 |
| `surfaceDim` | 87 / 6 | 90 / 10 |
| `surfaceContainerLowest` | 100 / 4 | 100 / 0 |
| `surfaceContainerLow` | 96 / 10 | 95 / 10 |
| `surfaceContainer` | 94 / 12 | 95 / 10 |
| `surfaceContainerHigh` | 92 / 17 | 90 / 20 |
| `surfaceContainerHighest` | 90 / 22 | 90 / 20 |

**The visual consequence:** some surface levels share the same color on Android 12–13, and dark `surfaceContainerLowest` is black. These are approximations, not exact Material 3 color generation. If your design relies on subtle separation between every surface level, check it on Android 12–13 or use your own fallback scheme there.

The complete mapping is in [DerivedColors.kt](dynamic-colors/src/main/kotlin/io/github/lucf15/dynamiccolors/internal/DerivedColors.kt).

### Android 14 and later: read platform roles

From API 34, most roles are read directly from Android’s semantic color resources. On API 34–36, `inversePrimary`, `inverseSurface`, and `inverseOnSurface` use the corresponding opposite-mode roles; `surfaceTint` uses the current mode’s `primary`, and `scrim` is black. From API 37, all five use their dedicated platform resources.

Light and dark sets are resolved with separate resource configurations. Raw palettes represent one configuration. On Android 17, even roles named `*Fixed` and raw palette tones can differ between light and dark; “fixed” is not a guarantee that Android returns identical values in both modes.

The library follows platform values where available, so its output is not guaranteed to match every Material 3 release. For example, it reads platform error roles from API 34, while the inspected AndroidX implementation keeps its baseline error colors. See [platform details](docs/platform-behavior.md) and the [AOSP/AndroidX source review](docs/review.md).

### All 48 roles

`dynamicColorsOrNull(context)` returns `light` and `dark` sets. Each exposes these properties; Compose’s `DynamicColorScheme` exposes the same names as native `Color` values.

| Group | Properties |
| --- | --- |
| Primary | `primary`, `onPrimary`, `primaryContainer`, `onPrimaryContainer` |
| Secondary | `secondary`, `onSecondary`, `secondaryContainer`, `onSecondaryContainer` |
| Tertiary | `tertiary`, `onTertiary`, `tertiaryContainer`, `onTertiaryContainer` |
| Error | `error`, `onError`, `errorContainer`, `onErrorContainer` |
| Background and surface | `background`, `onBackground`, `surface`, `onSurface`, `surfaceVariant`, `onSurfaceVariant`, `surfaceBright`, `surfaceDim` |
| Surface containers | `surfaceContainerLowest`, `surfaceContainerLow`, `surfaceContainer`, `surfaceContainerHigh`, `surfaceContainerHighest` |
| Outlines | `outline`, `outlineVariant` |
| Primary fixed | `primaryFixed`, `primaryFixedDim`, `onPrimaryFixed`, `onPrimaryFixedVariant` |
| Secondary fixed | `secondaryFixed`, `secondaryFixedDim`, `onSecondaryFixed`, `onSecondaryFixedVariant` |
| Tertiary fixed | `tertiaryFixed`, `tertiaryFixedDim`, `onTertiaryFixed`, `onTertiaryFixedVariant` |
| Inverse, tint, and scrim | `inversePrimary`, `inverseSurface`, `inverseOnSurface`, `surfaceTint`, `scrim` |

The naming describes the intended use:

- **`*Container`**: a background for content using the corresponding `on*Container` foreground. Surface container levels provide a progression for layered surfaces.
- **`*Fixed` / `*FixedDim`**: the fixed-role accent pair, with a stronger tone for `Dim`; use `on*Fixed` or `on*FixedVariant` for foreground content. On the Android 12–13 backport these use the same tone assignments in both modes. Newer Android resource values can vary by configuration.
- **`outline` / `outlineVariant`**: boundary colors with different emphasis.
- **`inverseSurface` / `inverseOnSurface` / `inversePrimary`**: colors for elements that contrast with the surrounding theme, such as an inverse-colored message surface.
- **`surfaceTint`**: a color your component may use for a surface tint. Reading it does not apply an elevation overlay.
- **`scrim`**: a color for an obscuring layer. Your component decides its opacity and when to draw it.

On API 31+, each role in a returned set has a color value. Below API 31, the whole read returns null. For raw palettes, the whole palette is null below API 31, and only its Error family is nullable on API 31–34.

## Sample app

### Try it on your own wallpaper

Open the project in Android Studio and run **sample**. Switch between **Compose / Views**, choose **Custom / System**, and toggle light/dark mode. Change the device’s system palette to see the same components take on a different set of colors.

The gallery includes buttons, text input, switches, cards, dialogs, and the full palette explorer. Both renderers use custom components. [Explore the sample source and structure →](sample/README.md)

## Development

Found a color that looks wrong on your device? [Open an issue](https://github.com/lucf15/android-dynamic-colors/issues) with the Android version, device, color role or tone, and a small reproducer. A screenshot helps show the difference you’re seeing.

To work on the library, use JDK 17 and Android SDK 37, then run:

```shell
./gradlew verify
```

This checks Kotlin formatting, library tests, lint, release builds, and sample compilation. To try unpublished changes in another app, follow the [local Maven instructions](docs/releasing.md#validate-locally).

[Contributing](CONTRIBUTING.md) · [Platform details](docs/platform-behavior.md) · [AOSP and AndroidX source validation](docs/review.md) · [Release instructions](docs/releasing.md)

## License

[Apache-2.0](LICENSE). The sample’s Inter font uses the [SIL Open Font License](sample/INTER-LICENSE.txt).
