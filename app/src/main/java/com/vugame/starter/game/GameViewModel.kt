package com.vugame.starter.game

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.sqrt
import kotlin.random.Random

class GameViewModel {
    val state = GameState()
    private var size = Offset(1080f, 1920f)
    fun movePlayer(delta: Offset) { state.player = Offset((state.player.x + delta.x * 1.8f).coerceIn(24f, size.x - 24f), (state.player.y + delta.y * 1.8f).coerceIn(24f, size.y - 24f)) }
    fun update(dt: Float) {
        if (dt <= 0f) return
        state.time += dt; state.score += (dt * 10).toInt()
        state.enemies.forEach { it.position = it.position.copy(y = it.position.y + it.speed * dt); if (it.position.y > size.y + 40) { it.position = Offset(Random.nextFloat() * size.x, -40f); state.score += 10 } }
        state.particles.removeAll { it.life <= 0f }; state.particles.forEach { it.life -= dt; it.position += it.velocity * dt }
        if (Random.nextFloat() < dt * 2.5f) state.particles += Particle(state.player, Offset(Random.nextFloat() * 80 - 40, Random.nextFloat() * 80 - 40))
    }
}

data class Star(val position: Offset, val radius: Float)
data class Enemy(var position: Offset, val radius: Float = 22f, val speed: Float = 120f)
data class Particle(var position: Offset, var velocity: Offset, var life: Float = 1f, val size: Float = 5f, val color: Color = Color(0xFFB8F2FF))

class GameState {
    var player by androidx.compose.runtime.mutableStateOf(Offset(540f, 1500f)); val playerRadius = 30f
    var score by androidx.compose.runtime.mutableIntStateOf(0); var time by androidx.compose.runtime.mutableFloatStateOf(0f)
    val stars = List(90) { Star(Offset(Random.nextFloat() * 1080f, Random.nextFloat() * 1920f), Random.nextFloat() * 3f + 1f) }
    val enemies = androidx.compose.runtime.mutableStateListOf(Enemy(Offset(180f, 300f)), Enemy(Offset(720f, 100f)), Enemy(Offset(920f, 500f)))
    val particles = androidx.compose.runtime.mutableStateListOf<Particle>()
}
