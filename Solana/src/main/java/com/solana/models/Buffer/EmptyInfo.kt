package com.solana.models.Buffer

class EmptyInfoLayout(
    override val clazz: Class<EmptyInfo> = EmptyInfo::class.java,
    override val layout: List<LayoutEntry> = listOf()
) : BufferLayout<EmptyInfo>(layout, clazz)

class EmptyInfo(val keys: Map<String, ByteArray>)