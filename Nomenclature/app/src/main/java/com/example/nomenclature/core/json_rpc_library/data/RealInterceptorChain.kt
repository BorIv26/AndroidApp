package com.example.nomenclature.core.json_rpc_library.data

import com.example.nomenclature.core.json_rpc_library.domain.JsonRpcClient
import com.example.nomenclature.core.json_rpc_library.domain.JsonRpcInterceptor
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcRequest
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse

data class RealInterceptorChain(
    private val client: JsonRpcClient,
    val interceptors: List<JsonRpcInterceptor>,
    private val request: JsonRpcRequest,
    private val index: Int = 0
) : JsonRpcInterceptor.Chain {

    override fun proceed(request: JsonRpcRequest): JsonRpcResponse {
        // Call the next interceptor in the chain. Last one in chain is ServerCallInterceptor.
        val nextChain = copy(index = index + 1, request = request)
        val nextInterceptor = interceptors[index]
        return nextInterceptor.intercept(nextChain)
    }

    override fun request(): JsonRpcRequest = request

    override fun toString(): String {
        return "RealInterceptorChain(index=$index, interceptors=$interceptors)"
    }
}