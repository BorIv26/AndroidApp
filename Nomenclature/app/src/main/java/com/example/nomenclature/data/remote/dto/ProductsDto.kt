package com.example.nomenclature.data.remote.dto

import com.squareup.moshi.Json

data class ProductsDto(
    @field:Json(name = "has_next")
    val has_next: Boolean,
    @field:Json(name = "total_size")
    val total_size: Int?,
    @field:Json(name = "items")
    val items: List<ProductDto>,
)