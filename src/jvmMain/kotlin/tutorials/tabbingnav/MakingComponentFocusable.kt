package tutorials.tabbingnav

import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.WindowSize
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun main() = application {
    Window(
        state = WindowState(size = WindowSize(350.dp, 450.dp)),
        onCloseRequest = ::exitApplication
    ) {
        val buttonFocusRequester = remember { FocusRequester() }
        val textFieldFocusRequester = remember { FocusRequester() }
        val focusState = remember { mutableStateOf(false) }
        val text = remember { mutableStateOf("") }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(50.dp)
            ) {
                Button(
                    onClick = {
                        focusState.value = !focusState.value
                        if (focusState.value) {
                            textFieldFocusRequester.requestFocus()
                        } else {
                            buttonFocusRequester.requestFocus()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(buttonFocusRequester)
                        .focusable()
                ) {
                    Text(text = "Focus switcher")
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = text.value,
                    singleLine = true,
                    onValueChange = { text.value = it },
                    modifier = Modifier
                        .focusRequester(textFieldFocusRequester)
                )
            }
        }
    }
}