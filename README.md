# Rupam Keyboard

Android custom keyboard (IME) starter project built with Kotlin.

## Features
- Android Input Method Service
- Basic English and Bengali key layouts
- Number/symbol layout
- Adjustable key corner radius
- Light, dark, blue, and pink theme presets
- Saved theme preferences and live preview
- No Internet permission; typed content is not transmitted

## Build APK
1. Install Android Studio with Android SDK Platform 35.
2. Open this repository in Android Studio.
3. Allow Gradle sync.
4. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Debug APK: `app/build/outputs/apk/debug/app-debug.apk`.
6. Install on a test device, then enable Rupam Keyboard in Android keyboard settings.

## Current prototype limitations
This is not a Gboard replacement. Bengali is a basic sample layout, not phonetic transliteration. Autocorrect, word suggestions, glide typing, emoji/clipboard panels, image backgrounds, and full color pickers are not implemented. Test before daily use.


Android APK build workflow: `.github/workflows/android-apk.yml`.

Build workflow SDK setup corrected.

Kotlin/Java JVM target compatibility configured for Java 17.
