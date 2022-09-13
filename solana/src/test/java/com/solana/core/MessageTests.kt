package com.solana.core

import com.solana.programs.SystemProgram.transfer
import org.bitcoinj.core.Base58
import org.junit.Assert
import org.junit.Test
import java.util.*

class MessageTest {
    @Test
    fun serializeMessage() {
        val fromPublicKey = PublicKey("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo")
        val toPublickKey = PublicKey("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5")
        val lamports = 3000
        val signer = HotAccount(
            Base58
                .decode("4Z7cXSyeFR8wNGMVXUE1TwtKn5D5Vu7FzEv69dokLv7KrQk7h6pu4LF8ZRR9yQBhc7uSM6RTTZtU1fmaxiNrxXrs")
        )

        val transaction: Transaction = TransactionBuilder()
            .addInstruction(transfer(fromPublicKey, toPublickKey, lamports.toLong()))
            .setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            .setSigners(listOf(signer))
            .build()
        val transactionInstructionSerializer = transaction.serialize()

        Assert.assertEquals(
            listOf<Byte>(1, 39, 67, 117, 96, 90, 41, 117, 81, 3, -18, -97, -107, 81, 98, 102, -119, 34, 115, -40, 10, -45, -22, -11, 37, 104, 48, 26, 15, -58, 36, 9, 11, 74, -73, -104, -84, 97, 120, -83, -76, -62, 92, -46, 111, -54, -21, 122, 43, -88, 110, -92, -101, 102, -17, 97, -77, -76, -74, -42, 54, 100, 69, -43, 14, 1, 0, 1, 3, 6, 26, -39, -48, 83, -121, 21, 72, 83, 126, -34, 62, 38, 24, 73, -93, -33, -73, -3, 2, -6, -68, 117, -78, 35, -56, -28, 106, -37, -123, 61, 12, -21, 122, -68, -48, -40, 117, -21, -62, 109, -95, -79, -127, -93, 51, -101, 62, -14, -93, 22, -107, -69, 122, -67, -68, 103, -126, 115, -68, -83, -51, -27, -86, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -53, -30, -120, -63, -103, -108, -16, 50, -26, 98, 9, 79, -35, -77, -13, -82, 90, 67, 104, -87, 6, -69, -91, 72, 36, -100, 19, 57, -124, 38, 69, -11, 1, 2, 2, 0, 1, 12, 2, 0, 0, 0, -72, 11, 0, 0, 0, 0, 0, 0),
            transactionInstructionSerializer.toList()
        )
    }
}
