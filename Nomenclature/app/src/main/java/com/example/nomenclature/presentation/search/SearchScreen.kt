package com.example.nomenclature.presentation.search

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import com.example.nomenclature.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
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
                val isSearchVisible = remember { MutableTransitionState(false) }
                var expanded by remember { mutableStateOf(false) }
                ElevatedCard(
                    modifier = Modifier
                        .padding(
                            horizontal = 6.dp
                        )
                        ,
                ) {
                    Column {
                        ListItem(
                            modifier = Modifier
                                .clickable {
                                    isSearchVisible.targetState =
                                        !isSearchVisible.currentState
                                    expanded = !expanded
                                }
                                .height(45.dp),
                            headlineText = {
                                Text(
                                    text = if (state.currentCategory == null)
                                        stringResource(R.string.category_placeholder)
                                    else
                                        state.currentCategory.name,
                                )
                            },
                            trailingContent = {
                                Icon(
                                    contentDescription = null,
                                    imageVector = if (expanded)
                                        Icons.Default.KeyboardArrowUp
                                    else {
                                        Icons.Default.KeyboardArrowDown
                                    }
                                )
                            }
                        )
                        DropdownMenu(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp)
                            ,
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = !expanded
                            },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(id = R.string.category_placeholder)
                                    )
                                },
                                onClick = {
                                    expanded = !expanded
                                    viewModel.onCategoryFilterClear()
                                }
                            )
                            for(category in state.categories) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = category.name
                                        )
                                    },
                                    onClick = {
                                        expanded = !expanded
                                        viewModel.onCategoryClick(category)
                                    }
                                )
                            }
                        }
                    }
                }
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
                            items(lazyPagingItems) { storageItem ->
                                storageItem?.let {
                                    ElevatedCard(
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                            .padding(horizontal = 6.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate(
                                                        Screen.StorageItemScreen.withArgs(
                                                            storageItem.id
                                                        )
                                                    )
                                                }
                                                .fillMaxWidth()
                                                .padding(start = 6.dp, top = 6.dp, bottom = 6.dp),
                                        ) {
                                            ListItem(
                                                modifier = Modifier
                                                    .height(51.dp),
                                                headlineText = { Text(storageItem.product_name) },
                                                overlineText = { Text(stringResource(R.string.product_name_text)) },
                                                trailingContent = {
                                                    CategoryCard(
                                                        categoryName = storageItem.product_category_name,
                                                        modifier = Modifier
                                                    )
                                                }
                                            )
                                            ListItem(
                                                modifier = Modifier
                                                    .height(51.dp),
                                                overlineText = { Text(stringResource(R.string.product_id_text)) },
                                                headlineText = { Text(storageItem.ext_id) },
                                            )
                                            ListItem(
                                                modifier = Modifier
                                                    .height(51.dp),
                                                headlineText = { Text(storageItem.product_SKU) },
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
            ErrorTextHandler(
                error = state.error,
                modifier = Modifier
                    .align(Alignment.Center),
                onRefreshClick = viewModel::initLoad
            )
            FloatingActionButton(
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.BottomEnd)
                    ,
                onClick = {
                    navController.navigate(Screen.QRScannerScreen.route)
                }
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    contentDescription = null,
                    painter = painterResource(id = R.drawable.ic_scanner)
                )
            }
        }
    }
}