package com.solana.models

import com.squareup.moshi.Json

class ConfigObjects {
    class ConfirmedSignFAddr2(limit: Int) {
        @Json(name = "limit")
        private val limit: Long

        @Json(name = "before")
        private val before: String? = null

        @Json(name = "until")
        private val until: String? = null

        init {
            this.limit = limit.toLong()
        }
    }

    class Memcmp {
        @Json(name = "offset")
        private var offset: Long = 0

        @Json(name = "bytes")
        private var bytes: String? = null

        constructor() {}
        constructor(offset: Long, bytes: String?) {
            this.offset = offset
            this.bytes = bytes
        }
    }

    class Filter {
        @Json(name = "memcmp")
        private var memcmp: Memcmp? = null

        constructor() {}
        constructor(memcmp: Memcmp?) {
            this.memcmp = memcmp
        }
    }

    class ProgramAccountConfig {
        @Json(name = "encoding")
        private var encoding: RpcSendTransactionConfig.Encoding? = null

        @Json(name = "filters")
        private var filters: List<Any>? = null

        constructor() {}
        constructor(filters: List<Any>?) {
            this.filters = filters
        }

        constructor(encoding: RpcSendTransactionConfig.Encoding?) {
            this.encoding = encoding
        }
    }
}