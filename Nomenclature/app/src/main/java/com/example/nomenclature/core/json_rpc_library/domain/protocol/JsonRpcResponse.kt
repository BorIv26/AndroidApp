package com.example.nomenclature.core.json_rpc_library.domain.protocol

data class JsonRpcResponse(
    val id: Long,
    val result: Any?,
    val error: JsonRpcError?
)