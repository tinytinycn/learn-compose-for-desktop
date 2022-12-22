package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun SheetBottomDemo() {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        sheetContent = {
            Box(
                Modifier.fillMaxWidth().height(128.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("Swipe up to expand sheet")
            }
            Column(
                Modifier.fillMaxWidth().padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        },
        sheetBackgroundColor = MaterialTheme.colors.secondary,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Bottom sheet scaffold") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Localized description")
                    }
                }
            )
        },
        floatingActionButton = {
            var clickCount by remember { mutableStateOf(0) }
            FloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Snackbar #${++clickCount}")
                    }
                }
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Localized description")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 128.dp,
        drawerContent = {
            Column(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Drawer content")
                Spacer(Modifier.height(20.dp))
                Button(onClick = { scope.launch { scaffoldState.drawerState.close() } }) {
                    Text("Click to close drawer")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(100) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = Color.White)
                ) {
                    Text("item $it")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun SheetBottomDemo2() {
    var skipHalfExpanded by remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.HalfExpanded,
        // skipHalfExpanded = skipHalfExpanded 貌似没有这个参数
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            LazyColumn {
                items(50) {
                    ListItem(
                        text = { Text("Item $it") },
                        icon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Localized description"
                            )
                        }
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.toggleable(
                    value = skipHalfExpanded,
                    role = Role.Checkbox,
                    onValueChange = { checked -> skipHalfExpanded = checked }
                )
            ) {
                Checkbox(checked = skipHalfExpanded, onCheckedChange = null)
                Spacer(Modifier.width(16.dp))
                Text("Skip Half Expanded State")
            }
            Spacer(Modifier.height(20.dp))
            Button(onClick = { scope.launch { state.show() } }) {
                Text("Click to show sheet")
            }
        }
    }
}