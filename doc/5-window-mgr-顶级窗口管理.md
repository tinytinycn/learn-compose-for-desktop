# 顶级窗口管理

## 涵盖的内容

在本教程中，我们将向您展示如何使用 Compose for Desktop 处理窗口。

我们以适合 Compose 风格的状态操作的形状表示窗口状态，并自动将其映射到操作系统窗口状态。

顶级窗口可以在其他 composable 函数中有条件地创建，并且它们的窗口管理器状态也可以使用 `rememberWindowState()` 函数生成的状态进行操作。

## 打开和关闭窗口

创建窗口的主要函数是 `Window`。此函数应在 Composable scope 作用域内使用。创建 Composable scope 作用域的最简单方法是使用 `application` 函数：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        // Content
    }
}
```

</details>

`Window` 是一个 Composable 函数，这意味着您可以以声明的方式更改其属性：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    var fileName by remember { mutableStateOf("Untitled") }

    Window(onCloseRequest = ::exitApplication, title = "$fileName - Editor") {
        Button(onClick = { fileName = "note.txt" }) {
            Text("Save")
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![window_properties](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/window_properties.gif)

</details>

## 打开和关闭窗口（有条件地）

您还可以使用简单的 `if` 语句关闭/打开窗口。

当 `Window` 离开组合时（`isPerformingTask` 变为 `false`）—— native 原生窗口自动关闭。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay

fun main() = application {
    var isPerformingTask by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // Do some heavy lifting
        isPerformingTask = false
    }

    if (isPerformingTask) {
        Window(onCloseRequest = ::exitApplication) {
            Text("Performing some tasks. Please wait!")
        }
    } else {
        Window(onCloseRequest = ::exitApplication) {
            Text("Hello, World!")
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![window-api-new](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/window_splash.gif)

</details>

如果窗口在关闭时需要一些自定义逻辑（例如，显示对话框），您可以使用 `onCloseRequest` 覆盖关闭操作。

我们没有使用命令式方法来关闭窗口 (`window.close()`)，而是使用声明式方法 - 关闭窗口以响应状态更改 (`isOpen = false`)..

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    var isAskingToClose by remember { mutableStateOf(false) }

    if (isOpen) {
        Window(
            onCloseRequest = { isAskingToClose = true }
        ) {
            if (isAskingToClose) {
                Dialog(
                    onCloseRequest = { isAskingToClose = false },
                    title = "Close the document without saving?",
                ) {
                    Button(
                        onClick = { isOpen = false }
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![ask-to-close](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/ask_to_close.gif)

</details>





## 将窗口隐藏到托盘中

如果你不需要关闭窗口，只需要隐藏它（比如隐藏到托盘），你可以改变 `windowState.isVisible` 状态：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay

fun main() = application {
    var isVisible by remember { mutableStateOf(true) }

    Window(
        onCloseRequest = { isVisible = false },
        visible = isVisible,
        title = "Counter",
    ) {
        var counter by remember { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            while (true) {
                counter++
                delay(1000)
            }
        }
        Text(counter.toString())
    }

    if (!isVisible) {
        Tray(
            TrayIcon,
            tooltip = "Counter",
            onAction = { isVisible = true },
            menu = {
                Item("Exit", onClick = ::exitApplication)
            },
        )
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![hide-instead-of-close](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/hide_instead_of_close.gif)

</details>

## 打开和关闭多个窗口

如果一个应用程序有多个窗口，那么最好将其状态放入一个单独的类中，并打开/关闭窗口以响应 `mutableStateListOf` 的更改（请参阅记事本示例以了解更复杂的用例）：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val applicationState = remember { MyApplicationState() }

    for (window in applicationState.windows) {
        key(window) {
            MyWindow(window)
        }
    }
}

@Composable
private fun ApplicationScope.MyWindow(
    state: MyWindowState
) = Window(onCloseRequest = state::close, title = state.title) {
    MenuBar {
        Menu("File") {
            Item("New window", onClick = state.openNewWindow)
            Item("Exit", onClick = state.exit)
        }
    }
}

private class MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()

    init {
        windows += MyWindowState("Initial window")
    }

    fun openNewWindow() {
        windows += MyWindowState("Window ${windows.size}")
    }

    fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String
    ) = MyWindowState(
        title,
        openNewWindow = ::openNewWindow,
        exit = ::exit,
        windows::remove
    )
}

private class MyWindowState(
    val title: String,
    val openNewWindow: () -> Unit,
    val exit: () -> Unit,
    private val close: (MyWindowState) -> Unit
) {
    fun close() = close(this)
}
```

