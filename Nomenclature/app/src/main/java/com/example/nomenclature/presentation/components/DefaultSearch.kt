package com.example.nomenclature.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nomenclature.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultSearch(
    searchValue: String,
    onValueChange: (String) -> Unit,
    onSearchClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        modifier = modifier,
        value = searchValue,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
        decorationBox = { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = searchValue,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = MutableInteractionSource(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.default_search_placeholder_text)
                    )
                },
                contentPadding = PaddingValues(0.dp),
                trailingIcon = {
                    if(searchValue.isNotEmpty()) {
                        IconButton(
                            onClick = onSearchClear,
                        ) {
                            Icon(
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                imageVector = Icons.Default.Clear,
                            )
                        }
                    }
                },
                container = {
                    OutlinedCard {
                        Box(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                ,
                        )
                    }
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(
//                                color = MaterialTheme.colorScheme.,
//                                shape = shape
//                            )
//                            .border(
//                                width = 2.dp,
//                                color = MaterialTheme.colorScheme.onSurface,
//                                shape = shape,
//                            )
//                            .padding(all = 0.dp), // inner padding
//                    )
                }
            )
        },
        singleLine = true,
    )
}