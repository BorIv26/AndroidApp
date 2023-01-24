package com.example.nomenclature.core.json_rpc_library.domain

import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcRequest
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse

interface JsonRpcInterceptor {

    fun intercept(chain: Chain): JsonRpcResponse

    interface Chain {
        fun proceed(request: JsonRpcRequest): JsonRpcResponse

        fun request(): JsonRpcRequest
    }
}