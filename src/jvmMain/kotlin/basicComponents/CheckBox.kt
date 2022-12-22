package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CheckBoxDemo() {

    var checkedState by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { checkedState = it }
        )


        // define dependent checkboxes states
        val (state, onStateChange) = remember { mutableStateOf(true) }
        val (state2, onStateChange2) = remember { mutableStateOf(true) }

        // TriStateCheckbox state reflects state of dependent checkboxes
        val parentState = remember(state, state2) {
            if (state && state2) ToggleableState.On
            else if (!state && !state2) ToggleableState.Off
            else ToggleableState.Indeterminate
        }
        // click on TriStateCheckbox can set state for dependent checkboxes
        val onParentClick = {
            val s = parentState != ToggleableState.On
            onStateChange(s)
            onStateChange2(s)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TriStateCheckbox(
                state = parentState,
                onClick = onParentClick,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary
                )
            )
            Text("全部")
        }
        Spacer(Modifier.size(5.dp))
        Column(Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(state, onStateChange)
                Text("选项A")
            }
            Spacer(Modifier.size(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(state2, onStateChange2)
                Text("选项B")
            }
        }
    }
}