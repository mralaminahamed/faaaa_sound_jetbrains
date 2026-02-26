# Faaaa Sound

<!-- Plugin description -->
**Faaaa Sound** is a JetBrains IDE plugin that plays a dramatic "Faaaaaaah" sound when tests fail.
<!-- Plugin description end -->

## Features

- Plays `faaah.mp3` when test runs report failing tests
- Falls back to speech synthesis if audio playback fails
- Manual trigger via Tools menu

## Commands

- **Tools > Faaaa Sound: Play Now** - Manually trigger the sound
- **Tools > Faaaa Sound: Self Test** - Test the extension

## Platform Notes

- macOS: uses `afplay` for audio, `say` for speech
- Linux: tries `ffplay`, then `mpg123`, then `spd-say`/`espeak`
- Windows: uses PowerShell media playback, System.Speech for synthesis

## Development

1. Open project in IntelliJ IDEA
2. Run `./gradlew runIde` to test in sandbox
3. Run `./gradlew buildPlugin` to build the plugin

## Build

```bash
./gradlew buildPlugin
```

Install from `build/distributions/*.zip` in IntelliJ IDEA Settings > Plugins > Install from disk.
