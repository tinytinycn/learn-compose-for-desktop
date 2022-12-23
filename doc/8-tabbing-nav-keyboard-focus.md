# Tab 导航和键盘焦点

## 涵盖内容

在本教程中，我们将向您展示如何通过键盘快捷键 `tab` 和 `shift + tab` 在组件之间使用 Tab 制表符导航。

## 默认 Next/Previous tabbing 导航

默认情况下，`Next/Previous` tabbing 导航按组合顺序（按出现顺序）移动焦点，要了解其工作原理，
我们可以使用一些默认情况下可聚焦的组件：`TextField`、`OutlinedTextField`、`BasicTextField`、带有 `Modifier.clickable` 的组件。（`Button`, `IconButton`, `MenuItem`）。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

fun main() = application {
    Window(
        state = WindowState(size = DpSize(350.dp, 500.dp)),
        onCloseRequest = ::exitApplication
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(50.dp)
            ) {
                for (x in 1..5) {
                    val text = remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = text.value,
                        singleLine = true,
                        onValueChange = { text.value = it }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![default-tab-nav](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Tab_Navigation/default-tab-nav.gif)

</details>

要使不可聚焦的组件 focusable 可聚焦，您需要对组件应用 `Modifier.focusable()` 修饰符。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.background
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.DpSize

fun main() = application {
    Window(
        state = WindowState(size = DpSize(350.dp, 450.dp)),
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(
                primary = Color(10, 132, 232),
                secondary = Color(150, 232, 150)
            )
        ) {
            val clicks = remember { mutableStateOf(0) }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(40.dp)
                ) {
                    Text(text = "Clicks: ${clicks.value}")
                    Spacer(modifier = Modifier.height(20.dp))
                    for (x in 1..5) {
                        FocusableBox("Button $x", { clicks.value++ })
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableBox(
    text: String = "",
    onClick: () -> Unit = {},
    size: IntSize = IntSize(200, 35)
) {
    val keyPressedState = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = if (interactionSource.collectIsFocusedAsState().value) {
        if (keyPressedState.value)
            lerp(MaterialTheme.colors.secondary, Color(64, 64, 64), 0.3f)
        else
            MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.primary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .size(size.width.dp, size.height.dp)
            .onPointerEvent(PointerEventType.Press) { onClick() }
            .onPreviewKeyEvent {
                if (
                    it.key == Key.Enter ||
                    it.key == Key.Spacebar
                ) {
                    when (it.type) {
                        KeyEventType.KeyDown -> {
                            keyPressedState.value = true
                        }
                        KeyEventType.KeyUp -> {
                            keyPressedState.value = false
                            onClick.invoke()
                        }
                    }
                }
                false
            }
            .focusable(interactionSource = interactionSource),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White)
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![fouscable-button](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Tab_Navigation/focusable-button.gif)

</details>

## 自定义导航顺序

要以自定义顺序移动焦点，我们需要创建一个 `FocusRequester` 并将 `Modifier.focusOrder` 修饰符应用于您要导航的每个组件。

- `FocusRequester` 发送更改焦点的请求。
- `Modifier.focusOrder` 用于指定自定义的焦点遍历顺序。

在下面的示例中，我们只是创建一个 `FocusRequester` 列表并为列表中的每个 `FocusRequester` 创建文本字段。
当以相反顺序使用 `shift + tab` 或 `tab` 键盘快捷键时，每个文本字段都会向列表中的上一个和下一个文本字段发送焦点请求。

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

fun main() = application {
    Window(
        state = WindowState(size = DpSize(350.dp, 500.dp)),
        onCloseRequest = ::exitApplication
    ) {
        val itemsList = remember { List(5) { FocusRequester() } }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(50.dp)
            ) {
                itemsList.forEachIndexed { index, item ->
                    val text = remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = text.value,
                        singleLine = true,
                        onValueChange = { text.value = it },
                        modifier = Modifier.focusOrder(item) {
                            // reverse order
                            next = if (index - 1 < 0) itemsList.last() else itemsList[index - 1]
                            previous = if (index + 1 == itemsList.size) itemsList.first() else itemsList[index + 1]
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![reverse-order](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Tab_Navigation/reverse-order.gif)

</details>

## 使组件可聚焦 focused

为了使组件获得焦点，我们需要创建一个 `FocusRequester` 并将 `Modifier.focusRequester` 修饰符应用到您想要关注的组件上。
使用 `FocusRequester`，我们可以请求焦点，如下例所示：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.WindowSize
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun main() = application {
    Window(
        state = WindowState(size = WindowSize(350.dp, 450.dp)),
        onCloseRequest = ::exitApplication
    ) {
        val buttonFocusRequester = remember { FocusRequester() }
        val textFieldFocusRequester = remember { FocusRequester() }
        val focusState = remember { mutableStateOf(false) }
        val text = remember { mutableStateOf("") }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(50.dp)
            ) {
                Button(
                    onClick = {
                        focusState.value = !focusState.value
                        if (focusState.value) {
                            textFieldFocusRequester.requestFocus()
                        } else {
                            buttonFocusRequester.requestFocus()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(buttonFocusRequester)
                        .focusable()
                ) {
                    Text(text = "Focus switcher")
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = text.value,
                    singleLine = true,
                    onValueChange = { text.value = it },
                    modifier = Modifier
                        .focusRequester(textFieldFocusRequester)
                )
            }
        }
    }
}
```

</details>

<details><summary>图片🖼️</summary>

![focus-switcher](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Tab_Navigation/focus-switcher.gif)

</details>

## 已知问题

### Tab 键导航在多行 TextField 中不起作用

```kotlin
Column {
    repeat(5) {
        var text by remember { mutableStateOf("Hello, World!") }

        OutlinedTextField(
            value = text,
            singleLine = false, // Pay attention here! Also, by default, singleLine is false.
            onValueChange = { text = it },
            modifier = Modifier.padding(8.dp)
        )
    }
}
```

当用户按下“Tab”键时，焦点不会切换到下一个可聚焦组件。而是添加制表符。

#### 一个可能的解决方法

[Issues/109](https://github.com/JetBrains/compose-jb/issues/109#issuecomment-1161705265) 中提到了此解决方法。编写自定义 `Modifier.moveFocusOnTab`：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication {
    Column {
        repeat(5) {
            var text by remember { mutableStateOf("Hello, World!") }

            OutlinedTextField(
                value = text,
                singleLine = false, // Pay attention here! Also, by default, singleLine is false.
                onValueChange = { text = it },
                modifier = Modifier.padding(8.dp).moveFocusOnTab()
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.moveFocusOnTab() = composed {
    val focusManager = LocalFocusManager.current
    onPreviewKeyEvent {
        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
            focusManager.moveFocus(
                if (it.isShiftPressed) FocusDirection.Previous else FocusDirection.Next
            )
            true
        } else {
            false
        }
    }
}
```

</details>
