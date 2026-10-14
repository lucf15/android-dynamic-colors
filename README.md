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

## Contents

[See it](#see-it) · [Install](#install) · [First color](#your-first-dynamic-color) · [Compose](#compose) · [Fragments](#views-and-fragments) · [Custom Views](#custom-views) · [Data Binding](#xml-data-binding) · [Full palette](#palette-tones) · [Compatibility](#platform-behavior) · [Sample](#sample-app) · [Contributing](#development) · [License](#license)

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
| Accent 1, 2, 3 | `system_accent1_`, `system_accent2_`, `system_accent3_` | API 31 |
| Neutral 1, 2 | `system_neutral1_`, `system_neutral2_` | API 31 |
| Error | `system_error_` | API 35 |

Every ramp contains **13 tones**: `T0`, `T10`, `T20`, `T30`, `T40`, `T50`, `T60`, `T70`, `T80`, `T90`, `T95`, `T99`, `T100`. There are **65 raw colors on API 31–34** and **78 on API 35+**. `error` is null before API 35; missing tones/families are not synthesized.

Tone numbers increase with lightness. Android resource suffixes run in the opposite direction: suffix = `(100 - tone) × 10`, so `Tone.T40` corresponds to `_600`. The [sample explorer](sample/README.md) shows every available swatch and copies its resource name and ARGB value when tapped.

Pass `darkTheme = true/false` to `rememberDynamicPaletteOrNull` when previewing a specific resource mode. Raw ramps follow the requested configuration and can differ by mode on Android 17. In Views, use `collectDynamicPalette` or `observeDynamicPalette` for updates.

<details>
<summary><strong>Explore the palette in the sample</strong></summary>

<img src="docs/images/system-palette.png" width="300" alt="Palette explorer with tone numbers and hexadecimal color values">

Tap a swatch to copy its Android resource name and ARGB value. The explorer shows every family available on the device.

</details>

## Platform behavior

### Know which colors you’re reading

The library reads Android’s system resources. The device’s selected palette and Android version determine the values; it does not generate a palette from an arbitrary seed.

`dynamicColorsOrNull(context)` returns complete `light` and `dark` sets, each with **48 semantic roles**. `ColorRoles` has named ARGB properties and indexed access with `ColorRole`; the Compose `DynamicColorScheme` exposes the same roles as native `Color` properties.

| Android version | Semantic role source |
| --- | --- |
| Below API 31 | Unavailable; readers return null |
| API 31–33 | Real palette stops; nearest available stops for missing surface tones, baseline error colors |
| API 34–36 | Platform semantic resources, with inverse/tint aliases and black scrim where dedicated resources are unavailable |
| API 37+ | Dedicated inverse, tint, and scrim resources; light/dark contexts resolved independently |

These are platform values, not a promise of identical output to every Material 3 version. See the [source review](docs/review.md) and [platform behavior](docs/platform-behavior.md). Core snapshots retain no Context. Direct readers perform fresh reads; framework adapters own refresh behavior.

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
