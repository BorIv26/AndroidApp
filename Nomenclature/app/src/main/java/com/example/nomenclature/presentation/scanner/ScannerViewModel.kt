package com.example.nomenclature.presentation.scanner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.domain.use_case.CreateStorageUnitWithProductBarcodeUseCase
import com.example.nomenclature.domain.use_case.GetProductWithBarcodeUseCase
import com.example.nomenclature.domain.use_case.GetStorageUnitByQrCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val getStorageUnitByQrCode: GetStorageUnitByQrCode,
    private val createStorageUnitWithProductBarcodeUseCase: CreateStorageUnitWithProductBarcodeUseCase,
    private val getProductWithBarcodeUseCase: GetProductWithBarcodeUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(ScannerScreenState())
    val state = _state.asStateFlow()

    private var searchByQrJob: Job? = null

    fun onExtIdValueClear() {
        onExtIdValueChange("")
    }

    fun onExtIdValueChange(value: String) {
        _state.update {
            it.copy(
                extIdValue = value
            )
        }
    }

    fun onUnitCreateWithBarcodeConfirm(onSuccess: (String) -> Unit = {}) {
        val state = state.value
        if(state.barcodeValue == null) {
            return
        }
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            createStorageUnitWithProductBarcodeUseCase(
                barcode = state.barcodeValue,
                extId = state.extIdValue
            ).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                qrCodeStorageItem = result.data,
                            )
                        }
                        onCreateExtIdDialogHideClick()
                        onSuccess(result.data.id)
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

    fun onCreateExtIdDialogHideClick() {
        _state.update {
            it.copy(
                isCreateExtIdDialogOpened = false,
                barcodeValue = null,
                foundProduct = null,
                error = null,
            )
        }
    }

    fun handleScannerResult(value: String, isSearchMode: Boolean, onSuccess: (String) -> Unit) {
        val state = state.value
        if(searchByQrJob != null || state.foundProduct != null || state.error != null) {
            return
        }
        searchByQrJob = viewModelScope.launch {
            if(isSearchMode) {
                if(state.qrCodeStorageItem != null) {
                    return@launch
                }
                getStorageUnitByQrCode(
                    value
                ).collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    qrCodeStorageItem = result.data
                                )
                            }
                            onSuccess(result.data.id)
                        }
                        is Resource.Error -> {
                        }
                    }
                    searchByQrJob = null
                }
            }
            else {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isCreateExtIdDialogOpened = true,
                    )
                }
                getProductWithBarcodeUseCase(value).collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    barcodeValue = value,
                                    foundProduct = result.data,
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
                    searchByQrJob = null
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}