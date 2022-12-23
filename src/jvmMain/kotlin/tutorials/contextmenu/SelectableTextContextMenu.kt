package tutorials.contextmenu

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(title = "Context menu") {
    SelectionContainer {
        Text("Hello World!")
    }
}