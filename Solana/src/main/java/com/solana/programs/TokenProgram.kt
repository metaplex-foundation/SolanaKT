package com.solana.programs

import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * Class for creating Token Program [TransactionInstruction]s
 */
object TokenProgram : Program() {
    val PROGRAM_ID = PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA")
    private val SYSVAR_RENT_PUBKEY = PublicKey("SysvarRent111111111111111111111111111111111")
    private const val INITIALIZE_METHOD_ID = 1
    private const val TRANSFER_METHOD_ID = 3
    private const val CLOSE_ACCOUNT_METHOD_ID = 9
    private const val TRANSFER_CHECKED_METHOD_ID = 12

    /**
     * Transfers an SPL token from the owner's source account to destination account.
     * Destination pubkey must be the Token Account (created by token mint), and not the main SOL address.
     * @param source SPL token wallet funding this transaction
     * @param destination Destined SPL token wallet
     * @param amount 64 bit amount of tokens to send
     * @param owner account/private key signing this transaction
     * @return transaction id for explorer
     */
    fun transfer(
        source: PublicKey,
        destination: PublicKey,
        amount: Long,
        owner: PublicKey
    ): TransactionInstruction {
        val keys: MutableList<AccountMeta> = ArrayList()
        keys.add(AccountMeta(source, false, true))
        keys.add(AccountMeta(destination, false, true))
        keys.add(AccountMeta(owner, true, false))
        val transactionData = encodeTransferTokenInstructionData(
            amount
        )
        return createTransactionInstruction(
            PROGRAM_ID,
            keys,
            transactionData
        )
    }

    fun transferChecked(
        source: PublicKey,
        destination: PublicKey,
        amount: Long,
        decimals: Byte,
        owner: PublicKey,
        tokenMint: PublicKey
    ): TransactionInstruction {
        val keys: MutableList<AccountMeta> = ArrayList()
        keys.add(AccountMeta(source, false, true))
        // index 1 = token mint (https://docs.rs/spl-token/3.1.0/spl_token/instruction/enum.TokenInstruction.html#variant.TransferChecked)
        keys.add(AccountMeta(tokenMint, false, false))
        keys.add(AccountMeta(destination, false, true))
        keys.add(AccountMeta(owner, true, false))
        val transactionData = encodeTransferCheckedTokenInstructionData(
            amount,
            decimals
        )
        return createTransactionInstruction(
            PROGRAM_ID,
            keys,
            transactionData
        )
    }

    fun initializeAccount(
        account: PublicKey,
        mint: PublicKey,
        owner: PublicKey
    ): TransactionInstruction {
        val keys: MutableList<AccountMeta> = ArrayList()
        keys.add(AccountMeta(account, false, true))
        keys.add(AccountMeta(mint, false, false))
        keys.add(AccountMeta(owner, false, true))
        keys.add(AccountMeta(SYSVAR_RENT_PUBKEY, false, false))
        val buffer = ByteBuffer.allocate(1)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(INITIALIZE_METHOD_ID.toByte())
        return createTransactionInstruction(
            PROGRAM_ID,
            keys,
            buffer.array()
        )
    }

    fun closeAccount(
        source: PublicKey?,
        destination: PublicKey?,
        owner: PublicKey?
    ): TransactionInstruction {
        val keys: MutableList<AccountMeta> = ArrayList()
        keys.add(AccountMeta(source!!, false, true))
        keys.add(AccountMeta(destination!!, false, true))
        keys.add(AccountMeta(owner!!, true, false))
        val buffer = ByteBuffer.allocate(1)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(CLOSE_ACCOUNT_METHOD_ID.toByte())
        return createTransactionInstruction(
            PROGRAM_ID,
            keys,
            buffer.array()
        )
    }

    private fun encodeTransferTokenInstructionData(amount: Long): ByteArray {
        val result = ByteBuffer.allocate(9)
        result.order(ByteOrder.LITTLE_ENDIAN)
        result.put(TRANSFER_METHOD_ID.toByte())
        result.putLong(amount)
        return result.array()
    }

    private fun encodeTransferCheckedTokenInstructionData(amount: Long, decimals: Byte): ByteArray {
        val result = ByteBuffer.allocate(10)
        result.order(ByteOrder.LITTLE_ENDIAN)
        result.put(TRANSFER_CHECKED_METHOD_ID.toByte())
        result.putLong(amount)
        result.put(decimals)
        return result.array()
    }
}