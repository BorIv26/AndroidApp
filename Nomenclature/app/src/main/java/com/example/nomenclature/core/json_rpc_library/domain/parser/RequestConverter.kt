package com.example.nomenclature.core.json_rpc_library.domain.parser

import com.example.nomenclature.core.json_rpc_library.domain.protocol.JsonRpcRequest

interface RequestConverter {
    fun convert(request: JsonRpcRequest): String
}