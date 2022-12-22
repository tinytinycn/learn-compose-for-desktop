package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@Composable
fun NavigationDrawerDemo() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            // 表示抽屉内内容的可组合
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
                onClick = { coroutineScope.launch { drawerState.close() } },
                content = { Text("关闭抽屉") }
            )
        },
        drawerShape = MaterialTheme.shapes.small,
        content = {
            // 抽屉剩余部分UI内容
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = if (drawerState.isClosed) ">>> 划动 >>>" else "<<< 划动 <<<")
                Spacer(Modifier.height(20.dp))
                Button(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Text("点击打开")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun NavigationDrawerDemo2() {
    val (gesturesEnabled, toggleGesturesEnabled) = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
                .toggleable(value = gesturesEnabled, onValueChange = toggleGesturesEnabled)
        ) {
            Checkbox(gesturesEnabled, null)
            Text(text = if (gesturesEnabled) "手势已开启" else "手势已关闭")
        }
        // 底部抽屉状态
        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        BottomDrawer(
            gesturesEnabled = gesturesEnabled,
            drawerState = drawerState,
            drawerContent = {
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
                    onClick = { scope.launch { drawerState.close() } },
                    content = { Text("关闭抽屉") }
                )
                LazyColumn {
                    items(25) {
                        ListItem(
                            text = { Text("Item $it") },
                            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) }
                        )
                    }
                }
            },
            content = {
                // 这个例子可以清晰地看出，哪些属于是抽屉内容的其他部分UI
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val openText = if (gesturesEnabled) "▲▲▲ 拉起来 ▲▲▲" else "点击按钮!"
                    Text(text = if (drawerState.isClosed) openText else "▼▼▼ 拽下去 ▼▼▼")
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { scope.launch { drawerState.open() } }) {
                        Text("打开抽屉")
                    }
                }
            }
        )
    }
}