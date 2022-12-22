# 滚动和滚动条

## 涵盖的内容

在本教程中，我们将向您展示如何使用 Compose for Desktop 的桌面特定组件，例如 scrollbars 和 tooltips 。

## Scrollbars 滚动条

您可以将滚动条应用于可滚动组件。滚动条和可滚动组件共享一个公共状态以相互同步。
例如，`VerticalScrollbar` 可以附加到 `Modifier.verticalScroll` 、`LazyColumn` ; `HorizontalScrollbar`
可以附加到 `Modifier.horizontalScroll` 、 `LazyRow`。

```kotlin
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Scrollbars",
    state = WindowState(width = 250.dp, height = 400.dp)
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(180, 180, 180))
            .padding(10.dp)
    ) {
        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ) {
            Column {
                for (item in 0..30) {
                    TextBox("Item #$item")
                    if (item < 30) {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp),
            adapter = rememberScrollbarAdapter(stateHorizontal)
        )
    }
}

@Composable
fun TextBox(text: String = "Item") {
    Box(
        modifier = Modifier.height(32.dp)
            .width(400.dp)
            .background(color = Color(200, 0, 0, 20))
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text)
    }
}
```

可以通过拖动滚动条并使用鼠标滚轮或触摸板来移动滚动条。可以通过侧击滚轮或按住 `Shift` 键来使用鼠标滚轮进行水平滚动。

![scrollbars](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Desktop_Components/scrollbars.gif)

## 带有 Scrollbar 的 lazy 可滚动组件

您可以将滚动条与 lazy 可滚动组件一起使用，例如 `LazyColumn`。

```kotlin
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scrollbars",
        state = rememberWindowState(width = 250.dp, height = 400.dp)
    ) {
        LazyScrollable()
    }
}

@Composable
fun LazyScrollable() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(180, 180, 180))
            .padding(10.dp)
    ) {

        val state = rememberLazyListState()

        LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp), state) {
            items(1000) { x ->
                TextBox("Item #$x")
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}

@Composable
fun TextBox(text: String = "Item") {
    Box(
        modifier = Modifier.height(32.dp)
            .fillMaxWidth()
            .background(color = Color(0, 0, 0, 20))
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text)
    }
}
```

![lazy-scrollbar](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Desktop_Components/lazy_scrollbar.gif)

## Tooltips 工具提示

您可以使用 `TooltipArea` 向任何组件添加工具提示。 `TooltipArea` 类似于 `Box`，但具有显示工具提示的能力。

`TooltipArea` 函数的主要参数：

- tooltip - 表示工具提示的可组合内容
- tooltipPlacement - 描述如何放置工具提示。您可以指定锚点（鼠标光标或组件）、偏移量和对齐方式
- delay - 以毫秒为单位的时间延迟，之后将显示工具提示（默认为 500 毫秒）

```kotlin
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tooltip Example",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        val buttons = listOf("Button A", "Button B", "Button C", "Button D", "Button E", "Button F")
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            buttons.forEachIndexed { index, name ->
                // wrap button in BoxWithTooltip
                TooltipArea(
                    tooltip = {
                        // composable tooltip content
                        Surface(
                            modifier = Modifier.shadow(4.dp),
                            color = Color(255, 255, 210),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Tooltip for ${name}",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    },
                    modifier = Modifier.padding(start = 40.dp),
                    delayMillis = 600, // in milliseconds
                    tooltipPlacement = TooltipPlacement.CursorPoint(
                        alignment = Alignment.BottomEnd,
                        offset = if (index % 2 == 0) DpOffset(-16.dp, 0.dp) else DpOffset.Zero // tooltip offset
                    )
                ) {
                    Button(onClick = {}) { Text(text = name) }
                }
            }
        }
    }
}
```

![tooltips](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Desktop_Components/tooltips.gif)
