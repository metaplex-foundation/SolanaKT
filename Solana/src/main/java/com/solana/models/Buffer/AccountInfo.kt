package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.BorshInput
import com.solana.vendor.borshj.BorshOutput
import com.solana.vendor.borshj.BorshRule
import java.lang.Exception

data class AccountInfo (
    val mint: PublicKey,
    val owner: PublicKey,
    val lamports: Long,
    val delegateOption: Int,
    var delegate: PublicKey?,
    val isInitialized: Boolean,
    val isFrozen: Boolean,
    val state: Int,
    val isNativeOption: Int,
    val rentExemptReserve: Long?,
    val isNativeRaw: Long,
    val isNative: Boolean,
    var delegatedAmount: Long,
    val closeAuthorityOption: Int,
    var closeAuthority: PublicKey?
): BorshCodable

class AccountInfoRule(override val clazz: Class<AccountInfo> = AccountInfo::class.java):
    BorshRule<AccountInfo> {
    override fun read(input: BorshInput): AccountInfo {
        val mint: PublicKey = PublicKeyRule().read(input)
        val owner: PublicKey = PublicKeyRule().read(input)
        val lamports: Long = input.readU64()
        val delegateOption: Int = input.readU32()
        val tempdelegate: PublicKey? = try {  PublicKeyRule().read(input)  } catch (e : Exception) { null }
        val state: Int = input.read().toInt()
        val isNativeOption: Int = input.readU32()
        val isNativeRaw: Long = input.readU64()
        var delegatedAmount: Long = input.readU64()
        val closeAuthorityOption: Int = input.readU32()
        var closeAuthority: PublicKey? = try {  PublicKeyRule().read(input) } catch (e : Exception) { null }

        val delegate: PublicKey?
        if(delegateOption == 0){
            delegate = null
            delegatedAmount = 0
        } else {
            delegate = tempdelegate
        }

        val isInitialized = state != 0
        val isFrozen = state == 2

        val isNative: Boolean?
        val rentExemptReserve: Long?
        if (isNativeOption == 1) {
            rentExemptReserve = isNativeRaw
            isNative = true
        } else {
            rentExemptReserve = null
            isNative = false
        }

        if(closeAuthorityOption == 0){
            closeAuthority = null
        }


        return AccountInfo(
            mint,
            owner,
            lamports,
            delegateOption,
            delegate,
            isInitialized,
            isFrozen,
            state,
            isNativeOption,
            rentExemptReserve,
            isNativeRaw,
            isNative,
            delegatedAmount,
            closeAuthorityOption,
            closeAuthority
        )
    }


    override fun <Self>write(obj: Any, output: BorshOutput<Self>): Self {
        val accountInfo = obj as AccountInfo
        PublicKeyRule().write(accountInfo.mint, output)
        PublicKeyRule().write(accountInfo.owner, output)
        output.writeU64(accountInfo.lamports)
        output.writeU32(accountInfo.delegateOption)
        accountInfo.delegate?.let {
            PublicKeyRule().write(it, output)
        }?: run {
            PublicKeyRule().writeZeros(output)
        }
        output.write(accountInfo.state.toByte())
        output.writeU32(accountInfo.isNativeOption)
        output.writeU64(accountInfo.isNativeRaw)
        output.writeU64(accountInfo.delegatedAmount)
        output.writeU32(accountInfo.closeAuthorityOption)
        return accountInfo.closeAuthority?.let {
            PublicKeyRule().write(it, output)
        }?: run {
            PublicKeyRule().writeZeros(output)
        }
    }
}
