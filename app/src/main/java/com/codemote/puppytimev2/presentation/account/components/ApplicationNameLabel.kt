package com.codemote.puppytimev2.presentation.account.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.codemote.puppytimev2.R

@Composable
fun ApplicationNameLabel() {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )
            ) {
                append(stringResource(R.string.app_name_1))
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                append(stringResource(R.string.app_name_2))
            }
            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                append("©")
            }
        },
        style = MaterialTheme.typography.displayLarge.copy(
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    )
}