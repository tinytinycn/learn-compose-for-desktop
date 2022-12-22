package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MenuDemo() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(20.dp, (-20).dp)
        ) {
            DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                Text("刷新")
            }
            DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                Text("设置")
            }
            Divider()
            DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                Text("发送反馈")
            }
        }
    }
}