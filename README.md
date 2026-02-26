# Faaaa Sound

<!-- Plugin description -->
**Faaaa Sound** is a JetBrains IDE plugin that plays a dramatic "Faaaaaaah" sound when tests fail.
<!-- Plugin description end -->

## Features

- Plays `faaah.mp3` when test runs report failing tests
- Falls back to speech synthesis if audio playback fails
- Manual trigger via Tools menu
- Configurable settings

## Commands

- **Tools > Faaaa Sound: Play Now** - Manually trigger the sound
- **Tools > Faaaa Sound: Self Test** - Test the extension

## Settings

Configure the plugin via **Settings > Tools > Faaaa Sound**:

| Setting | Description | Default |
|---------|-------------|---------|
| Enable Faaaa Sound plugin | Enable/disable the plugin | Enabled |
| Play sound when tests fail | Play sound when test runs fail | Enabled |
| Play sound on new errors | Play sound when new errors are detected | Disabled |
| Read error message before playing sound | Speak the error message before playing the sound | Disabled |
| Cooldown (ms) | Minimum time between sound plays | 2500 |
| Custom phrase | Custom phrase to speak (max 300 chars) | Faaaaaaah |
| Sound file | Custom sound file path (mp3/wav) | (bundled) |

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
