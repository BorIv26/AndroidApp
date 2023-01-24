package com.example.nomenclature.domain.repository

import androidx.paging.PagingData
import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.data.remote.dto.*
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getStorageUnits(
        filters: filters? = null,
    ): Flow<PagingData<StorageUnitItemDto>>

    fun getProducts(
        search: String? = null,
    ): Flow<PagingData<ProductDto>>

    fun getProductWithBarcode(barcode: String): Flow<Resource<ProductDto>>

    fun getProductCategories(): Flow<Resource<List<CategoryDto>>>

    fun getStorageUnitById(id: String): Flow<Resource<StorageUnitItemDto>>

    fun deleteStorageUnitById(id: String): Flow<Resource<Any>>

    fun updateStorageUnitExtId(storageId: String, extId: String): Flow<Resource<StorageUnitItemDto>>

    fun getStorageUnitWithQrCode(qrCodeContent: String): Flow<Resource<StorageUnitItemDto>>

    fun createStorageUnitWithProductBarcode(barcode: String, extId: String): Flow<Resource<StorageUnitItemDto>>

    fun createStorageUnitWithProductId(productId: String, extId: String): Flow<Resource<StorageUnitItemDto>>
}