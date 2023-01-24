package com.example.nomenclature.domain.use_case

import com.example.nomenclature.domain.repository.Repository
import javax.inject.Inject

class UpdateStorageUnitExtIdUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(
        extId: String,
        storageId: String
    ) = repository.updateStorageUnitExtId(
        extId = extId,
        storageId = storageId
    )
}