</details>

<details><summary>图片🖼️</summary>

![multiple-windows](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/multiple_windows.gif)

</details>

## 函数 singleWindowApplication

有一个用于创建单窗口应用程序的简化函数：

```kotlin
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication {
    // Content
}
```

在以下情况下使用它：

- 您的应用程序只有一个窗口
- 您不需要自定义关闭逻辑
- 您不需要在创建窗口后更改窗口参数

## 自适应窗口大小

有时我们想整体显示一些内容，但事先并不知道究竟会显示什么，这意味着我们不知道它的最佳窗口尺寸。
通过将窗口的 `WindowSize` 的一个或两个维度设置为 `Dp.Unspecified`，Compose for Desktop 将自动调整该维度的窗口初始大小以适应其内容：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = Dp.Unspecified, height = Dp.Unspecified),
        title = "Adaptive",
        resizable = false
    ) {
        Column(Modifier.background(Color(0xFFEEEEEE))) {
            Row {
                Text("label 1", Modifier.size(100.dp, 100.dp).padding(10.dp).background(Color.White))
                Text("label 2", Modifier.size(150.dp, 200.dp).padding(5.dp).background(Color.White))
                Text("label 3", Modifier.size(200.dp, 300.dp).padding(25.dp).background(Color.White))
            }
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![adaptive-windows](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/adaptive.png)

</details>

## 更改窗口的状态（最大化、最小化、全屏、大小、位置）

native 原生窗口的某些状态已移至单独的 API 类 `WindowState`。您可以在回调中更改其属性或在可组合项中观察它。
当某些状态发生变化时（窗口大小或位置），Composable 函数将自动重新组合。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(onCloseRequest = ::exitApplication, state) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    state.placement == WindowPlacement.Fullscreen,
                    {
                        state.placement = if (it) {
                            WindowPlacement.Fullscreen
                        } else {
                            WindowPlacement.Floating
                        }
                    }
                )
                Text("isFullscreen")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    state.placement == WindowPlacement.Maximized,
                    {
                        state.placement = if (it) {
                            WindowPlacement.Maximized
                        } else {
                            WindowPlacement.Floating
                        }
                    }
                )
                Text("isMaximized")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(state.isMinimized, { state.isMinimized = !state.isMinimized })
                Text("isMinimized")
            }

            Text(
                "Position ${state.position}",
                Modifier.clickable {
                    val position = state.position
                    if (position is WindowPosition.Absolute) {
                        state.position = position.copy(x = state.position.x + 10.dp)
                    }
                }
            )

            Text(
                "Size ${state.size}",
                Modifier.clickable {
                    state.size = state.size.copy(width = state.size.width + 10.dp)
                }
            )
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![window-state](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/state.gif)

</details>

## 监听窗口的状态

当您需要更新 UI 时，读取组合中的状态很有用，但在某些情况下，您需要对状态更改做出反应并将值发送到另一个不可组合级别的应用程序（例如，将其写入数据库） :

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun main() = application {
    val state = rememberWindowState()

    Window(onCloseRequest = ::exitApplication, state) {
        // Content

        LaunchedEffect(state) {
            snapshotFlow { state.size }
                .onEach(::onWindowResize)
                .launchIn(this)

            snapshotFlow { state.position }
                .filter { it.isSpecified }
                .onEach(::onWindowRelocate)
                .launchIn(this)
        }
    }
}

private fun onWindowResize(size: DpSize) {
    println("onWindowResize $size")
}

