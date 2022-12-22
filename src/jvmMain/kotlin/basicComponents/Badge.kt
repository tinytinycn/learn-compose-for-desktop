package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable

@Preview
@Composable
fun BadgeDemo() {
    BottomNavigation {
        BottomNavigationItem(
            icon = {
                BadgedBox(badge = { Badge { Text("8") } }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorite"
                    )
                }
            },
            selected = false,
            onClick = {}
        )
    }
}