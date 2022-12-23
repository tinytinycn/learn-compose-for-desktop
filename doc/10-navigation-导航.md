# Navigation 导航

## General attitude 一般态度

Jetpack Compose 导航库 ([navigation-compose](https://developer.android.com/jetpack/compose/navigation)) 是一个仅限 Android
的库，因此不能与 Compose for Desktop
一起使用。我们的一般态度不是“强迫”人们使用特定的第一方库。但是有可用的第三方库。可以考虑 [Decompose](https://github.com/arkivanov/Decompose) 作为可能的解决方案。

## Patterns 模式

导航 Navigation 不仅仅是关于切换子组件 components 和管理后台堆栈 back stack 。它还可能影响应用程序的架构。

Compose 中有两种常见的导航模式：导航逻辑可以在 `@Composable` 世界内部或外部进行保存和管理。每种方法都有其优点和缺点，所以请明智地决定。

本教程描述了这两种模式，如何在它们之间进行选择，以及 Decompose 库如何提供帮助。

## 一. Prerequisites 先决条件

本教程使用一个非常简单的 List-Details 应用程序示例，它只有两个屏幕：ItemList 和 ItemDetails。我们需要首先做的事情很少。

### 1 安装

首先让我们将 Decompose 库添加到项目中。请参阅文档的 [入门](https://arkivanov.github.io/Decompose/getting-started/) 部分。

### 2 Item 模型 和 数据库

这是我们需要的 `Item` 数据类：

```kotlin
data class Item(val id: Long, val text: String)
```

还有一个简单的数据库接口，将被子屏幕 child screens 使用（为了简单起见，没有并发）：

```kotlin
interface Database {
    fun getAll(): List<Item>
    fun getById(id: Long): Item
}
```

### 3 child screens 的基础 UI

我们将需要一些用于列表 `List` 和详细信息 `Details` 屏幕的基本 UI。

`ItemListScreen`` @Composable` 组件显示 `Items` 列表并在单击 item 时调用 `onItemClick` 回调：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ItemListScreen(items: List<Item>, onItemClick: (id: Long) -> Unit) {
    LazyColumn {
        items(items = items) { item ->
            Text(
                text = item.text,
                modifier = Modifier.clickable { onItemClick(item.id) }
            )
        }
    }
}
```

</details>

`ItemDetailsScreen` `@Composable` 组件显示之前选中的 `Item` 并在点击 `TopAppBar` 中的返回按钮时调用 `onBackClick` 回调：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun ItemDetailsScreen(item: Item, onBackClick: () -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Item details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        Text(text = item.text)
    }
}
```

</details>

### 4 Children configuration 子配置

Decompose 库的主要目标之一是编译时安全。每个 child 都由一个名为 `Configuration` 的类来描述。 配置的目的是描述应该使用哪些 child 以及它有什么参数。因此，对于每个 child 来说，都有一个自己的
Configuration 类的实例。 通常导航涉及多个子级，因此，整套配置通常是一个密封类。

例如，对于一个简单的 List-Details 导航，我们只需要两个条目：

```kotlin
import com.arkivanov.decompose.statekeeper.Parcelable

sealed class Configuration : Parcelable {
    object List : Configuration()
    data class Details(val itemId: Long) : Configuration()
}
```

这种方法看起来有点冗长，但它在以下情况下带来了编译时的安全性：

- Child 参数在编译时进行验证（与通过字符串、`Bundles` 等传递参数不同）。
- 可以详尽地检查配置，因此如果没有涵盖所有子项，则编译将失败。

#### 4.1 Android 中的 Parcelable 配置

Desktop Compose 实际上是一个多平台库，也可以在 Android 中使用。这也使得共享导航逻辑成为可能。但 Android 对导航有额外的要求 -
后台堆栈应该能够在 [配置更改](https://developer.android.com/guide/topics/resources/runtime-changes) 后继续存在。 一般来说，当此类事件发生时，应该保存和恢复后退堆栈。

为了使这成为可能，所有子配置都必须是 [Parcelable](https://developer.android.com/reference/android/os/Parcelable) 。为方便起见，Decompose 使用
[expect/actual](https://kotlinlang.org/docs/reference/mpp-connect-to-apis.html) 定义 `Parcelable` 和 `@Parcelize`：

- `Parcelable` - 此接口由 `commonMain` 源集中的 Decompose 定义。它是针对 Android target 的 Android `Parcelable` 接口类型化的，在所有其他目标（包括
  JVM/Desktop）中只是一个空接口。
- `@Parcelize` - 此注释也在 `commonMain` 源集中定义。它被类型化为 [kotlin-parcelize](https://developer.android.com/kotlin/parcelize)插件提供的
  `@Parcelize` 注解。并且在 non-Android target 中缺少（因为不需要）。

如果您需要 Android 支持，请确保您已启用 `kotlin-parcelize` 插件。所有配置应如下所示：

```kotlin
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize

sealed class Configuration : Parcelable {
    @Parcelize
    object List : Configuration()

    @Parcelize
    data class Details(val itemId: Long) : Configuration()
}
```

## 二. 在 @Composable 世界之外管理导航

如果以下任何一项适用，则应选择此模式：

1. 您支持具有不同 UI 框架的 Multipaltform targets，并且您希望在它们之间共享导航逻辑。例如，如果您支持带有 Compose UI 的桌面、带有 SwiftUI 的 iOS 和/或带有 React UI 的
   JavaScript。
2. 您想让 children 在后堆栈中运行（停止，但未销毁）。
3. 您的目标是 Android 并且需要在 children（又名 AndroidX ViewModels）中保留实例功能，并且您希望将此逻辑隐藏为实现细节。
4. 您希望将导航逻辑（可能还有业务逻辑）与 UI 分开。

第一点很明显。如果 Compose 不是您使用的唯一 UI，并且您希望共享导航逻辑，则 Compose 无法对其进行管理。

第二点在 Desktop 中可能特别有用。当一个 children 被 push 推到后堆栈中，它会停止但不会被销毁。所以它在没有 UI 的情况下一直在“后台”运行。这使得在导航时将 children 的状态保存在内存中成为可能。

第三点是关于实例保留的，比如AndroidX ViewModels，主要用于Android。它允许在发生 Android 配置更改并重新创建整个导航堆栈时保留（保留在内存中）一些数据。在这种模式中，实例保留最重要的优点是它被封装在
children 中作为实现细节。

第四点不是那么明显，但可能非常重要。将导航和业务逻辑与用户界面分离可以提高可测试性。例如。可以通过简单的 JUnit 测试来测试非 UI 代码。 UI 也可以使用其他测试框架单独测试。

您可以在 TodoApp 示例中找到一些集成测试：

- [TodoMainTest](https://github.com/JetBrains/compose-jb/blob/master/examples/todoapp/common/main/src/commonTest/kotlin/example/todo/common/main/integration/TodoMainTest.kt)
    - 主屏幕的集成测试。
- [TodoRootTest](https://github.com/JetBrains/compose-jb/blob/master/examples/todoapp/common/root/src/commonTest/kotlin/example/todo/common/root/integration/TodoRootTest.kt)
    - 用于在主屏幕和编辑屏幕之间导航的集成测试。

Decompose 库鼓励这种模式。如果这是您的选择，那么您可以使用其推荐的方法。

主要思想是通过多个组件拆分（分解）您的项目。组件可以以树状结构组织，每个级别可以（但不是必须）有多个 [路由器](https://arkivanov.github.io/Decompose/router/overview/)
。每个组件只是一个普通的接口/类，是底层逻辑的入口点。

用户界面的 **唯一职责** 是监听组件的状态变化并触发它们的事件。

以下资源可以帮助解决此模式:

- Decompose [文档](https://arkivanov.github.io/Decompose/)
- [TodoApp](https://github.com/JetBrains/compose-jb/tree/master/examples/todoapp) 案例
- 文章 "[Fully cross-platform Kotlin applications (almost)](https://proandroiddev.com/fully-cross-platform-kotlin-applications-almost-29c7054f8f28)"

### 一个非常基本的例子：

`ItemList` child 的 UI：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class ItemList(
    database: Database, // Accept the Database as dependency
    val onItemSelected: (itemId: Long) -> Unit // Called on item click
) {
    // No concurrency involved just for simplicity. The state can be updated if needed.
    private val _state = mutableStateOf(database.getAll())
    val state: State<List<Item>> = _state
}

@Composable
fun ItemListUi(list: ItemList) {
    ItemListScreen(
        items = list.state.value,
        onItemClick = list.onItemSelected
    )
}
```

</details>

`ItemDetails` child 的 UI：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class ItemDetails(
    itemId: Long, // An item id to be loaded and displayed
    database: Database, // Accept the Database as dependency
    val onFinished: () -> Unit // Called on TopAppBar back button click
) {
    // No concurrency involved just for simplicity. The state can be updated if needed.
    private val _state = mutableStateOf(database.getById(id = itemId))
    val state: State<Item> = _state
}

@Composable
fun ItemDetailsUi(details: ItemDetails) {
    ItemDetailsScreen(
        item = details.state.value,
        onBackClick = details.onFinished
    )
}
```

</details>

带导航的 Root（假设仅使用 Compose UI）：

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class Root(
    componentContext: ComponentContext, // In Decompose each component has its own ComponentContext
    private val database: Database // Accept the Database as dependency
) : ComponentContext by componentContext {

    private val router =
        router<Configuration, Content>(
            initialConfiguration = Configuration.List, // Starting with List
            childFactory = ::createChild // The Router calls this function, providing the child Configuration and ComponentContext 
        )

    val routerState = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {
            is Configuration.List -> list()
            is Configuration.Details -> details(configuration)
        } // Configurations are handled exhaustively

    private fun list(): Content =
        ItemList(
            database = database, // Supply dependencies
            onItemSelected = { router.push(Configuration.Details(itemId = it)) } // Push Details on item click
        ).asContent { ItemListUi(it) }

    private fun details(configuration: Configuration.Details): Content =
        ItemDetails(
            itemId = configuration.itemId, // Safely pass arguments
            database = database, // Supply dependencies
            onFinished = router::pop // Go back to List
        ).asContent { ItemDetailsUi(it) }
}

@Composable
fun RootUi(root: Root) {
    Children(root.routerState) { child ->
        child.instance()
    }
}
```

</details>

Application 和 Root 的初始化

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent

fun main() = singleWindowApplication(
    title = "Navigation tutorial"
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        MaterialTheme {
            DesktopTheme {
                RootUi(root()) // Render the Root and its children
            }
        }
    }
}

@Composable
private fun root(): Root =
    // The rememberRootComponent function provides the root ComponentContext and remembers the instance or Root
    rememberRootComponent { componentContext ->
        Root(
            componentContext = componentContext,
            database = DatabaseImpl() // Supply dependencies
        )
    }
```

</details>

## 二. 在 @Composable 世界之内管理导航

通过使用这种模式，导航逻辑在 `@Composable` 函数中得以保存和管理。例如，Jetpack Compose `navigation-compose` 库使用此模式。 在实践中，
通常有一个像 `@Composable fun Navigator(...)` 或 `@Composable fun NavHost(...)` 这样的函数来管理后台堆栈 back stack 并呈现当前活动的 child
。函数如何呈现子元素的方式取决于它的 API。

如果您更喜欢使用 Compose (不仅仅用于 UI 之外)，则应选择此模式，并且第一个模式的要点都不适用。

Decompose 没有提供任何开箱即用的 `@Composable` 导航 API。但是用它来编写你自己的很容易。您可以试验并提出自己的 API。

实现细节请参考以下文章：[A-comprehensive-hundred-line-navigation-for-Jetpack/Desktop-Compose](https://proandroiddev.com/a-comprehensive-hundred-line-navigation-for-jetpack-desktop-compose-5b723c4f256e)
。它还解释了一些附加功能，如后退按钮处理、过渡动画等。

### 一个非常基本的例子：

```kotlin
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.statekeeper.Parcelable

@Composable
inline fun <reified C : Parcelable> rememberRouter(
    noinline initialConfiguration: () -> C
): Router<C, Any> =
    TODO("See the article mentioned above for the implementation")
```

首先，我们需要 Decompose 库中的路由器。一旦我们有了它，我们需要做的就是使用 `Children` 函数。 `Children` 函数侦听 `Router`
状态变化，并使用提供的回调呈现当前活动的 child 。上面提到的文章解释了实现细节。

使用 `Router`:

<details><summary>代码☕️</summary>

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.composenavigatorexample.navigator.rememberRouter
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push


@Composable
fun ItemList(
    database: Database,
    onItemClick: (itemId: Long) -> Unit
) {
    // No concurrency involved just for simplicity. The state can be updated if needed.
    val items = remember { mutableStateOf(database.getAll()) }

    ItemListScreen(
        items = items.value,
        onItemClick = onItemClick
    )
}

@Composable
fun ItemDetails(
    itemId: Long,
    database: Database,
    onBackClick: () -> Unit
) {
    // No concurrency involved just for simplicity. The state can be updated if needed.
    val item = remember { mutableStateOf(database.getById(id = itemId)) }

    ItemDetailsScreen(
        item = item.value,
        onBackClick = onBackClick
    )
}

@Composable
fun Root(database: Database) {
    // Create and remember the Router
    val router =
        rememberRouter<Configuration>(
            initialConfiguration = { Configuration.List } // Start with the List screen
        )

    // Render children
    Children(routerState = router.state) { screen ->
        when (val configuration = screen.configuration) {
            is Configuration.List ->
                ItemList(
                    database = database, // Supply dependencies
                    onItemClick = { router.push(Configuration.Details(itemId = it)) } // Push Details on item click
                )

            is Configuration.Details ->
                ItemDetails(
                    itemId = configuration.itemId, // Safely pass arguments
                    database = database, // Supply dependencies
                    onBackClick = router::pop // Go back to List
                )
        }.let {} // Ensure exhaustiveness
    }
}
```

</details>