package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.vendor.toLong

class TokenSwapInfoLayOut(
    override val layout: List<LayoutEntry> = listOf(
        LayoutEntry("version", 1),
        LayoutEntry("isInitialized", 1),
        LayoutEntry("nonce", 1),
        LayoutEntry("tokenProgramId", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("tokenAccountA", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("tokenAccountB", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("tokenPool", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("mintA", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("mintB", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("feeAccount", PublicKey.PUBLIC_KEY_LENGTH),
        LayoutEntry("tradeFeeNumerator", 8),
        LayoutEntry("tradeFeeDenominator", 8),
        LayoutEntry("ownerTradeFeeNumerator", 8),
        LayoutEntry("ownerTradeFeeDenominator", 8),
        LayoutEntry("ownerWithdrawFeeNumerator", 8),
        LayoutEntry("ownerWithdrawFeeDenominator", 8),
        LayoutEntry("hostFeeNumerator", 8),
        LayoutEntry("hostFeeDenominator", 8),
        LayoutEntry("curveType", 1),
        LayoutEntry("payer", PublicKey.PUBLIC_KEY_LENGTH)
    )
) : BufferLayout(layout)

class TokenSwapInfo(val keys: Map<String, ByteArray>) {
    val version: Int
    val isInitialized: Boolean
    val nonce: Int
    val tokenProgramId: PublicKey
    var tokenAccountA: PublicKey
    var tokenAccountB: PublicKey
    val tokenPool: PublicKey
    var mintA: PublicKey
    var mintB: PublicKey
    val feeAccount: PublicKey
    val tradeFeeNumerator: Long
    val tradeFeeDenominator: Long
    val ownerTradeFeeNumerator: Long
    val ownerTradeFeeDenominator: Long
    val ownerWithdrawFeeNumerator: Long
    val ownerWithdrawFeeDenominator: Long
    val hostFeeNumerator: Long
    val hostFeeDenominator: Long
    val curveType: Int
    val payer: PublicKey

    init {
        version = keys["version"]!!.first().toInt()
        isInitialized = keys["isInitialized"]!!.first().toInt() == 1
        nonce = keys["nonce"]!!.first().toInt()
        tokenProgramId = PublicKey(keys["tokenProgramId"]!!)
        tokenAccountA = PublicKey(keys["tokenAccountA"]!!)
        tokenAccountB = PublicKey(keys["tokenAccountB"]!!)
        tokenPool = PublicKey(keys["tokenPool"]!!)
        mintA = PublicKey(keys["mintA"]!!)
        mintB = PublicKey(keys["mintB"]!!)
        feeAccount = PublicKey(keys["feeAccount"]!!)
        tradeFeeNumerator = keys["tradeFeeNumerator"]!!.toLong()
        tradeFeeDenominator = keys["tradeFeeDenominator"]!!.toLong()
        ownerTradeFeeNumerator = keys["ownerTradeFeeNumerator"]!!.toLong()
        ownerTradeFeeDenominator = keys["ownerTradeFeeDenominator"]!!.toLong()
        ownerWithdrawFeeNumerator = keys["ownerWithdrawFeeNumerator"]!!.toLong()
        ownerWithdrawFeeDenominator = keys["ownerWithdrawFeeDenominator"]!!.toLong()
        hostFeeNumerator = keys["hostFeeNumerator"]!!.toLong()
        hostFeeDenominator = keys["hostFeeDenominator"]!!.toLong()
        curveType = keys["curveType"]!!.first().toInt()
        payer = PublicKey(keys["payer"]!!)
    }
}