package com.example.nomenclature.data.remote.dto

data class pagination_scroll(
    val offset: Int,
    val limit: Int,
    val count: Boolean = false,
)