package com.vugame.starter.game

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.hypot
import kotlin.random.Random

enum class Scene { PLAYING, PAUSED, GAME_OVER }

data class Star(val position: Offset, val radius: Float, val alpha: Float)
data class Enemy(var position: Offset, val radius: Float, val speed: Float)
data class Particle(var position: Offset, var velocity: Offset, var life: Float, val size: Float, val color: Color)

class GameViewModel {
    val state = GameState()
    private var viewport = Offset(1080f, 1920f)

    fun resize(width: Float, height: Float) {
        if (width <= 0 || height <= 0) return
        viewport = Offset(width, height)
        state.player = state.player.copy(x = state.player.x.coerceIn(32f, width - 32f), y = state.player.y.coerceIn(32f, height - 32f))
    }

    fun movePlayer(delta: Offset) {
        state.player = Offset(
            (state.player.x + delta.x * 1.8f).coerceIn(32f, viewport.x - 32f),
            (state.player.y + delta.y * 1.8f).coerceIn(32f, viewport.y - 32f)
        )
    }

    fun togglePause() { if (state.scene != Scene.GAME_OVER) state.scene = if (state.scene == Scene.PAUSED) Scene.PLAYING else Scene.PAUSED }
    fun restart() { state.reset(viewport) }

    fun update(dt: Float) {
        if (dt <= 0f || state.scene != Scene.PLAYING) return
        state.time += dt
        state.score += (dt * 10).toInt()
        state.enemies.forEach { enemy ->
            enemy.position = enemy.position.copy(y = enemy.position.y + enemy.speed * dt)
            if (enemy.position.y > viewport.y + enemy.radius) {
                enemy.position = Offset(Random.nextFloat() * viewport.x, -enemy.radius)
                state.score += 10
            }
            if (distance(enemy.position, state.player) < enemy.radius + state.playerRadius) state.scene = Scene.GAME_OVER
        }
        state.particles.removeAll { it.life <= 0f }
        state.particles.forEach { particle -> particle.life -= dt; particle.position += particle.velocity * dt }
        if (Random.nextFloat() < dt * 12f) state.particles += Particle(
            state.player, Offset(Random.nextFloat() * 80f - 40f, Random.nextFloat() * 80f - 40f), 1f, Random.nextFloat() * 5f + 3f, Color(0xFFB8F2FF)
        )
    }

    private fun distance(a: Offset, b: Offset) = hypot(a.x - b.x, a.y - b.y)
}

class GameState {
    var scene by mutableStateOf(Scene.PLAYING)
    var player by mutableStateOf(Offset(540f, 1500f))
    var score by mutableIntStateOf(0)
    var time by mutableFloatStateOf(0f)
    val playerRadius = 30f
    val stars = mutableStateListOf<Star>()
    val enemies = mutableStateListOf<Enemy>()
    val particles = mutableStateListOf<Particle>()

    init { reset(Offset(1080f, 1920f)) }

    fun reset(viewport: Offset) {
        scene = Scene.PLAYING; score = 0; time = 0f
        player = Offset(viewport.x / 2f, viewport.y * .78f)
        stars.clear(); repeat(90) { stars += Star(Offset(Random.nextFloat() * viewport.x, Random.nextFloat() * viewport.y), Random.nextFloat() * 3f + 1f, Random.nextFloat() * .65f + .35f) }
        enemies.clear(); repeat(4) { enemies += Enemy(Offset(Random.nextFloat() * viewport.x, -Random.nextFloat() * viewport.y), 20f + Random.nextFloat() * 8f, 100f + Random.nextFloat() * 100f) }
        particles.clear()
    }
}
