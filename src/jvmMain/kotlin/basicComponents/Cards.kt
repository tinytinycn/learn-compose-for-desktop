package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CardsDemo() {
    Column {

        var count by remember { mutableStateOf(0) }
        Card(
            onClick = { count++ }
        ) { Text("一键 $count 连") }


        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(15.dp) //外边距
                .clickable { },
            elevation = 10.dp //阴影
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(text = buildAnnotatedString {
                    append("欢迎来到")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, color = Color.Gray)) {
                        append("北京 beijing")
                    }
                })
                Text(text = buildAnnotatedString {
                    append("你现在观看的章节是 ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append("Card")
                    }
                })
            }
        }
    }
}