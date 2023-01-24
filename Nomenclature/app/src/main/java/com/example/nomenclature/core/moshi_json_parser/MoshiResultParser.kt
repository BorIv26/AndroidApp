package com.example.nomenclature.core.moshi_json_parser

import com.example.nomenclature.core.json_rpc_library.domain.parser.ResultParser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi.*
import java.lang.reflect.Type

class MoshiResultParser(
    private val moshi: Moshi = Builder().add(KotlinJsonAdapterFactory()).build()
) : ResultParser {
    override fun <T> parse(type: Type, data: Any): T {
        return moshi.adapter<T>(type).fromJsonValue(data)
            ?: throw IllegalStateException("Unexpectedly null json parse result for value: $data!")
    }
}