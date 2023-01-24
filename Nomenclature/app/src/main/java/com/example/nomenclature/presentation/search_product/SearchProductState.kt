package com.example.nomenclature.presentation.search_product

import androidx.paging.PagingData
import com.example.nomenclature.core.util.UiText
import com.example.nomenclature.data.remote.dto.ProductDto
import kotlinx.coroutines.flow.Flow

data class SearchProductState(
    val isLoading: Boolean = false,
    val searchValue: String = "",
    val pagingListFlow: Flow<PagingData<ProductDto>>? = null,
    val error: UiText? = null,
    val isOpenedDialog: Boolean = false,
    val extIdValue: String = "",
    val selectedProduct: ProductDto? = null
)