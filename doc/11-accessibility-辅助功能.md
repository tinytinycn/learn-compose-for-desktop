# 辅助功能支持

## 平台支持

| platform | status    |
|----------|-----------|
| MacOs    | Supported |
| Windows  | Supported with Java Access Bridge          |
| Linux    | Not supported          |

## 具有语义规则的自定义小部件

```kotlin
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*

fun main() = singleWindowApplication(
    title = "Custom Button", state = WindowState(size = DpSize(300.dp, 200.dp))
) {
    var count by remember { mutableStateOf(0) }

    Box(modifier = Modifier.padding(50.dp)) {
        Box(modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .clickable { count += 1 }
            .semantics(mergeDescendants = true /* Use text from the contents (1) */) {
                // This is a button (2)
                role = Role.Button
                // Add some help text to button (3)
                contentDescription = "Click to increment value"
            }
        ) {
            val text = when (count) {
                0 -> "Click Me!"
                1 -> "Clicked"
                else -> "Clicked $count times"
            }
            Text(text, modifier = Modifier.align(Alignment.Center), fontSize = 24.sp)
        }
    }
}
```

<details><summary>图片🖼️</summary>

![custom-widget](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Accessibility/images/custom-widget.png)

</details>

## Windows 操作系统

Windows 上的辅助功能由 `Java Access Bridge` 提供，默认情况下处于禁用状态。要启用它，请在命令提示符中运行以下命令。

`%JAVA_HOME%\bin\jabswitch.exe /enable`

Windows 上的 HiDPI 显示支持存在一些问题，有关详细信息，请参阅 [Windows 上的桌面辅助功能](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Accessibility/Windows.md)。