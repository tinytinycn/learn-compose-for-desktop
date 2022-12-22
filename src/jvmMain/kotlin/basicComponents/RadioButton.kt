package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun RadioButtonDemo() {
    Column {

        var radioBtnState by remember { mutableStateOf(true) }
        Row(Modifier.selectableGroup()) {
            RadioButton(
                selected = radioBtnState,
                onClick = { radioBtnState = true }
            )
            RadioButton(
                selected = !radioBtnState,
                onClick = { radioBtnState = false }
            )
        }

        Divider(Modifier.height(50.dp))

        val radioOptions = listOf("Calls", "Missed", "Friends")
        val (selectOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
        Column(Modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                Row(
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (text == selectOption), onClick = null)
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 16.dp)
                    )

                }
            }
        }
    }
}