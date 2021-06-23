package com.solana.programs;

import com.solana.core.AccountMeta;
import com.solana.core.PublicKey;
import com.solana.core.TransactionInstruction;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Interface for the Memo program, used for writing UTF-8 data into Solana transactions.
 */
public class MemoProgram extends Program {

    public static final PublicKey PROGRAM_ID = new PublicKey("Memo1UhkJRfHyvLMcVucJwxXeuD728EqVDDwQDxFMNo");

    /**
     * Returns a {@link TransactionInstruction} object containing instructions to call the Memo program with the
     * specified memo.
     * @param account signer pubkey
     * @param memo utf-8 string to be written into Solana transaction
     * @return {@link TransactionInstruction} object with memo instruction
     */
    public static TransactionInstruction writeUtf8(PublicKey account, String memo) {
        // Add signer to AccountMeta keys
        final List<AccountMeta> keys = Collections.singletonList(
                new AccountMeta(
                        account,
                        true,
                        false
                )

        );

        // Convert memo string to UTF-8 byte array
        final byte[] memoBytes = memo.getBytes(StandardCharsets.UTF_8);

        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                memoBytes
        );
    }
}
