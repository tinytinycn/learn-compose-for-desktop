package basicComponents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TextFieldDemo() {
    var text by remember { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    fun validate(text: String) {
        isError = text.count() < 5
    }

    Column {
        TextField(
            value = text,
            onValueChange = {
                text = it
                isError = false
            },
            label = { Text("Label") },
            singleLine = true,
            placeholder = { Text("example@gmail.com") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            trailingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
            isError = isError,
            keyboardActions = KeyboardActions { validate(text) },
            modifier = Modifier.semantics {
                if (isError) error("email format is invalid.")
            }
        )
        Text(
            text = "Helper message",
            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
        Divider()

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Face else Icons.Filled.Done
                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )
        Divider()

        var text2 by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue("example", TextRange(0, 7)))
        }
        TextField(
            value = text2,
            onValueChange = { text2 = it },
            label = { Text("Label2") }
        )
        Divider()

        var text3 by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = text3,
            onValueChange = { text3 = it },
            label = { Text("Label3") }
        )

    }
}