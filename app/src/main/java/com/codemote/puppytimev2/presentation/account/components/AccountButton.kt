package com.codemote.puppytimev2.presentation.account.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codemote.puppytimev2.R

@Composable
fun GetStartedButton(onClick: () -> Unit) {
    ElevatedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = stringResource(R.string.get_started_button_text),
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
        )
    }
}

@Composable
fun SignInButton(onClick: () -> Unit) {
    FilledTonalButton(
        onClick = { onClick() },
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.padding(bottom = 15.dp)
    ) {
        Text(
            text = stringResource(R.string.sign_in_button_text),
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
        )
    }
}