package tutorials.imageandicon

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.singleWindowApplication

/**
 * 从 resources 加载图像
 *
 * `painterResource()` 支持 raster 格式（BMP、GIF、HEIF、ICO、JPEG、PNG、WBMP、WebP）和矢量格式（SVG、XML 矢量绘图）。
 * */
@Preview
fun main() = singleWindowApplication {
    Image(
        painter = painterResource("sample.png"),
        contentDescription = "Sample",
        modifier = Modifier.fillMaxSize()
    )
}