package com.vugame.starter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.vugame.starter.game.GameState
import com.vugame.starter.game.GameViewModel
import kotlinx.coroutines.android.awaitFrame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val model = remember { GameViewModel() }
            var lastFrame by remember { mutableLongStateOf(0L) }
            LaunchedEffect(Unit) {
                while (true) { val now = awaitFrame(); val dt = if (lastFrame == 0L) 0f else ((now - lastFrame) / 1_000_000_000f).coerceAtMost(.05f); lastFrame = now; model.update(dt) }
            }
            GameCanvas(model.state, model::movePlayer)
        }
    }
}

@Composable
private fun GameCanvas(state: GameState, onMove: (Offset) -> Unit) {
    val density = LocalDensity.current
    Canvas(Modifier.fillMaxSize().pointerInput(Unit) {
        detectDragGestures { change, drag -> change.consume(); onMove(drag) }
    }) {
        drawRect(Color(0xFF101526))
        state.stars.forEach { drawCircle(Color(0xFF8FA8D8), it.radius, it.position) }
        state.particles.forEach { drawCircle(it.color.copy(alpha = it.life), it.size, it.position) }
        drawCircle(Color(0xFF64D8FF), state.playerRadius, state.player)
        drawCircle(Color.White.copy(alpha = .35f), state.playerRadius * .55f, state.player - Offset(5f, 5f))
        state.enemies.forEach { drawCircle(Color(0xFFFF5C7A), it.radius, it.position) }
    }
}
