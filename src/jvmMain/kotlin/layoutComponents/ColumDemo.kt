package layoutComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ColumnDemo() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween, // child排列方式
        horizontalAlignment = Alignment.CenterHorizontally //child对齐方式
    ) {
        Spacer(Modifier.height(30.dp))
        Box(Modifier.size(40.dp, 80.dp).background(Color.Magenta))
        Spacer(Modifier.height(80.dp))
        Box(Modifier.size(20.dp, 150.dp).background(Color.Blue))
        Spacer(Modifier.height(50.dp))
        Box(Modifier.size(30.dp, 60.dp).background(Color.Green))
        Spacer(Modifier.height(20.dp))
    }
}