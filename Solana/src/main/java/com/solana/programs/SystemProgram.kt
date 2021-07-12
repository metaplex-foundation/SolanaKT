package com.solana.programs

import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import org.bitcoinj.core.Utils
import java.util.*

object SystemProgram : Program() {
    val PROGRAM_ID = PublicKey("11111111111111111111111111111111")
    const val PROGRAM_INDEX_CREATE_ACCOUNT = 0
    const val PROGRAM_INDEX_TRANSFER = 2
    @JvmStatic
    fun transfer(
        fromPublicKey: PublicKey,
        toPublickKey: PublicKey,
        lamports: Long
    ): TransactionInstruction {
        val keys = ArrayList<AccountMeta>()
        keys.add(AccountMeta(fromPublicKey, true, true))
        keys.add(AccountMeta(toPublickKey, false, true))

        // 4 byte instruction index + 8 bytes lamports
        val data = ByteArray(4 + 8)
        Utils.uint32ToByteArrayLE(PROGRAM_INDEX_TRANSFER.toLong(), data, 0)
        Utils.int64ToByteArrayLE(lamports, data, 4)
        return createTransactionInstruction(PROGRAM_ID, keys, data)
    }

    fun createAccount(
        fromPublicKey: PublicKey, newAccountPublickey: PublicKey,
        lamports: Long, space: Long, programId: PublicKey
    ): TransactionInstruction {
        val keys = ArrayList<AccountMeta>()
        keys.add(AccountMeta(fromPublicKey, true, true))
        keys.add(AccountMeta(newAccountPublickey, true, true))
        val data = ByteArray(4 + 8 + 8 + 32)
        Utils.uint32ToByteArrayLE(PROGRAM_INDEX_CREATE_ACCOUNT.toLong(), data, 0)
        Utils.int64ToByteArrayLE(lamports, data, 4)
        Utils.int64ToByteArrayLE(space, data, 12)
        System.arraycopy(programId.toByteArray(), 0, data, 20, 32)
        return createTransactionInstruction(PROGRAM_ID, keys, data)
    }
}