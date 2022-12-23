package tutorials.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(title = "Context menu") {
    ContextMenuArea(items = {
        listOf(
            ContextMenuItem("User-defined Action") {/*do something here*/},
            ContextMenuItem("Another user-defined action") {/*do something else*/}
        )
    }) {
        Box(modifier = Modifier.background(Color.Blue).height(100.dp).width(100.dp))
    }
}