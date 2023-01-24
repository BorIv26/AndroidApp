package com.example.nomenclature.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.nomenclature.R
import com.example.nomenclature.core.util.UiText

@Composable
fun ErrorTextHandler(
    error: UiText?,
    modifier: Modifier,
    onRefreshClick: () -> Unit
) {
    error?.let { errorMsg ->
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = errorMsg.asString(),
                textAlign = TextAlign.Center,
            )
            TextButton(
                onClick = onRefreshClick,
            ) {
                Text(
                    text = stringResource(R.string.refresh_page_button_text),
                )
            }
        }
    }
}