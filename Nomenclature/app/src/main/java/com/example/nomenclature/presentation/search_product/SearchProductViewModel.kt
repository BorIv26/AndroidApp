package com.example.nomenclature.presentation.search_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.data.remote.dto.ProductDto
import com.example.nomenclature.domain.use_case.CreateStorageUnitWithProductIdUseCase
import com.example.nomenclature.domain.use_case.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val createStorageUnitWithProductIdUseCase: CreateStorageUnitWithProductIdUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SearchProductState())
    val state = _state.asStateFlow()

    init{
        initLoad()
    }

    fun onProductClick(productItem: ProductDto) {
        _state.update {
            it.copy(
                selectedProduct = productItem,
                isOpenedDialog = true
            )
        }
    }

    fun onExtIdValueChange(value: String) {
        _state.update {
            it.copy(
                extIdValue = value
            )
        }
    }

    fun onCreateConfirm(onSuccess: (String) -> Unit = {}) {
        val state = state.value
        if(state.selectedProduct == null)
            return
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            createStorageUnitWithProductIdUseCase(
                productId = state.selectedProduct.id,
                extId = state.extIdValue
            ).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        onSuccess(result.data.id)
                        onDismissDialog()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message,
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

    fun onExtIdValueClear() {
        onExtIdValueChange("")
    }

    fun onDismissDialog() {
        _state.update {
            it.copy(
                isOpenedDialog = false,
                selectedProduct = null
            )
        }
    }

    private fun getPagingListFlow() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    pagingListFlow = getProductsUseCase(
                        search = null,
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }

    fun initLoad() {
        getPagingListFlow()
    }

    fun onSearchClearClick() {
        onSearchValueChange("")
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
                    pagingListFlow = getProductsUseCase(
                        search = value
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }
}