package com.solana.rxsolana.actions

import com.solana.core.HotAccount

class InMemoryAccountStorage(private var account: HotAccount? = null) {

    fun save(account: HotAccount) : Result<Unit> {
        this.account = account
        return Result.success(Unit)
    }

    fun account(): Result<HotAccount> {
        if (account != null){
            return Result.success(account!!)
        }
        return Result.failure(Exception("unauthorized"))
    }

    fun clear(): Result<Unit>{
        account = null
        return Result.success(Unit)
    }
}