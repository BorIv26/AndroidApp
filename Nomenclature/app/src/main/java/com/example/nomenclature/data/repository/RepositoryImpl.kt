package com.example.nomenclature.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.nomenclature.R
import com.example.nomenclature.core.json_rpc_library.domain.JsonRpcException
import com.example.nomenclature.core.product_paging_source.ProductPagingSource
import com.example.nomenclature.core.storage_unit_paging_source.StorageUnitPagingSource
import com.example.nomenclature.core.util.Resource
import com.example.nomenclature.core.util.UiText
import com.example.nomenclature.data.remote.NomenclatureApi
import com.example.nomenclature.data.remote.dto.*
import com.example.nomenclature.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: NomenclatureApi
): Repository {
    override fun getStorageUnits(
        filters: filters?
    ): Flow<PagingData<StorageUnitItemDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = NomenclatureApi.MAX_STORAGE_UNITS_ITEMS_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                StorageUnitPagingSource(
                    filters = filters,
                    nomenclatureApi = api,
                )
            }
        ).flow
    }

    override fun getProducts(search: String?): Flow<PagingData<ProductDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = NomenclatureApi.MAX_STORAGE_UNITS_ITEMS_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                ProductPagingSource(
                    search = search,
                    nomenclatureApi = api,
                )
            }
        ).flow
    }

    override fun getProductWithBarcode(barcode: String): Flow<Resource<ProductDto>> {
        return flow {
            emit(safeApiCall{
                api.getProductWithBarcode(barcode)
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun getProductCategories(): Flow<Resource<List<CategoryDto>>> {
        return flow {
            emit(safeApiCall{
                api.getProductCategories(null)
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun getStorageUnitById(id: String): Flow<Resource<StorageUnitItemDto>> {
        return flow {
            emit(safeApiCall{
                api.getStorageUnitById(id)
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteStorageUnitById(id: String): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall{
                api.deleteStorageUnitById(id)
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun updateStorageUnitExtId(
        storageId: String,
        extId: String
    ): Flow<Resource<StorageUnitItemDto>> {
        return flow {
            emit(safeApiCall{
                api.updateStorageUnitExtId(
                    extId = extId,
                    storageId = storageId
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun getStorageUnitWithQrCode(qrCodeContent: String): Flow<Resource<StorageUnitItemDto>> {
        return flow {
            emit(safeApiCall{
                api.getStorageUnitWithQrCode(
                    qrCodeContent = qrCodeContent
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun createStorageUnitWithProductBarcode(
        barcode: String,
        extId: String
    ): Flow<Resource<StorageUnitItemDto>> {
        return flow {
            emit(safeApiCall{
                api.createStorageUnitWithProductBarcode(
                    barcode = barcode,
                    extId = extId,
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun createStorageUnitWithProductId(
        productId: String,
        extId: String
    ): Flow<Resource<StorageUnitItemDto>> {
        return flow {
            emit(safeApiCall{
                api.createStorageUnitWithProductId(
                    productId = productId,
                    extId = extId,
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): Resource<T> {
        return try {
            val data = apiCall()
            Resource.Success(data = data)
        } catch(throwable: Throwable) {
            Resource.Error(handleThrowableException(throwable))
        }
    }

    private fun handleThrowableException(throwable: Throwable): UiText {
        return when(throwable) {
            is JsonRpcException -> {
                if(throwable.localizedMessage.isNullOrEmpty()) {
                    UiText.StringResource(R.string.unknown_exception)
                }
                else {
                    UiText.DynamicString(throwable.message)
                }
            }
            is IOException -> {
                UiText.StringResource(R.string.io_exception)
            }
            else -> UiText.StringResource(R.string.unknown_exception)
        }
    }
}