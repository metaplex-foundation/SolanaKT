package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.vendor.borshj.*
import com.solana.vendor.toLong
import java.lang.Exception

data class TokenSwapInfo(
    val version: Int,
    val isInitialized: Boolean,
    val nonce: Int,
    val tokenProgramId: PublicKey,
    val tokenAccountA: PublicKey,
    val tokenAccountB: PublicKey,
    val tokenPool: PublicKey,
    val mintA: PublicKey,
    val mintB: PublicKey,
    val feeAccount: PublicKey,
    val tradeFeeNumerator: Long,
    val tradeFeeDenominator: Long,
    val ownerTradeFeeNumerator: Long,
    val ownerTradeFeeDenominator: Long,
    val ownerWithdrawFeeNumerator: Long,
    val ownerWithdrawFeeDenominator: Long,
    val hostFeeNumerator: Long,
    val hostFeeDenominator: Long,
    val curveType: Int,
    val payer: PublicKey
) : BorshCodable

class TokenSwapInfoRule(override val clazz: Class<TokenSwapInfo> = TokenSwapInfo::class.java): BorshRule<TokenSwapInfo> {
    override fun read(input: BorshInput): TokenSwapInfo {
        val version: Int = input.readU8().toInt()
        val isInitialized: Boolean = input.read().toInt() == 1
        val nonce: Int = input.readU8().toInt()
        val tokenProgramId: PublicKey = PublicKeyRule().read(input)
        val tokenAccountA: PublicKey = PublicKeyRule().read(input)
        val tokenAccountB: PublicKey = PublicKeyRule().read(input)
        val tokenPool: PublicKey =  PublicKeyRule().read(input)
        val mintA: PublicKey = PublicKeyRule().read(input)
        val mintB: PublicKey = PublicKeyRule().read(input)
        val feeAccount: PublicKey = PublicKeyRule().read(input)
        val tradeFeeNumerator: Long = input.readU64()
        val tradeFeeDenominator: Long = input.readU64()
        val ownerTradeFeeNumerator: Long = input.readU64()
        val ownerTradeFeeDenominator: Long = input.readU64()
        val ownerWithdrawFeeNumerator: Long = input.readU64()
        val ownerWithdrawFeeDenominator: Long = input.readU64()
        val hostFeeNumerator: Long = input.readU64()
        val hostFeeDenominator: Long = input.readU64()
        val curveType: Int = input.readU8().toInt()
        val payer: PublicKey = PublicKeyRule().read(input)
        return TokenSwapInfo(
            version,
            isInitialized,
            nonce,
            tokenProgramId,
            tokenAccountA,
            tokenAccountB,
            tokenPool,
            mintA,
            mintB,
            feeAccount,
            tradeFeeNumerator,
            tradeFeeDenominator,
            ownerTradeFeeNumerator,
            ownerTradeFeeDenominator,
            ownerWithdrawFeeNumerator,
            ownerWithdrawFeeDenominator,
            hostFeeNumerator,
            hostFeeDenominator,
            curveType,
            payer
        )
    }

    override fun <Self>write(obj: Any, output: BorshOutput<Self>): Self {
        val tokenSwapInfo = obj as TokenSwapInfo
        output.writeU8(tokenSwapInfo.version)
        if(tokenSwapInfo.isInitialized){
            output.write(1)
        } else {
            output.write(0)
        }
        output.writeU8(tokenSwapInfo.nonce)
        PublicKeyRule().write(tokenSwapInfo.tokenProgramId, output)
        PublicKeyRule().write(tokenSwapInfo.tokenAccountA, output)
        PublicKeyRule().write(tokenSwapInfo.tokenAccountB, output)
        PublicKeyRule().write(tokenSwapInfo.tokenPool, output)
        PublicKeyRule().write(tokenSwapInfo.mintA, output)
        PublicKeyRule().write(tokenSwapInfo.mintB, output)
        PublicKeyRule().write(tokenSwapInfo.feeAccount, output)
        output.writeU64(tokenSwapInfo.tradeFeeNumerator)
        output.writeU64(tokenSwapInfo.tradeFeeDenominator)
        output.writeU64(tokenSwapInfo.ownerTradeFeeNumerator)
        output.writeU64(tokenSwapInfo.ownerTradeFeeDenominator)
        output.writeU64(tokenSwapInfo.ownerWithdrawFeeNumerator)
        output.writeU64(tokenSwapInfo.ownerWithdrawFeeDenominator)
        output.writeU64(tokenSwapInfo.hostFeeNumerator)
        output.writeU64(tokenSwapInfo.hostFeeDenominator)
        output.writeU8(tokenSwapInfo.curveType)
        return PublicKeyRule().write(tokenSwapInfo.payer, output)
    }
}
