package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Preview
@Composable
fun SliderDemo() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = sliderPosition.toString())
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..100f,
        steps = 5,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.secondary,
            activeTickColor = MaterialTheme.colors.secondary
        )
    )
}