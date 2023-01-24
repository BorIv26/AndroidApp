package com.example.nomenclature.domain.use_case

import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteStorageUnitByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(id: String): Flow<Resource<Any>> {
        return repository.deleteStorageUnitById(id)
    }
}