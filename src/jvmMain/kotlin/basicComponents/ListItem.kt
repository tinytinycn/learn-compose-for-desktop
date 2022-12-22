package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ListItemDemo() {
    Column {
        ListItem(text = { Text("单行没有带图标的list item") })
        Divider()
        ListItem(
            text = { Text("单行带24x24图标的list item") },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) }
        )
        Divider()
        ListItem(
            text = { Text("单行带40x40图标的list item") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("单行带56x56图标的list item") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("单行可点击的list item") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            },
            modifier = Modifier.clickable { }
        )
        Divider()
        ListItem(
            text = { Text("单行带尾部图标的list item") },
            trailing = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "本地化描述"
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("单行list item") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            },
            trailing = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "本地化描述"
                )
            }
        )
        Divider()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ListItemDemo2() {
    Column {
        ListItem(
            text = { Text("两行文本的 list item") },
            secondaryText = { Text("次要文本 text") }
        )
        Divider()
        ListItem(
            text = { Text("两行文本的 list item") },
            overlineText = { Text("上划线文本 overline") }
        )
        Divider()
        ListItem(
            text = { Text("两行文本带图标的 list item") },
            secondaryText = { Text("次要文本 text") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("两行文本带40x40图标的list item") },
            secondaryText = { Text("次要文本 text") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("两行文本带40x40图标的list item") },
            secondaryText = { Text("次要文本 text") },
            trailing = { Text("meta") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        )
        Divider()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ListItemDemo3() {
    Column {
        ListItem(
            text = { Text("三行文字的list item") },
            secondaryText = {
                Text(
                    "这是当前列表项的长辅助文本，" +
                            "显示在两行"
                )
            },
            singleLineSecondaryText = false,
            trailing = { Text("meta") }
        )
        Divider()
        ListItem(
            text = { Text("三行文字的list item") },
            overlineText = { Text("上划线文本OVERLINE") },
            secondaryText = { Text("次要文本text") }
        )
        Divider()
        ListItem(
            text = { Text("三行文字带24x24图标的list item") },
            secondaryText = {
                Text(
                    "这是当前列表项的长辅助文本，" +
                            "显示在两行"
                )
            },
            singleLineSecondaryText = false,
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("三行文字带尾部图标的list item") },
            secondaryText = {
                Text(
                    "这是当前列表项的长辅助文本" +
                            " item, 显示在两行"
                )
            },
            singleLineSecondaryText = false,
            trailing = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "本地化描述"
                )
            }
        )
        Divider()
        ListItem(
            text = { Text("三行文本的list item") },
            overlineText = { Text("上划线文本OVERLINE") },
            secondaryText = { Text("次要文本text") },
            trailing = { Text("meta") }
        )
        Divider()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ListItemDemo4() {
    Column {
        var switched by remember { mutableStateOf(false) }
        val onSwitchedChange: (Boolean) -> Unit = { switched = it }
        ListItem(
            text = { Text("可切换的 ListItem") },
            trailing = {
                Switch(
                    checked = switched,
                    onCheckedChange = null // null recommended for accessibility with screenreaders
                )
            },
            modifier = Modifier.toggleable(
                value = switched,
                onValueChange = onSwitchedChange
            )
        )
        Divider()
        var checked by remember { mutableStateOf(true) }
        val onCheckedChange: (Boolean) -> Unit = { checked = it }
        ListItem(
            text = { Text("可复选的 ListItem") },
            trailing = {
                Checkbox(
                    checked = checked,
                    onCheckedChange = null // null recommended for accessibility with screenreaders
                )
            },
            modifier = Modifier.toggleable(
                value = checked,
                onValueChange = onCheckedChange
            )
        )
        Divider()
    }
}