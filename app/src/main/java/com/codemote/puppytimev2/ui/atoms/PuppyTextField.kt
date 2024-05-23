package com.codemote.puppytimev2.ui.atoms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PuppyTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "",
    leadingIcon: @Composable() (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(),
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        leadingIcon = leadingIcon,
        isError = isError,
        enabled = enabled,
        visualTransformation = visualTransformation,
        supportingText = {
            if (isError) {
                Text(errorMessage)
            }
        },
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = {
            onValueChange(it)
        }
    )
}