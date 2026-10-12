# Contributing

Use JDK 17 and Android SDK 37. Run `./gradlew formatKotlin` after Kotlin edits and `./gradlew verify` before proposing a change. Run `./gradlew :dynamic-colors:connectedDebugAndroidTest` with an API 37+ emulator/device when changing resource reads. The instrumented tests are skipped on older APIs.

Keep the core independent of UI frameworks. Keep Material 3 test-only. Public APIs use explicit Kotlin visibility/types and KDoc. Add a regression test for behavior changes; avoid tests that only repeat a production mapping without checking an independent resource contract.

Every new Android resource needs a checked introduction API, an SDK guard, and the matching `@RequiresApi`. Read [platform behavior](docs/platform-behavior.md) before changing a Material 3 mismatch. Update that document when intentionally changing the compatibility mapping.

Do not commit build output, local SDK paths, signing keys, or credentials. Publishing is a separate, manual operation after validation; see [releasing](docs/releasing.md).

## API review

Include a consumer example with API changes. Check setup, value types, mode and fallback selection, updates, lifecycle ownership, disposal, and error handling. Shared integration logic belongs in the framework adapter.

Compose should expose native immutable `Color` values, remembered configuration reads, theme propagation, and preview support. Coroutine APIs must preserve structured cancellation, propagate callback failures, and clean up on the UI thread. Views must tolerate detach/reattach and independent subscriptions without multiplying platform listeners. Data Binding adapters must be tested through generated XML, handle repeated rebinding, and restore stateful defaults when a role is removed.

When adding an integration, migrate its sample usage and validate its published metadata as well as its source APIs. Keep framework-specific dependencies in the adapter module.
