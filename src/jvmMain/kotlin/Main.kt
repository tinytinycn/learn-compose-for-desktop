// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import customComponents.DataProvicder
import customComponents.Item
import customComponents.SimpleTable


@OptIn(ExperimentalMaterialApi::class)
fun main() = application {

    /*Window(onCloseRequest = ::exitApplication) {
        val headers = listOf("序号", "姓名", "操作")
        val bodyDataKey = listOf("id", "name")
        val bodyData: List<Item> = DataProvicder.testItems

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
                stripe = false,
                headerData = headers,
                onRowSelected = { singleSelectionRowIndex, singleSelectionRowToggle ->
                    println("row-$singleSelectionRowIndex ${if (singleSelectionRowToggle) "isSelected" else "unSelected"}") },
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
    }*/
}
