package com.example.nomenclature.presentation.storage_utils_item

import com.example.nomenclature.core.util.UiText
import com.example.nomenclature.data.remote.dto.StorageUnitItemDto

data class StorageItemScreenState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val storageItem: StorageUnitItemDto? = null,
    val isOpenedDeleteDialog: Boolean = false,
    val isOpenedEditDialog: Boolean = false,
    val editValue: String = ""
)