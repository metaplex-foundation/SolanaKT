package com.solana.core;

import java.util.List;

public class TransactionInstruction {

    private PublicKey programId;

    private List<AccountMeta> keys;

    private byte[] data;

    public TransactionInstruction(PublicKey programId, List<AccountMeta> keys, byte[] data) {
        this.programId = programId;
        this.keys = keys;
        this.data = data;
    }

    public PublicKey getProgramId() {
        return programId;
    }

    public void setProgramId(PublicKey programId) {
        this.programId = programId;
    }

    public List<AccountMeta> getKeys() {
        return keys;
    }

    public void setKeys(List<AccountMeta> keys) {
        this.keys = keys;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
