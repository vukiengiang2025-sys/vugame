# VuGame

Minimal Android 2D game starter built with Kotlin, Jetpack Compose and Canvas.

## Included
- Frame-based game loop using `awaitFrame()` with delta time.
- Touch/drag input for a mobile player character.
- Procedural starfield, enemies, particles and simple effects.
- No external images or paid assets: visuals are drawn with Canvas and are easy to replace.
- Small project structure ready for adding sprites, sound, menus and levels.

## Run
Open the repository in Android Studio (JDK 17), sync Gradle and run the `app` configuration on an Android device or emulator.

## Extend
- Add sprites to `app/src/main/res/drawable` and replace the Canvas primitives with `ImageBitmap`.
- Add audio under `app/src/main/res/raw` and use `SoundPool` for short effects.
- Move rules into `game/`, add collision detection and introduce screens in Compose.
- Keep game updates in `GameViewModel.update(dt)` and rendering in `GameCanvas`.

The sample intentionally uses procedural assets so the repository stays small and license-safe.
