package com.example.nomenclature.core.json_rpc_library.domain.parser

import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcResponse

interface ResponseParser {
    fun parse(data: ByteArray): JsonRpcResponse
}