package layoutComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*


@Composable
fun BottomNavigationDemo() {

    var selectedItemIndex by remember { mutableStateOf(0) }
    val items = listOf<String>("聊天", "通讯录", "发现", "我的")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.ArrowBack, null) }
                }
            )
        },
        bottomBar = {
            BottomNavigation {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index },
                        label = { Text(item) },
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Filled.Home, null)
                                1 -> Icon(Icons.Filled.Person, null)
                                2 -> Icon(Icons.Filled.Star, null)
                                else -> Icon(Icons.Filled.Settings, null)
                            }
                        }
                    )
                }
            }
        }
    ) { }

}