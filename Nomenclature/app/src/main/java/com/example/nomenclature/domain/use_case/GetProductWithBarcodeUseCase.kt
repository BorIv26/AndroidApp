package com.example.nomenclature.domain.use_case

import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.data.remote.dto.ProductDto
import com.example.nomenclature.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductWithBarcodeUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(barcode: String): Flow<Resource<ProductDto>> {
        return repository.getProductWithBarcode(barcode)
    }
}