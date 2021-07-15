package com.solana

import com.solana.core.Account

class InMemoryAccountStorage: SolanaAccountStorage {
    private var _account: Account? = null
    override fun save(account: Account) : Result<Unit> {
        _account = account
        return Result.success(Unit)
    }

    override fun account(): Result<Account> {
        if (_account != null){
            return Result.success(_account!!)
        }
        return Result.failure(Exception("unauthorized"))
    }
    
    override fun clear(): Result<Unit>{
        _account = null
        return Result.success(Unit)
    }
}