# VuGame

VuGame is now a practical Android 2D game starter, inspired by patterns commonly used by libGDX, Korge, Godot and native Compose game samples. It does **not** copy third-party repositories or assets; it provides a small, license-safe core that can be extended.

## Included

- Frame loop using Compose `withFrameNanos` and capped delta time.
- Responsive viewport sizing for portrait or landscape.
- Scene state: playing, paused and game over.
- Touch drag input with clamped player movement.
- Enemy spawning, movement and circle collision detection.
- Score system and restart flow.
- Procedural starfield and particle effects.
- Clean separation between `MainActivity` rendering/UI and `game/GameEngine.kt` state/rules.
- Asset and licensing guidance.

## Run

Open in Android Studio with JDK 17, sync Gradle, and run `app` on an Android device or emulator.

## Architecture

```text
app/src/main/java/com/vugame/starter/
├── MainActivity.kt       # Compose UI, renderer and input
└── game/GameEngine.kt    # loop-facing state, entities, rules and collisions
```

## How to extend

1. Add a `Scene` implementation for menus, platformer or top-down games.
2. Replace Canvas primitives with `ImageBitmap` sprites from `res/drawable`.
3. Add `SoundPool` for short effects and `MediaPlayer` for music.
4. Add fixed-timestep physics when deterministic simulation is needed.
5. Add unit tests around collision and scoring before adding complex gameplay.
6. Add Google Play Games services only when achievements or leaderboards are required.

See `ASSETS.md` and `THIRD_PARTY_NOTICES.md` before adding downloaded assets.
