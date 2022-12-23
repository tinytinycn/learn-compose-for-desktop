package tutorials.windows

import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay

fun main() = application {
    var isPerformingTask by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // 模拟做一些繁重的工作
        isPerformingTask = false
    }

    if (isPerformingTask) {
        Window(onCloseRequest = ::exitApplication) {
            Text("Performing some tasks. Please wait!")
        }
    } else {
        Window(onCloseRequest = ::exitApplication) {
            Text("Hello, World!")
        }
    }
}