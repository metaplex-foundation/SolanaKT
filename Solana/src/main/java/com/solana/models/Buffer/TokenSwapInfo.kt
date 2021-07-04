package com.solana.models.Buffer

import com.solana.core.PublicKey
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.toLong

class TokenSwapInfo(keys: Map<String, ByteArray>) : BorshCodable {
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