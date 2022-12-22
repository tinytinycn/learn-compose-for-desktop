package layoutComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SpacerDemo() {
    Row {
        Box(Modifier.size(100.dp).background(Color.Red))
        Spacer(Modifier.width(20.dp))
        Box(Modifier.size(100.dp).background(Color.Magenta))
        Spacer(Modifier.weight(1f))
        Box(Modifier.size(100.dp).background(Color.Black))
    }
}