package com.example.nomenclature.domain.use_case

import com.example.nomenclature.data.remote.dto.filters
import com.example.nomenclature.domain.repository.Repository
import javax.inject.Inject

class GetStorageUnitsUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(
        filters: filters? = null,
    ) = repository.getStorageUnits(
        filters = filters,
    )
}