package com.example.nomenclature.data.remote

import com.example.nomenclature.core.json_rpc_library.domain.JsonRpc
import com.example.nomenclature.data.remote.dto.*

interface NomenclatureApi {
    @JsonRpc("get_storage_units")
    suspend fun getStorageUnits(
        @JsonRpc("filters") filters: filters? = null,
        @JsonRpc("pagination_scroll") pagination_scroll: pagination_scroll,
        ): StorageUnitsDto

    @JsonRpc("get_products")
    suspend fun getProducts(
        @JsonRpc("search") search: String? = null,
        @JsonRpc("pagination_scroll") pagination_scroll: pagination_scroll,
    ): ProductsDto

    @JsonRpc("get_product_with_barcode")
    suspend fun getProductWithBarcode(
        @JsonRpc("barcode") barcode: String,
    ): ProductDto

    @JsonRpc("get_product_categories")
    suspend fun getProductCategories(
        @JsonRpc("parent_id") parentId: String?,
    ): List<CategoryDto>

    @JsonRpc("get_storage_unit_with_id")
    suspend fun getStorageUnitById(
        @JsonRpc("id") id: String?,
    ): StorageUnitItemDto

    @JsonRpc("delete_storage_unit")
    suspend fun deleteStorageUnitById(
        @JsonRpc("storage_unit_id") id: String?,
    ): Any

    @JsonRpc("update_storage_unit_ext_id")
    suspend fun updateStorageUnitExtId(
        @JsonRpc("storage_unit_id") storageId: String,
        @JsonRpc("ext_id") extId: String,
    ): StorageUnitItemDto

    @JsonRpc("get_storage_unit_with_qrcode")
    suspend fun getStorageUnitWithQrCode(
        @JsonRpc("qrcode_content") qrCodeContent: String,
    ): StorageUnitItemDto

    @JsonRpc("create_storage_unit_with_product_id")
    suspend fun createStorageUnitWithProductId(
        @JsonRpc("product_id") productId: String,
        @JsonRpc("ext_id") extId: String,
    ): StorageUnitItemDto

    @JsonRpc("create_storage_unit_with_product_barcode")
    suspend fun createStorageUnitWithProductBarcode(
        @JsonRpc("barcode") barcode: String,
        @JsonRpc("ext_id") extId: String,
    ): StorageUnitItemDto

    companion object {
        const val BASE_URL = "http://192.168.1.160:8000/api/v1/mobile/jsonrpc/"
        const val MAX_STORAGE_UNITS_ITEMS_PAGE_SIZE = 30
    }
}
