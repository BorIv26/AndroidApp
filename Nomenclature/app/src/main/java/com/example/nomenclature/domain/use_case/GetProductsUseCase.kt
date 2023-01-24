package com.example.nomenclature.domain.use_case

import com.example.nomenclature.domain.repository.Repository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(
        search: String? = null,
    ) = repository.getProducts(
        search = search,
    )
}