package com.example.nomenclature.core.moshi_json_parser

import com.example.nomenclature.core.json_rpc_library.domain.parser.RequestConverter
import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiRequestConverter(
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
) : RequestConverter {
    override fun convert(request: JsonRpcRequest): String {
        return moshi.adapter(JsonRpcRequest::class.java).toJson(request)
    }
}