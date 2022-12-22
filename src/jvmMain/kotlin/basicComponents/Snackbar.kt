package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch

@Preview
@Composable
fun SnackbarDemo() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { snackbarData: SnackbarData ->
                Snackbar(
                    modifier = Modifier.border(Dp.Hairline, MaterialTheme.colors.secondary),
                    snackbarData = snackbarData
                )
            }
        },
        floatingActionButton = {
            var clickCount by remember { mutableStateOf(0) }
            ExtendedFloatingActionButton(
                text = { Text("Show snackbar") },
                onClick = {
                    // show snackbar as a suspend function
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Snackbar # ${++clickCount}")
                    }
                }
            )
        },
        content = { innerPadding ->
            Text(
                text = "Body content",
                modifier = Modifier.padding(innerPadding).fillMaxSize().wrapContentSize()
            )
        }
    )
}