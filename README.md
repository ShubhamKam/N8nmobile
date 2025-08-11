# n8n Mobile (WebView wrapper)

This Android app wraps a WebView pointing to a locally running n8n (OSS) instance on `http://127.0.0.1:5678`.

It’s designed to be used together with Termux, which runs the n8n server on-device. The app simply provides a convenient, full-screen, app-like experience.

## Prerequisites
- Android 7.0+ (API 24+) device
- [Termux](https://f-droid.org/en/packages/com.termux/) installed from F-Droid

## Set up n8n in Termux
```bash
pkg update -y && pkg upgrade -y
# Fetch this repo or copy only the script below
bash termux_n8n_setup.sh
# Start n8n (keep Termux in foreground for first run)
~/bin/start-n8n
```
Then open the app; it loads `http://127.0.0.1:5678` in a WebView.

Tip: Use [Termux:Boot](https://f-droid.org/en/packages/com.termux.boot/) to auto-start `~/bin/start-n8n` on device boot.

## Build the APK (local)
Open the project in Android Studio (Giraffe+), install SDK 34, then:
- Build > Make Project
- Build > Build Bundle(s) / APK(s) > Build APK(s)

Or via CLI:
```bash
./gradlew :app:assembleDebug
```
Generated APK: `app/build/outputs/apk/debug/app-debug.apk`

## Build the APK (GitHub Actions)
Push this repo to GitHub and enable Actions. Workflow will attach an APK artifact to each run.

## Notes
- This project does not embed Node.js or n8n; it relies on Termux for the local server. Embedding the full n8n stack inside an APK is currently impractical due to Node.js + native deps + licensing/size constraints on Android.
- Ensure `n8n` listens on `127.0.0.1:5678` (the Termux script sets this up). The app’s network security config allows cleartext for localhost.
