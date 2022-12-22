package basicComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ButtonDemo() {

    val interactionSource = remember { MutableInteractionSource() }

    val (text, textColor, buttonColor) = when {
        interactionSource.collectIsPressedAsState().value -> ButtonState("按下按钮", Color.Red, Color.Black)
        else -> ButtonState("一个按钮", Color.White, Color.Gray)
    }

    var outlineText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.width(200.dp).padding(all = 8.dp)
    ) {
        Button(onClick = {}) {
            Text("按钮")
        }
        Button(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("点赞")
        }
        Button(
            onClick = {},
            modifier = Modifier.width(100.dp).height(IntrinsicSize.Min),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = buttonColor
            ),
            interactionSource = interactionSource,
            shape = RoundedCornerShape(50),
            elevation = ButtonDefaults.elevation(defaultElevation = 15.dp)
        ) {
            Text(text = text, color = textColor)
        }

        OutlinedButton(onClick = {}) { Text("概览按钮") }

        OutlinedTextField(
            value = outlineText,
            onValueChange = { outlineText = it },
            label = {
                Text("Label")
            },
            placeholder = { Text("输入点什么呢") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
        )

        TextButton(
            onClick = {},
            border = BorderStroke(Dp.Hairline, Color.Green)
        ) {
            Text("这是一个按钮哦")
        }

        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.End).fillMaxWidth(1f),
            enabled = true
        ) {
            Row {
                Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null)
                Text("检查")
            }
        }

        var checkedIconToggleBtn by remember { mutableStateOf(false) }
        IconToggleButton(
            checked = checkedIconToggleBtn,
            onCheckedChange = { checkedIconToggleBtn = it }
        ) {
            val tint by animateColorAsState(if (checkedIconToggleBtn) Color(0xFFEC407A) else Color(0xFFB0BEC5))
            Icon(Icons.Filled.Favorite, contentDescription = "本地化描述", tint = tint)
//            Icon(Icons.Filled.Favorite, contentDescription = "本地化描述")
        }
    }
}

data class ButtonState(var text: String, var textColor: Color, var buttonColor: Color)