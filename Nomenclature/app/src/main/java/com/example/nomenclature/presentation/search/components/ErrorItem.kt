package com.example.nomenclature.presentation.search.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.nomenclature.R

@Composable
fun ErrorItem(
    error: Throwable,
    modifier: Modifier,
    onClickRetry: (() -> Unit)? = null,
) {
    val errorMessage =
        if(error.message != null) {
            error.message
        } else if(error.localizedMessage != null) {
            error.localizedMessage
        }
        else {
            "Неизвестная ошибка"
        }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage!!,
            textAlign = TextAlign.Center,
        )
        onClickRetry?.let {
            TextButton(
                onClick = onClickRetry,
            ) {
                Text(
                    text = stringResource(R.string.refresh_page_button_text),
                )
            }
        }
    }
}