package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LinearProgressIndicatorDemo() {
    Column {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
        )
        Divider()
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colors.secondary,
            strokeWidth = 5.dp
        )
    }
}