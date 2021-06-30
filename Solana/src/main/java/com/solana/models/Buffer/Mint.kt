package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.vendor.toInt32
import com.solana.vendor.toLong

class MintLayOut(
    override val layout: List<LayoutEntry> = listOf(
        LayoutEntry("mintAuthorityOption", 4),
        LayoutEntry("mintAuthority",  PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("supply", 8),
        LayoutEntry("decimals", 1),
        LayoutEntry("isInitialized", 1),
        LayoutEntry("freezeAuthorityOption", 4),
        LayoutEntry("freezeAuthority", PublicKey.PUBLIC_KEY_LENGTH)
    )
) : BufferLayout(layout)

class Mint(val keys: Map<String, ByteArray>) {
    val mintAuthorityOption: Int
    var mintAuthority: PublicKey?
    val supply: Long
    val decimals: Int
    val isInitialized: Boolean
    val freezeAuthorityOption: Int
    var freezeAuthority: PublicKey?

    init {
        mintAuthorityOption = keys["mintAuthorityOption"]!!.toInt32()
        mintAuthority = PublicKey(keys["mintAuthority"]!!)
        supply = keys["supply"]!!.toLong()
        decimals = keys["decimals"]!!.first().toInt()
        isInitialized = decimals != 1
        freezeAuthorityOption = keys["freezeAuthorityOption"]!!.toInt32()
        freezeAuthority = PublicKey(keys["freezeAuthority"]!!)

        if(mintAuthorityOption == 0){
            this.mintAuthority = null
        }
        if(freezeAuthorityOption == 0){
            this.freezeAuthority = null
        }
    }
}