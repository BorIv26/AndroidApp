package com.example.nomenclature.domain.use_case

import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.data.remote.dto.StorageUnitItemDto
import com.example.nomenclature.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateStorageUnitWithProductBarcodeUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(barcode: String, extId: String): Flow<Resource<StorageUnitItemDto>> {
        return repository.createStorageUnitWithProductBarcode(barcode, extId)
    }
}