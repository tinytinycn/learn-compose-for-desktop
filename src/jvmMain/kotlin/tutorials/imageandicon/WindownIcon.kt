package tutorials.imageandicon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val icon = painterResource("sample.png")
    Window(
        onCloseRequest = ::exitApplication,
        icon = icon
    ) {
        Box(Modifier.paint(icon).fillMaxSize())
    }
}