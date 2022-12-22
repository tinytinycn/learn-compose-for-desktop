package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DialogDemo() {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("确认") }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("忽略") }
            },
            title = {
                Text("Dialog标题")
            },
            text = {
                Text(
                    "该区域通常包含支持性文本" +
                            "它提供了有关 Dialog 目的的详细信息。"
                )
            }
        )
    }
}

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DialogDemo2() {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text("Dialog标题")
            },
            text = {
                Text(
                    "该区域通常包含支持性文本" +
                            "它提供了有关 Dialog 目的的详细信息。"
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text("忽略")
                    }
                }
            },
            modifier = Modifier.width(250.dp)
        )
    }
}