package com.example.nomenclature.core.json_rpc_library.data

import com.example.nomenclature.core.json_rpc_library.domain.JsonRpcClient
import com.example.nomenclature.core.json_rpc_library.domain.JsonRpcInterceptor
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse

class ServerCallInterceptor(private val client: JsonRpcClient) : JsonRpcInterceptor {

    override fun intercept(chain: JsonRpcInterceptor.Chain): JsonRpcResponse {
        return client.call(chain.request())
    }
}