package com.example.nomenclature.presentation.scanner

import com.example.nomenclature.core.util.UiText
import com.example.nomenclature.data.remote.dto.ProductDto
import com.example.nomenclature.data.remote.dto.StorageUnitItemDto

data class ScannerScreenState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val qrCodeStorageItem: StorageUnitItemDto? = null,
    val isCreateExtIdDialogOpened: Boolean = false,
    val barcodeValue: String? = null,
    val extIdValue: String = "",
    val foundProduct: ProductDto? = null,
)