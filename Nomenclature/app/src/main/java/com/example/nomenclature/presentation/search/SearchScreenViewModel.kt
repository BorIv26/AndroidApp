package com.example.nomenclature.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.data.remote.dto.CategoryDto
import com.example.nomenclature.data.remote.dto.filters
import com.example.nomenclature.domain.use_case.GetProductCategoriesUseCase
import com.example.nomenclature.domain.use_case.GetStorageUnitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getProductCategoriesUseCase: GetProductCategoriesUseCase,
    private val getStorageUnitsUseCase: GetStorageUnitsUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    init{
        initLoad()
    }

    private fun getPagingListFlow() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    pagingListFlow = getStorageUnitsUseCase(
                        filters = null,
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }

    fun initLoad() {
        getProductCategories()
        getPagingListFlow()
    }

    private fun getProductCategories() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            getProductCategoriesUseCase().collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                categories = result.data
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message
                            )
                        }
                    }
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onCategoryFilterClear() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentCategory = null,
                )
            }
            onSearchValueChange(state.value.searchValue)
        }
    }

    fun onSearchClearClick() {
        onSearchValueChange("")
    }

    fun onCategoryClick(category: CategoryDto) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentCategory = category,
                )
            }
            _state.update {
                val searchValue = state.value.searchValue
                it.copy(
                    pagingListFlow = getStorageUnitsUseCase(
                        filters = filters(
                            search_query = searchValue.ifEmpty { null },
                            state_id = null,
                            product_category_id = category.id,
                        ),
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }

    private var searchJob: Job? = null

    fun onSearchValueChange(value: String) {
        _state.update {
            it.copy(
                searchValue = value,
            )
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    pagingListFlow = getStorageUnitsUseCase(
                        filters = filters(
                            search_query = value,
                            state_id = null,
                            product_category_id = state.value.currentCategory?.id,
                        ),
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }
}