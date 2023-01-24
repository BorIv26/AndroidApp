package com.example.nomenclature.presentation.search_product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import com.example.nomenclature.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.nomenclature.presentation.Screen
import com.example.nomenclature.presentation.components.DefaultSearch
import com.example.nomenclature.presentation.components.ErrorTextHandler
import com.example.nomenclature.presentation.search.components.CategoryCard
import com.example.nomenclature.presentation.search.components.ErrorItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchProductScreen(
    viewModel: SearchProductViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.collectAsState().value
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = 6.dp
                    )
                ,
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        DefaultSearch(
                            searchValue = state.searchValue,
                            onValueChange = viewModel::onSearchValueChange,
                            onSearchClear = viewModel::onSearchClearClick,
                            modifier = Modifier
                                .height(40.dp)
                            ,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            },
                        ) {
                            Icon(
                                contentDescription = null,
                                imageVector = Icons.Default.ArrowBack,
                            )
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
            ,
        ) {
            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if(state.error == null && !state.isLoading) {
                state.pagingListFlow?.let { flow ->
                    val lazyPagingItems = flow.collectAsLazyPagingItems()
                    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
                    SwipeRefresh(
                        state = swipeRefreshState,
                        onRefresh = {
                            viewModel.onSearchValueChange(state.searchValue)
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                            ,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            items(lazyPagingItems) { productItem ->
                                productItem?.let {
                                    ElevatedCard(
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                            .padding(horizontal = 6.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .clickable {
                                                    viewModel.onProductClick(productItem)
                                                }
                                                .fillMaxWidth()
                                                .padding(start = 6.dp, top = 6.dp, bottom = 6.dp),
                                        ) {
                                            ListItem(
                                                modifier = Modifier
                                                    .height(51.dp),
                                                headlineText = { Text(productItem.name) },
                                                overlineText = { Text(stringResource(R.string.product_name_text)) },
                                                trailingContent = {
                                                    CategoryCard(
                                                        categoryName = productItem.category.name,
                                                        modifier = Modifier
                                                    )
                                                }
                                            )
                                            ListItem(
                                                modifier = Modifier
                                                    .height(51.dp),
                                                overlineText = { Text(stringResource(R.string.product_id_text)) },
                                                headlineText = { Text(productItem.id) },
                                            )
                                            ListItem(
                                                modifier = Modifier
                                                    .height(51.dp),
                                                headlineText = { Text(productItem.SKU) },
                                                overlineText = { Text(stringResource(R.string.product_sku_text)) },
                                            )
                                        }
                                    }
                                }
                            }
                            lazyPagingItems.apply {
                                when {
                                    loadState.refresh is LoadState.Loading -> {
                                        item {  CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }
                                    }
                                    loadState.append is LoadState.Loading -> {
                                        item {  CircularProgressIndicator(modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(10.dp)) }
                                    }
                                    loadState.refresh is LoadState.Error -> {
                                        val e = loadState.refresh as LoadState.Error
                                        item {
                                            ErrorItem(
                                                error = e.error,
                                                modifier = Modifier.fillParentMaxSize(),
                                                onClickRetry = { retry() }
                                            )
                                        }
                                    }
                                    loadState.append is LoadState.Error -> {
                                        val e = loadState.append as LoadState.Error
                                        item {
                                            ErrorItem(
                                                error = e.error,
                                                onClickRetry = { retry() },
                                                modifier = Modifier.fillParentMaxSize(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(state.isOpenedDialog) {
                AlertDialog(
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    modifier = Modifier
                        .wrapContentHeight()
                        ,
                    onDismissRequest = viewModel::onDismissDialog,
                    title = {
                        state.selectedProduct?.let {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = viewModel::onCreateConfirm,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                            ,
                        ) {
                            Text(
                                text = stringResource(R.string.add_storage_unit_button_text),
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onCreateConfirm { id ->
                                    navController.navigate(Screen.FromAddingScannerStorageItemScreen.withArgs(id)) {
//                                        popUpTo(
//                                            Screen.ScannerScreen.route
//                                        ) {
//                                            inclusive = true
//                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                            ,
                        ) {
                            Text(
                                text = stringResource(R.string.add_and_open_storage_unit_button_text)
                            )
                        }
                    },
                    text = {
                        ErrorTextHandler(
                            error = state.error,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Center),
                            onRefreshClick = viewModel::onDismissDialog
                        )
                        if(state.isLoading) {
                            ListItem(
                                headlineText = {
                                    Text(
                                        text = stringResource(R.string.loading_text)
                                    )
                                },
                                trailingContent = {
                                    CircularProgressIndicator(Modifier.size(30.dp))
                                }
                            )
                        }
                        if(!state.isLoading && state.error == null) {
                            OutlinedTextField(
                                value = state.extIdValue,
                                onValueChange = viewModel::onExtIdValueChange,
                                trailingIcon = {
                                    if(state.extIdValue.isNotEmpty()) {
                                        IconButton(
                                            onClick = viewModel::onExtIdValueClear,
                                        ) {
                                            Icon(
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                imageVector = Icons.Default.Clear,
                                            )
                                        }
                                    }
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.ext_id_number_text)
                                    )
                                }
                            )
                        }
                    }
                )
            }

            ErrorTextHandler(
                error = state.error,
                modifier = Modifier
                    .align(Alignment.Center),
                onRefreshClick = viewModel::initLoad
            )
        }
    }
}