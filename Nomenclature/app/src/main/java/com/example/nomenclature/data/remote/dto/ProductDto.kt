package com.example.nomenclature.data.remote.dto

import com.squareup.moshi.Json

data class ProductDto(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "SKU")
    val SKU: String,
    @field:Json(name = "barcode")
    val barcode: String,
    @field:Json(name = "category")
    val category: CategoryDto,
)