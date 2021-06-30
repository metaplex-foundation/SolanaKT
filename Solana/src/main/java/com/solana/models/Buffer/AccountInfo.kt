package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.vendor.toInt32
import com.solana.vendor.toLong

class AccountInfoLayout(
    override val layout: List<LayoutEntry> = listOf(
        LayoutEntry("mint", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("owner", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("lamports", 8),
        LayoutEntry("delegateOption", 4),
        LayoutEntry("delegate", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("state", 1),
        LayoutEntry("isNativeOption", 4),
        LayoutEntry("isNativeRaw", 8),
        LayoutEntry("delegatedAmount", 8),
        LayoutEntry("closeAuthorityOption", 4),
        LayoutEntry("closeAuthority", PublicKey.PUBLIC_KEY_LENGTH)
    )
) : BufferLayout(layout)

class AccountInfoData(val keys: Map<String, ByteArray>) {
    val mint: PublicKey
    val owner: PublicKey
    val lamports: Long
    val delegateOption: Int
    var delegate: PublicKey?
    val isInitialized: Boolean
    val isFrozen: Boolean
    val state: Int
    val isNativeOption: Int
    val rentExemptReserve: Long?
    val isNativeRaw: Long
    val isNative: Boolean
    var delegatedAmount: Long
    val closeAuthorityOption: Int
    var closeAuthority: PublicKey?

    init {
        mint = PublicKey(keys["mint"]!!)
        owner = PublicKey(keys["owner"]!!)
        lamports = keys["lamports"]!!.toLong()
        delegateOption = keys["delegateOption"]!!.toInt32()
        delegate = PublicKey(keys["delegate"]!!)
        state = keys["state"]!!.first().toInt()
        isNativeOption = keys["isNativeOption"]!!.toInt32()
        isNativeRaw = keys["isNativeRaw"]!!.toLong()
        delegatedAmount = keys["delegatedAmount"]!!.toLong()
        closeAuthorityOption = keys["closeAuthorityOption"]!!.toInt32()
        closeAuthority = PublicKey(keys["closeAuthority"]!!)

        if(delegateOption == 0) {
            this.delegate = null
            this.delegatedAmount = 0
        }

        this.isInitialized = state != 0
        this.isFrozen = state == 2

        if(isNativeOption == 1){
            this.rentExemptReserve = isNativeRaw
            this.isNative = true
        } else {
            this.rentExemptReserve = null
            isNative = false
        }

        if(closeAuthorityOption == 0){
            this.closeAuthority = null
        }
    }
}

