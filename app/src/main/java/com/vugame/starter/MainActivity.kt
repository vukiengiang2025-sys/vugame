package com.vugame.starter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.withFrameNanos
import com.vugame.starter.game.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { VuGameApp() } }
}

@Composable
private fun VuGameApp() {
    val model = remember { GameViewModel() }
    LaunchedEffect(Unit) {
        var previous = 0L
        while (true) { withFrameNanos { now -> val dt = if (previous == 0L) 0f else ((now - previous) / 1_000_000_000f).coerceAtMost(.05f); previous = now; model.update(dt) } }
    }
    Box(Modifier.fillMaxSize().background(Color(0xFF101526))) {
        GameCanvas(model)
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("SCORE ${model.state.score}", color = Color.White, fontSize = 18.sp)
            Button(onClick = model::togglePause) { Text(if (model.state.scene == Scene.PAUSED) "RESUME" else "PAUSE") }
        }
        when (model.state.scene) {
            Scene.PAUSED -> CenterMessage("PAUSED", "Resume to continue", "RESUME", model::togglePause)
            Scene.GAME_OVER -> CenterMessage("GAME OVER", "Score: ${model.state.score}", "RESTART", model::restart)
            Scene.PLAYING -> Unit
        }
    }
}

@Composable
private fun GameCanvas(model: GameViewModel) {
    val state = model.state
    Canvas(Modifier.fillMaxSize().pointerInput(Unit) { detectDragGestures { change, drag -> change.consume(); model.movePlayer(drag) } }) {
        model.resize(size.width, size.height)
        state.stars.forEach { drawCircle(Color(0xFF8FA8D8).copy(alpha = it.alpha), it.radius, it.position) }
        state.particles.forEach { drawCircle(it.color.copy(alpha = it.life.coerceIn(0f, 1f)), it.size, it.position) }
        drawCircle(Color(0xFF64D8FF), state.playerRadius, state.player)
        drawCircle(Color.White.copy(alpha = .35f), state.playerRadius * .55f, state.player - Offset(5f, 5f))
        state.enemies.forEach { drawCircle(Color(0xFFFF5C7A), it.radius, it.position) }
    }
}

@Composable
private fun CenterMessage(title: String, subtitle: String, action: String, onClick: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(title, color = Color.White, fontSize = 32.sp); Text(subtitle, color = Color.LightGray, modifier = Modifier.padding(8.dp)); Button(onClick = onClick) { Text(action) }
    }
}
