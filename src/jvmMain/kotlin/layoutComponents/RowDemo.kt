package layoutComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun RowDemo() {
    Column {
        Row {
            Box(Modifier.size(40.dp).background(Color.Magenta))
            Box(Modifier.size(40.dp).weight(1f).background(Color.Yellow))
            Box(Modifier.size(40.dp).weight(1f).background(Color.Green))
        }
        Divider(Modifier.height(10.dp))
        Row {
            Box(Modifier.size(40.dp).background(Color.Magenta))
            Box(Modifier.size(40.dp).background(Color.Yellow))
            Box(Modifier.size(40.dp).background(Color.Green))
        }
    }

}
