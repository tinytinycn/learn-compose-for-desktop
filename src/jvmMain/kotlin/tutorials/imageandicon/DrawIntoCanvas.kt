package tutorials.imageandicon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.singleWindowApplication
import org.xml.sax.InputSource

fun main() = singleWindowApplication {
    val density = LocalDensity.current // to calculate the intrinsic size of vector images (SVG, XML)

    val sample = remember {
        useResource("sample.png", ::loadImageBitmap)
    }
    val ideaLogo = remember {
        useResource("idea-logo.svg") { loadSvgPainter(it, density) }
    }
    val composeLogo = rememberVectorPainter(
        remember {
            useResource("compose-logo.xml") { loadXmlImageVector(InputSource(it), density) }
        }
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawIntoCanvas { canvas ->
            canvas.withSave {
                canvas.drawImage(sample, Offset.Zero, Paint())
                canvas.translate(sample.width.toFloat(), 0f)
                with(ideaLogo) {
                    draw(ideaLogo.intrinsicSize)
                }
                canvas.translate(ideaLogo.intrinsicSize.width, 0f)
                with(composeLogo) {
                    draw(Size(100f, 100f))
                }
            }
        }
    }
}