package com.solana.programs

import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction

object AssociatedTokenProgram : Program() {
    val SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID = PublicKey("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL")

    fun createAssociatedTokenAccountInstruction(
        associatedProgramId: PublicKey = SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
        programId: PublicKey = TokenProgram.PROGRAM_ID,
        mint: PublicKey,
        associatedAccount: PublicKey,
        owner: PublicKey,
        payer: PublicKey
    ): TransactionInstruction {

        val keys = listOf(
            AccountMeta(payer, true, true),
            AccountMeta(associatedAccount, false, true),
            AccountMeta(owner, false, false),
            AccountMeta(mint,  false, false),
            AccountMeta(SystemProgram.PROGRAM_ID, false, false),
            AccountMeta(programId, false, false),
            AccountMeta(TokenProgram.SYSVAR_RENT_PUBKEY, false,  false)
        )

        return TransactionInstruction(
            keys = keys,
            programId = associatedProgramId,
            data = byteArrayOf(),
        )
    }
}
