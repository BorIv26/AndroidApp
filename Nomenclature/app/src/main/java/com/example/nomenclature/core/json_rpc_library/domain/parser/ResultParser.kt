package com.example.nomenclature.core.json_rpc_library.domain.parser

import java.lang.reflect.Type

interface ResultParser {
    fun <T> parse(type: Type, data: Any): T
}