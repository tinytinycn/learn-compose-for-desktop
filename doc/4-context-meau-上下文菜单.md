# Compose for Desktop 中的上下文菜单

## 涵盖的内容

在本教程中，我们将介绍使用 Compose UI 框架的上下文菜单的所有方面。

## 默认上下文菜单

对 `TextField` 和 `Selectable` text 有开箱即用的上下文菜单支持。

要为 `TextField` 启用标准上下文菜单，您只需将它放在 `DesktopMaterialTheme` 中：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(title = "Context menu") {
    val text = remember { mutableStateOf("Hello!") }
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(text = "Input") }
    )
}
```

</details>

<details><summary>图片🖼️</summary>

![context-menu-textfield](https://user-images.githubusercontent.com/5963351/190021028-c207164d-df04-4294-ad8f-da3106c16fb6.png)

</details>

`TextField` 的标准上下文菜单包含以下基于文本选择的项目：复制、剪切、粘贴、全选。

为 `Text` 组件启用标准上下文菜单是类似的——你只需要让它可选择(selectable)：

<details><summary>代码☕️</summary><p>

```kotlin
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(title = "Context menu") {
    SelectionContainer {
        Text("Hello World!")
    }
}
```

</p></details>

`text` 的上下文菜单仅包含复制操作：

<details><summary>图片🖼️</summary>

![context-menu-text](https://user-images.githubusercontent.com/5963351/190020951-0cc539a2-f698-4e2b-bc20-9d4aa1b11c6f.png)

</details>

## 用户定义的上下文菜单

要为 `TextField` 和 `Text` 组件启用额外的上下文菜单项，使用 `ContextMenuDataProvider` 和 `ContextMenuItem` 元素：

<details><summary>代码☕️</summary><p>

```kotlin
import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(title = "Context menu") {
    val text = remember { mutableStateOf("Hello!") }
    Column {
        ContextMenuDataProvider(
            items = {
                listOf(
                    ContextMenuItem("User-defined Action") {/*do something here*/ },
                    ContextMenuItem("Another user-defined action") {/*do something else*/ }
                )
            }
        ) {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = "Input") }
            )

            Spacer(Modifier.height(16.dp))

            SelectionContainer {
                Text("Hello World!")
            }
        }
    }
}
```

</p></details>

在此示例中，`Text/TextField` 上下文菜单将扩展为两个附加项：

<details><summary>图片🖼️</summary>

![context-menu-user-defined](https://user-images.githubusercontent.com/5963351/190020831-9b87b191-a351-4f70-a726-d5a53577ad53.png)

</details>

## 任意区域的上下文菜单

可以为任意应用程序窗口区域创建上下文菜单。这是使用类似于 `ContextMenuDataProvider` 的 `ContextMenuArea` API 实现的。

<details><summary>代码☕️</summary><p>

```kotlin
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(title = "Context menu") {
    ContextMenuArea(items = {
        listOf(
            ContextMenuItem("User-defined Action") {/*do something here*/},
            ContextMenuItem("Another user-defined action") {/*do something else*/}
        )
    }) {
        Box(modifier = Modifier.background(Color.Blue).height(100.dp).width(100.dp))
    }
}
```

</p></details>

右键单击蓝色方块将显示包含两个项目的上下文菜单：

<details><summary>图片🖼️</summary>

![contextMenuArea](https://user-images.githubusercontent.com/5963351/190020592-15e851f8-e356-413c-b5c3-225393712292.png)

</details>

## 设置上下文菜单样式

上下文菜单的样式不符合 `MaterialTheme`。要更改其颜色，您应该覆盖 `LocalContextMenuRepresentation`：

<details><summary>代码☕️</summary><p>

```kotlin
import androidx.compose.foundation.DarkDefaultContextMenuRepresentation
import androidx.compose.foundation.LightDefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication {
    isSystemInDarkTheme()
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
    ) {
        val contextMenuRepresentation = if (isSystemInDarkTheme()) {
            DarkDefaultContextMenuRepresentation
        } else {
            LightDefaultContextMenuRepresentation
        }
        CompositionLocalProvider(LocalContextMenuRepresentation provides contextMenuRepresentation) {
            Surface(Modifier.fillMaxSize()) {
                Box {
                    var value by remember { mutableStateOf("") }
                    TextField(value, { value = it })
                }
            }
        }
    }
}
```

</p></details>

<details><summary>图片🖼️</summary>

![context-menu-style](https://user-images.githubusercontent.com/5963351/190514663-d345a0ba-0b4c-4920-b6cd-743a753d7d83.png)

</details>

## 自定义文本的上下文菜单组件

您可以覆盖应用程序中所有文本和文本字段的文本菜单，覆盖 `TextContextMenu`：

<details><summary>代码☕️</summary><p>

```kotlin
import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import java.awt.Dimension
import java.net.URLEncoder
import java.nio.charset.Charset
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
    val panel = ComposePanel()
    panel.setContent {
        CustomTextMenuProvider {
            Column {
                SelectionContainer {
                    Text("Hello, Compose!")
                }

                var text by remember { mutableStateOf("") }

                TextField(text, { text = it })
            }
        }
    }

    val window = JFrame()
    window.contentPane.add(panel)
    window.size = Dimension(800, 600)
    window.isVisible = true
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextMenuProvider(content: @Composable () -> Unit) {
    val textMenu = LocalTextContextMenu.current
    val uriHandler = LocalUriHandler.current
    CompositionLocalProvider(
        LocalTextContextMenu provides object : TextContextMenu {
            @Composable
            override fun Area(
                textManager: TextContextMenu.TextManager,
                state: ContextMenuState,
                content: @Composable () -> Unit
            ) {
                // Here we reuse the original TextContextMenu, but add an additional item to item on the bottom.
                ContextMenuDataProvider({
                    val shortText = textManager.selectedText.crop()
                    if (shortText.isNotEmpty()) {
                        val encoded = URLEncoder.encode(shortText, Charset.defaultCharset())
                        listOf(ContextMenuItem("Search $shortText") {
                            uriHandler.openUri("https://google.com/search?q=$encoded")
                        })
                    } else {
                        emptyList()
                    }
                }) {
                    textMenu.Area(textManager, state, content = content)
                }
            }
        },
        content = content
    )
}

