package layoutComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@ExperimentalMaterialApi
@Composable
fun ModalBottomSheetLayoutDemo() {

    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            ModalBottomSheetList()
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { scope.launch { state.show() } }) {
                Text("点击分享")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ModalBottomSheetList() {
    Column {
        ListItem(text = { Text("选择分享到哪里～") })
        ListItem(text = { Text("Github") }, icon = { Icons.Filled.Share })
        ListItem(
            text = { Text("微信") },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF07C160)
                ) {
                    Icon(
                        painter = painterResource("avatar.jpeg"),
                        contentDescription = "wx",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp, 16.dp).padding(4.dp)
                    )
                }
            },
            modifier = Modifier.clickable { }
        )
    }
}