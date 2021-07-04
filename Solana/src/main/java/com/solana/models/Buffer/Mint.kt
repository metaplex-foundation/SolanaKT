package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.vendor.borshj.*
import com.solana.vendor.toInt32
import com.solana.vendor.toLong
import java.lang.Exception

class Mint(keys: Map<String, ByteArray>) : BorshCodable {
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

class MintRule(override val clazz: Class<Mint> = Mint::class.java): BorshRule<Mint> {
    override fun read(input: BorshInput): Mint {
        throw Exception("Not implemented Mint")
    }

    override fun <Self>write(obj: Any, output: BorshOutput<Self>): Self {
        throw Exception("Not implemented Mint")
    }
}