private fun AnnotatedString.crop() = if (length <= 5) toString() else "${take(5)}..."
```

</p></details>

<details><summary>图片🖼️</summary>

![custom-text-context-menu](https://user-images.githubusercontent.com/5963351/190509388-92cff018-2880-4cfe-95c4-4c023ecac09d.png)

</details>

## 与 Swing 的互操作性

如果您将 Compose 嵌入到现有应用程序中，您可能希望文本上下文菜单看起来与应用程序的其他部分相同。为此，有 `JPopupTextMenu`：

<details><summary>代码☕️</summary><p>

```kotlin
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.JPopupTextMenu
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.platform.LocalLocalization
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.CTRL_DOWN_MASK
import java.awt.event.KeyEvent.META_DOWN_MASK
import javax.swing.Icon
import javax.swing.JFrame
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.KeyStroke.getKeyStroke
import javax.swing.SwingUtilities
import org.jetbrains.skiko.hostOs

fun main() = SwingUtilities.invokeLater {
val panel = ComposePanel()
panel.setContent {
JPopupTextMenuProvider(panel) {
Column {
SelectionContainer {
Text("Hello, Compose!")
}

                var text by remember { mutableStateOf("") }

                TextField(text, { text = it })
            }
        }
    }

    val window = JFrame()
    window.contentPane.add(panel)
    window.size = Dimension(800, 600)
    window.isVisible = true
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JPopupTextMenuProvider(owner: Component, content: @Composable () -> Unit) {
val localization = LocalLocalization.current
CompositionLocalProvider(
LocalTextContextMenu provides JPopupTextMenu(owner) { textManager, items ->
JPopupMenu().apply {
textManager.cut?.also {
add(
swingItem(localization.cut, Color.RED, KeyEvent.VK_X, it)
)
}
textManager.copy?.also {
add(
swingItem(localization.copy, Color.GREEN, KeyEvent.VK_C, it)
)
}
textManager.paste?.also {
add(
swingItem(localization.paste, Color.BLUE, KeyEvent.VK_V, it)
)
}
textManager.selectAll?.also {
add(JPopupMenu.Separator())
add(
swingItem(localization.selectAll, Color.BLACK, KeyEvent.VK_A, it)
)
}

                // Here we add other items that can be defined additionaly in the other places of the application via ContextMenuDataProvider
                for (item in items) {
                    add(
                        JMenuItem(item.label).apply {
                            addActionListener { item.onClick() }
                        }
                    )
                }
            }
        },
        content = content
    )
}

private fun swingItem(
label: String,
color: Color,
key: Int,
onClick: () -> Unit
) = JMenuItem(label).apply {
icon = circleIcon(color)
accelerator = getKeyStroke(key, if (hostOs.isMacOS) META_DOWN_MASK else CTRL_DOWN_MASK)
addActionListener { onClick() }
}

private fun circleIcon(color: Color) = object : Icon {
override fun paintIcon(c: Component?, g: Graphics, x: Int, y: Int) {
g.create().apply {
this.color = color
translate(16, 2)
fillOval(0, 0, 16, 16)
}
}

    override fun getIconWidth() = 16

    override fun getIconHeight() = 16
}
```

</p></details>

<details><summary>图片🖼️</summary>

![context-menu-by-swing](https://user-images.githubusercontent.com/5963351/191312702-f455ab2c-4c47-4e11-b615-fc67af1af3f9.png)

</details>