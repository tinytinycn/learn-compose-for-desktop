package tutorials.imageandicon

import androidx.compose.material.Text
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.singleWindowApplication

fun main() {
    val icon = BitmapPainter(useResource("sample.png", ::loadImageBitmap))
    singleWindowApplication(icon = icon) {
        Text("Hello World!")
    }
}