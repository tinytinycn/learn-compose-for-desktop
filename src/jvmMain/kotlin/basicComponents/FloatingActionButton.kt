package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun FABDemo() {
    Column(
        modifier = Modifier.padding(all = 4.dp)
    ) {
        FloatingActionButton(onClick = {}) {
            Icon(Icons.Filled.Favorite, contentDescription = null)
        }
        ExtendedFloatingActionButton(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            text = { Text("ADD TO BASKET") },
            onClick = { /*do something*/ }
        )
        ExtendedFloatingActionButton(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            text = { Text("添加到购物车") },
            onClick = { /*do something*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}