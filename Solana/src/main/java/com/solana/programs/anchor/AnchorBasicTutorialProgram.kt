package com.solana.programs.anchor

import com.solana.core.Account
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import com.solana.programs.Program
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Implements the "initialize" call from Anchor's basic-0 tutorial.
 */
object AnchorBasicTutorialProgram : Program() {
    // Testnet address of basic-0 = EkEwddr34fqnv2SJREPynyC335PE32PAfjY4LVW5bTJS (has a method called initialize)
    private val PROGRAM_ID = PublicKey("EkEwddr34fqnv2SJREPynyC335PE32PAfjY4LVW5bTJS")
    private const val FUNCTION_NAMESPACE = "global::initialize"

    /**
     * Calls basic_0::initialize
     *
     * @param caller account signing the transaction
     * @return tx id
     */
    fun initialize(caller: Account): TransactionInstruction {
        val keys: MutableList<AccountMeta> = ArrayList()
        keys.add(AccountMeta(caller.publicKey, true, false))
        val transactionData = encodeInitializeData()
        return createTransactionInstruction(
            PROGRAM_ID,
            keys,
            transactionData
        )
    }

    /**
     * Encodes the "global::initialize" sighash
     * @return byte array containing sighash for "global::initialize"
     */
    private fun encodeInitializeData(): ByteArray? {
        var digest: MessageDigest? = null
        var encodedHash: ByteArray? = null
        val sigHashStart = 0
        val sigHashEnd = 8
        try {
            digest = MessageDigest.getInstance("SHA-256")
            encodedHash = Arrays.copyOfRange(
                digest.digest(
                    FUNCTION_NAMESPACE.toByteArray(
                        StandardCharsets.UTF_8
                    )
                ),
                sigHashStart,
                sigHashEnd
            )
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return encodedHash
    }
}