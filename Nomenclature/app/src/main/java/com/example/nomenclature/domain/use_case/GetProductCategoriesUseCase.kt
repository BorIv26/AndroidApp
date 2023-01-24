package com.example.nomenclature.domain.use_case

import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.data.remote.dto.CategoryDto
import com.example.nomenclature.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductCategoriesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<Resource<List<CategoryDto>>> {
        return repository.getProductCategories()
    }
}