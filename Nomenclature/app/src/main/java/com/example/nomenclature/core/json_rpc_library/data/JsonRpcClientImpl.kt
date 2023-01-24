package com.example.nomenclature.core.json_rpc_library.data

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.nomenclature.core.json_rpc_library.domain.JsonRpcClient
import com.example.nomenclature.core.json_rpc_library.domain.NetworkRequestException
import com.example.nomenclature.core.json_rpc_library.domain.TransportException
import com.example.nomenclature.core.json_rpc_library.domain.parser.RequestConverter
import com.example.nomenclature.core.json_rpc_library.domain.parser.ResponseParser
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcRequest
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse

class JsonRpcClientImpl(
    private val baseUrl: String,
    private val okHttpClient: OkHttpClient,
    private val requestConverter: RequestConverter,
    private val responseParser: ResponseParser
) : JsonRpcClient {

    override fun call(jsonRpcRequest: JsonRpcRequest): JsonRpcResponse {
        val requestBody = requestConverter.convert(jsonRpcRequest).toByteArray().toRequestBody()
        val request = Request.Builder()
            .post(requestBody)
            .url(baseUrl)
            .build()
        val response = try {
            okHttpClient.newCall(request).execute()
        } catch (e: Exception) {
            throw NetworkRequestException(
                message = "Network error: ${e.message}",
                cause = e
            )
        }
        return if (response.isSuccessful) {
            response.body?.let { responseParser.parse(it.bytes()) }
                ?: throw IllegalStateException("Response body is null")
        } else {
            throw TransportException(
                httpCode = response.code,
                message = "HTTP ${response.code}. ${response.message}",
                response = response,
            )
        }
    }
}