package customComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Preview
@Composable
fun PreviewSimpleTable() {
    val headers = listOf("序号", "姓名", "操作")
    val bodyDataKey = listOf("id", "name")
    val bodyData: List<Item> = DataProvicder.testItems
    // 接收selection操作的值currentRow
    val selection: List<Int> = listOf()

    Box(
        Modifier
            .border(border = BorderStroke(Dp.Hairline, color = Color.LightGray))
            .padding(20.dp)
    ) {
        SimpleTable(
            modifier = Modifier.height(300.dp),
            row = bodyData.size,
            col = headers.size,
            border = false,
            stripe = true,
            headerData = headers,
            onRowSelected = { singleSelectionRowIndex, singleSelectionRowToggle ->
                println("row-$singleSelectionRowIndex ${if (singleSelectionRowToggle) "isSelected" else "unSelected"}")
            },
            headerRow = { col ->
                Text(text = headers[col], style = MaterialTheme.typography.subtitle1)
            }
        ) { row, col ->
            // 方式一
            //        // 取数
            //        val item = bodyData[row]
            //        val itemVal = item.asMap[bodyDataKey[col]]
            //        // 数据绑定
            //        Text(
            //            text = "" + itemVal,
            //            style = MaterialTheme.typography.body1
            //        )

            // 方式二
            when (col) {
                0 -> Text(text = bodyData[row].id.toString(), style = MaterialTheme.typography.body1)
                1 -> Text(text = bodyData[row].name, style = MaterialTheme.typography.body1)
                2 -> Checkbox(checked = bodyData[row].isSelected, onCheckedChange = null)
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleTable(
    modifier: Modifier = Modifier,
    row: Int = 0,
    col: Int = 0,
    dataRowHeight: Dp = 30.dp,
    border: Boolean = false,
    stripe: Boolean = false,
    borderWidth: Dp = Dp.Hairline,
    onRowSelected: ((Int, Boolean) -> Unit)? = { _, _ -> },
    headerData: List<String> = listOf(),
    headerRow: @Composable ((Int) -> Unit)? = null,
    dataRow: @Composable (Int, Int) -> Unit
) {
    // 滚动状态记录
    val hScrollOffset = remember { mutableStateOf(0) }
    val hItemIndex = remember { mutableStateOf(0) }
    val hScrollState = rememberLazyListState(0, 0)
    hItemIndex.value = hScrollState.firstVisibleItemIndex
    hScrollOffset.value = hScrollState.firstVisibleItemScrollOffset
    // 协程
    val coroutineScope = rememberCoroutineScope()
    // 列表单选
    var singleSelectionRowIndex by remember { mutableStateOf(-1) }
    var singleSelectionRowToggle by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        // 表头
        stickyHeader {
            // 第一行的内容
            LazyRow(
                modifier = Modifier.fillMaxWidth().height(dataRowHeight)
                    .background(color = MaterialTheme.colors.background),
                state = hScrollState
            ) {
                items(col) { col ->
                    var rowModifier = Modifier
                        .width(100.dp).height(dataRowHeight)
                    if (border) {
                        rowModifier = rowModifier.border(
                            border = BorderStroke(
                                borderWidth,
                                color = MaterialTheme.colors.secondary
                            )
                        )
                    }
                    Row(
                        modifier = rowModifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (headerRow != null) {
                            headerRow.invoke(col)
                        } else {
                            Text(
                                text = headerData[col],
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                    }
                }
                // 第一行的内容滚动
                coroutineScope.launch {
                    hScrollState.scrollToItem(hItemIndex.value, hScrollOffset.value)
                }
            }
            if (!border) {
                Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
            }
        }
        // 表体, 每一列的内容
        items(row) { row ->
            // 每一行的内容
            var wholeRowModifier = Modifier.fillMaxWidth().height(dataRowHeight)
                .background(color = MaterialTheme.colors.background)
                .clickable {
                    if (singleSelectionRowIndex == row) {
                        // 选择了之前被选择的row 则反转该值
                        singleSelectionRowToggle = !singleSelectionRowToggle
                    } else {
                        singleSelectionRowToggle = true
                    }
                    singleSelectionRowIndex = row
                    onRowSelected?.invoke(singleSelectionRowIndex, singleSelectionRowToggle)
                }
            if (stripe && (row % 2 == 1)) {
                wholeRowModifier = wholeRowModifier.background(color = Color.Gray.copy(alpha = 0.191f))
            }
            if (singleSelectionRowToggle && singleSelectionRowIndex != -1 && singleSelectionRowIndex == row) {
                wholeRowModifier = wholeRowModifier.background(color = Color.Gray.copy(alpha = 0.382f))
            }
            LazyRow(
                modifier = wholeRowModifier,
                state = hScrollState,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(col) { col ->
                    var rowModifier = Modifier.width(100.dp).height(dataRowHeight)
                    if (border) {
                        rowModifier = rowModifier.border(
                            border = BorderStroke(
                                borderWidth,
                                color = MaterialTheme.colors.secondary
                            )
                        )
                    }
                    // 每一列的内容, 每一格的里面内容按row水平排列
                    Row(
                        modifier = rowModifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        dataRow.invoke(row, col)
                    }

                }
                // 每一行的内容滚动
                coroutineScope.launch {
                    hScrollState.scrollToItem(hItemIndex.value, hScrollOffset.value)
                }
            }
            if (!border) {
                Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
            }
        }

    }
}


@ExperimentalFoundationApi
fun LazyListScope.corner(key: Any? = null, content: @Composable LazyItemScope.() -> Unit) =
    stickyHeader(key, null, content)

fun LazyListScope.fixedRows(
    count: Int,
    key: ((index: Int) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(index: Int) -> Unit
) = items(count, key, { null }, itemContent)