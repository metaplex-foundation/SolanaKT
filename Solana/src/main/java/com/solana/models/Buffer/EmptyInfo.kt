package com.solana.models.Buffer

class EmptyInfoLayout(
    override val layout: List<LayoutEntry> = listOf()
) : BufferLayout(layout)

class EmptyInfo(val keys: Map<String, ByteArray>)