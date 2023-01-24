package com.example.nomenclature.presentation.storage_utils_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.domain.use_case.DeleteStorageUnitByIdUseCase
import com.example.nomenclature.domain.use_case.GetStorageUnitByIdUseCase
import com.example.nomenclature.domain.use_case.UpdateStorageUnitExtIdUseCase
import com.example.nomenclature.presentation.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageItemScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStorageUnitByIdUseCase: GetStorageUnitByIdUseCase,
    private val deleteStorageUnitByIdUseCase: DeleteStorageUnitByIdUseCase,
    private val updateStorageUnitExtIdUseCase: UpdateStorageUnitExtIdUseCase,
): ViewModel() {
    private val storageItemId = savedStateHandle.get<String>(Constants.STORAGE_ITEM_ID_PARAM)
    private val _state = MutableStateFlow(StorageItemScreenState())
    val state = _state.asStateFlow()

    init {
        getStorageItemById()
    }

    fun onEditValueChange(value: String) {
        _state.update {
            it.copy(
                editValue = value
            )
        }
    }

    fun onEditDialogHide() {
        _state.update {
            it.copy(
                isOpenedEditDialog = false,
                editValue = it.storageItem!!.ext_id
            )
        }
    }

    fun onEditTextClear() {
        onEditValueChange("")
    }
    fun onEditClick() {
        _state.update {
            it.copy(
                isOpenedEditDialog = true,
            )
        }
    }

    fun onEditConfirm() {
        val state = state.value
        if(state.storageItem == null) {
            return
        }
        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch {
            updateStorageUnitExtIdUseCase(
                extId = state.editValue,
                storageId = state.storageItem.id
            ).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                storageItem = result.data,
                                isOpenedEditDialog = false,
                                editValue = result.data.ext_id,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message,
                                isOpenedEditDialog = false,
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

    fun deleteStorageItem() {
        _state.update {
            it.copy(
                isOpenedDeleteDialog = true,
            )
        }
    }

    fun onDeleteStorageItemConfirm(onSuccess: () -> Unit) {
        val state = state.value
        if(state.storageItem == null)
            return
        viewModelScope.launch {
            deleteStorageUnitByIdUseCase(state.storageItem.id).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isOpenedDeleteDialog = false,
                            )
                        }
                        onSuccess()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onDialogHideClick() {
        _state.update {
            it.copy(
                isOpenedDeleteDialog = false
            )
        }
    }

    fun getStorageItemById() {
        if(storageItemId == null)
            return
        _state.update {
            it.copy(
                error = null,
                isLoading = true,
            )
        }
        viewModelScope.launch {
            getStorageUnitByIdUseCase(
                id = storageItemId
            ).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                storageItem = result.data,
                                editValue = result.data.ext_id
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
}