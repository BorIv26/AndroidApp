package com.example.nomenclature.core.moshi_json_parser

import com.example.nomenclature.core.json_rpc_library.domain.parser.ResponseParser
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiResponseParser(
    private val moshi: Moshi = Builder().add(KotlinJsonAdapterFactory()).build()
) : ResponseParser {
    override fun parse(data: ByteArray): JsonRpcResponse {
        val responseType = JsonRpcResponse::class.java
        val adapter = moshi.adapter(responseType)
        return adapter.fromJson(data.decodeToString())
            ?: throw IllegalStateException("Unexpectedly null json parse result for value: $data!")
    }
}