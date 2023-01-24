package com.example.nomenclature.presentation.storage_utils_item

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import com.example.nomenclature.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nomenclature.presentation.Screen
import com.example.nomenclature.presentation.components.ErrorTextHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageItemScreen(
    navController: NavController,
    isSearchMode: Boolean = true,
    viewModel: StorageItemScreenViewModel = hiltViewModel(),
) {
    BackHandler(
        enabled = true
    ) {
        navigateBack(isSearchMode, navController)
    }
    val state = viewModel.state.collectAsState().value
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if(state.storageItem != null) {
                        Text(
                            text = state.storageItem.product_name,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateBack(isSearchMode, navController)
                        }
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Default.ArrowBack,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            ErrorTextHandler(
                error = state.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                onRefreshClick = viewModel::getStorageItemById
            )
            if(state.storageItem != null && !state.isLoading && state.error == null) {
                Column() {
                    ListItem(
                        headlineText = {
                            Text(
                                text = state.storageItem.product_SKU
                            )
                        },
                        overlineText = {
                            Text(
                                text = stringResource(id = R.string.product_sku_text)
                            )
                        }
                    )
                    ListItem(
                        headlineText = {
                            Text(
                                text = state.storageItem.product_barcode
                            )
                        },
                        overlineText = {
                            Text(
                                text = stringResource(R.string.barcode_text)
                            )
                        }
                    )
                    ListItem(
                        headlineText = {
                            Text(
                                text = state.storageItem.product_category_name
                            )
                        },
                        overlineText = {
                            Text(
                                text = stringResource(R.string.category_text)
                            )
                        }
                    )
                    ListItem(
                        headlineText = {
                            Text(
                                text = state.storageItem.ext_id
                            )
                        },
                        overlineText = {
                            Text(
                                text = stringResource(R.string.ext_id_text)
                            )
                        },
                        trailingContent = {
                            IconButton(
                                onClick = viewModel::onEditClick
                            ) {
                                Icon(
                                    contentDescription = null,
                                    imageVector = Icons.Default.Edit,
                                )
                            }
                        }
                    )
                }
            }

            if(state.isOpenedEditDialog) {
                AlertDialog(
                    onDismissRequest = viewModel::onEditDialogHide,
                    title = {
                        Text(
                            text = stringResource(R.string.edit_alert_title)
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = viewModel::onEditConfirm,
                            modifier = Modifier
                                .align(Alignment.Center)
                            ,
                        ) {
                            Text(
                                text = stringResource(R.string.save_button_text),
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = viewModel::onEditDialogHide,
                            modifier = Modifier
                                .align(Alignment.Center)
                            ,
                        ) {
                            Text(
                                text = stringResource(R.string.delete_dialog_cancel_button_text)
                            )
                        }
                    },
                    text = {
                        OutlinedTextField(
                            value = state.editValue,
                            onValueChange = viewModel::onEditValueChange,
                            trailingIcon = {
                                if(state.editValue.isNotEmpty()) {
                                    IconButton(
                                        onClick = viewModel::onEditTextClear,
                                    ) {
                                        Icon(
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            imageVector = Icons.Default.Clear,
                                        )
                                    }
                                }
                            },
                        )
                    }
                )
            }

            if(state.isOpenedDeleteDialog) {
                AlertDialog(
                    onDismissRequest = viewModel::onDialogHideClick,
                    title = {
                        Text(
                            text = stringResource(R.string.delete_dialog_title)
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.onDeleteStorageItemConfirm {
                                    navigateBack(isSearchMode, navController)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                            ,
                        ) {
                            Text(
                                text = stringResource(R.string.delete_dialog_confirm_button_text),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = viewModel::onDialogHideClick,
                            modifier = Modifier
                                .align(Alignment.Center)
                            ,
                        ) {
                            Text(
                                text = stringResource(R.string.delete_dialog_cancel_button_text)
                            )
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.delete_confirm_text),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                )
            }

            OutlinedButton(
                onClick = viewModel::deleteStorageItem,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomCenter)
                    ,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                )
            ) {
                Text(
                    text = stringResource(R.string.delete_button_text)
                )
            }
        }
    }
}

fun navigateBack(isSearchMode: Boolean, navController: NavController) {
    if(isSearchMode) {
        navController.navigate(Screen.SearchScreen.route) {
            popUpTo(
                Screen.SearchScreen.route
            ) {
                inclusive = true
            }
        }
    }
    else {
        navController.navigate(Screen.ScannerScreen.route) {
            popUpTo(
                Screen.ScannerScreen.route
            ) {
                inclusive = true
            }
        }
    }
}