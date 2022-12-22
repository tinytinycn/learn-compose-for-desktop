package selection

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun selectionDemo() {
    val option1 = Color.Red
    val option2 = Color.Blue
    var selectedOption by remember { mutableStateOf(option1) }
    Column {
        Text("Selected: $selectedOption")
        Row {
            listOf(option1, option2).forEach { color ->
                val selected = selectedOption == color
                Box(
                    Modifier
                        .size(100.dp)
                        .background(color = color)
                        .selectable(
                            selected = selected,
                            onClick = { selectedOption = color }
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun selectionDemo2(){
    var checked by remember { mutableStateOf(false) }
// content that you want to make toggleable
    Text(
        modifier = Modifier.toggleable(value = checked, onValueChange = { checked = it }),
        text = checked.toString()
    )
}


@Preview
@Composable
fun selectionDemo3() {
    var checked by remember { mutableStateOf(ToggleableState.Indeterminate) }
// content that you want to make toggleable
    Text(
        modifier = Modifier.triStateToggleable(
            state = checked,
            onClick = {
                checked =
                    if (checked == ToggleableState.On) ToggleableState.Off else ToggleableState.On
            }
        ),
        text = checked.toString()
    )
}