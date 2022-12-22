package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DividerDemo() {

    Column {
        Text("AAAAAAA")
        Divider(color = Color.Blue, startIndent = 80.dp)
        Text("BBBBBBB")
        Divider()
        Text("CCCCCCC")
        Divider(thickness = Dp.Hairline, startIndent = 150.dp)
        Text("DDDDDDD")
    }
}