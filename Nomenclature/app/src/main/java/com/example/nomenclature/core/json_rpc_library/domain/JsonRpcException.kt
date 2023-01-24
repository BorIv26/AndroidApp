package com.example.nomenclature.core.json_rpc_library.domain

data class JsonRpcException(
    override val message: String,
    val code: Int,
    val data: Any?
) : RuntimeException(message)