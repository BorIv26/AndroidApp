package com.example.nomenclature.presentation.search

import androidx.paging.PagingData
import com.example.nomenclature.core.util.UiText
import com.example.nomenclature.data.remote.dto.CategoryDto
import com.example.nomenclature.data.remote.dto.StorageUnitItemDto
import kotlinx.coroutines.flow.Flow

data class SearchScreenState(
    val isLoading: Boolean = false,
    val searchValue: String = "",
    val categories: List<CategoryDto> = emptyList(),
    val pagingListFlow: Flow<PagingData<StorageUnitItemDto>>? = null,
    val currentCategory: CategoryDto? = null,
    val error: UiText? = null
)