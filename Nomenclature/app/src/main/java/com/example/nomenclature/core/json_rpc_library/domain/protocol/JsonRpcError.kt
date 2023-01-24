package com.example.nomenclature.core.json_rpc_library.domain.protocol

data class JsonRpcError(
    val message: String,
    val code: Int,
    val data: Any?
)