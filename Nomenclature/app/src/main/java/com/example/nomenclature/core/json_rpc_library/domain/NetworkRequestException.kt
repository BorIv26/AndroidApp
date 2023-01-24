package com.example.nomenclature.core.json_rpc_library.domain

class NetworkRequestException(
    override val message: String?,
    override val cause: Throwable
) : RuntimeException(message, cause)