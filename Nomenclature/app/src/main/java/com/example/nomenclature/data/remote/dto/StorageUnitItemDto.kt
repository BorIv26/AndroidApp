package com.example.nomenclature.data.remote.dto

import com.squareup.moshi.Json

data class StorageUnitItemDto(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "product_id")
    val product_id: String,
    @field:Json(name = "product_name")
    val product_name: String,
    @field:Json(name = "product_SKU")
    val product_SKU: String,
    @field:Json(name = "product_barcode")
    val product_barcode: String,
    @field:Json(name = "product_category_id")
    val product_category_id: String,
    @field:Json(name = "product_category_name")
    val product_category_name: String,
    @field:Json(name = "ext_id")
    val ext_id: String,
)