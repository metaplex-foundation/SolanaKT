package com.solana.core

import java.util.ArrayList
import java.util.HashMap

class AccountKeysList {
    private val accounts: HashMap<String, AccountMeta>
    fun add(accountMeta: AccountMeta) {
        val key = accountMeta.publicKey.toString()
        if (accounts.containsKey(key)) {
            if (!accounts[key]!!.isWritable && accountMeta.isWritable) {
                accounts[key] = accountMeta
            }
        } else {
            accounts[key] = accountMeta
        }
    }

    fun addAll(metas: Collection<AccountMeta>) {
        for (meta in metas) {
            add(meta)
        }
    }

    val list: ArrayList<AccountMeta>
        get() {
            val accountKeysList = ArrayList(accounts.values)
            accountKeysList.sortWith(metaComparator)
            return accountKeysList
        }

    companion object {
        private val metaComparator = java.util.Comparator<AccountMeta> { am1, am2 ->
            val cmpSigner = if (am1.isSigner == am2.isSigner) 0 else if (am1.isSigner) -1 else 1
            if (cmpSigner != 0) {
                return@Comparator cmpSigner
            }
            val cmpkWritable =
                if (am1.isWritable == am2.isWritable) 0 else if (am1.isWritable) -1 else 1
            if (cmpkWritable != 0) {
                cmpkWritable
            } else Integer.compare(cmpSigner, cmpkWritable)
        }
    }

    init {
        accounts = HashMap()
    }
}