package com.example.nomenclature.core.json_rpc_library.domain

import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcRequest
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse

interface JsonRpcClient {
    fun call(jsonRpcRequest: JsonRpcRequest): JsonRpcResponse
}