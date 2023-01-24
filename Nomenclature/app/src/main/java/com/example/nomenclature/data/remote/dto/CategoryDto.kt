package com.example.nomenclature.data.remote.dto

import com.squareup.moshi.Json

data class CategoryDto(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "parent_id")
    val parentId: String?,
    @field:Json(name = "name")
    val name: String,
)