private fun onWindowRelocate(position: WindowPosition) {
    println("onWindowRelocate $position")
}
```

</details>

## 对话框 Dialogs

有两种类型的窗口——模态窗口和常规窗口。Below are the functions for creating each:

1. Window - 常规窗口类型。
2. Dialog - 模态窗口类型。这种类型会锁定其父窗口，直到用户完成使用它并关闭模态窗口。

您可以在下面看到两种类型窗口的示例。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        var isDialogOpen by remember { mutableStateOf(false) }

        Button(onClick = { isDialogOpen = true }) {
            Text(text = "Open dialog")
        }

        if (isDialogOpen) {
            Dialog(
                onCloseRequest = { isDialogOpen = false },
                state = rememberDialogState(position = WindowPosition(Alignment.Center))
            ) {
                // Dialog's content
            }
        }
    }
}
```

</details>

## 与 Swing 的互操作性

因为 Compose for Desktop 在底层使用 Swing，所以可以直接使用 Swing 创建一个窗口：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.ui.awt.ComposeWindow
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
    ComposeWindow().apply {
        size = Dimension(300, 300)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        setContent {
            // Content
        }
        isVisible = true
    }
}
```

</details>

您还可以在 Composable window scope作用域内访问 `ComposeWindow`：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.singleWindowApplication
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetAdapter
import java.awt.dnd.DropTargetDropEvent

fun main() = singleWindowApplication {
    LaunchedEffect(Unit) {
        window.dropTarget = DropTarget().apply {
            addDropTargetListener(object : DropTargetAdapter() {
                override fun drop(event: DropTargetDropEvent) {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    val fileName = event.transferable.getTransferData(DataFlavor.javaFileListFlavor)
                    println(fileName)
                }
            })
        }
    }
}
```

</details>

如果您需要一个在 Swing 中实现的对话框，您可以将其包装到一个 Composable 函数中：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.application
import java.awt.FileDialog
import java.awt.Frame

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }

    if (isOpen) {
        FileDialog(
            onCloseRequest = {
                isOpen = false
                println("Result $it")
            }
        )
    }
}

@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)
```

</details>

## 可拖动的窗口区域 Draggable window area

如果你的窗口是未装饰的，你想给它添加一个自定义的可拖动标题栏（或使整个窗口可拖动），你可以使用 `DraggableWindowArea`：

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, undecorated = true) {
        WindowDraggableArea {
            Box(Modifier.fillMaxWidth().height(48.dp).background(Color.DarkGray))
        }
    }
}
```

请注意，`WindowDraggableArea` 只能在 `singleWindowApplication`、`Window` 和 `Dialog` 内部使用。如果您需要在另一个可组合函数中使用它，请将 `WindowScope` 作为接收器传递到那里：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, undecorated = true) {
        AppWindowTitleBar()
    }
}

@Composable
private fun WindowScope.AppWindowTitleBar() = WindowDraggableArea {
    Box(Modifier.fillMaxWidth().height(48.dp).background(Color.DarkGray))
}
```

</details>

<details><summary>图片🖼️</summary>

![draggable-area](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Window_API_new/draggable_area.gif)

</details>

## 透明窗口（例如允许制作自定义窗体的窗口）

要创建透明窗口，将两个参数传递给 Window 函数就足够了：`transparent=true` 和 `undecorate=true`（装饰透明窗口是不可能的）。
常见的场景是将透明窗口与自定义窗体的 Surface 组合在一起。下面是一个圆角窗口的示例。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.Text
import androidx.compose.runtime.*

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    if (isOpen) {
        Window(
            onCloseRequest = { isOpen = false },
            title = "Transparent Window Example",
            transparent = true,
            undecorated = true, //transparent window must be undecorated
        ) {
            Surface(
                modifier = Modifier.fillMaxSize().padding(5.dp).shadow(3.dp, RoundedCornerShape(20.dp)),
                color = Color(55, 55, 55),
                shape = RoundedCornerShape(20.dp) //window has round corners now
            ) {
                Text("Hello World!", color = Color.White)
            }
        }
    }
}
```

</details>

> **重要说明**：窗口透明度是基于 JDK 实现实现的，它包含 Linux 上已知的问题，以防在具有不同分辨率的两个显示器之间移动窗口。
> 因此，当您移动应用程序时，窗口不再透明。而且在 Compose 端似乎对这种情况无能为力。[An issue about it](https://github.com/JetBrains/compose-jb/issues/1339)