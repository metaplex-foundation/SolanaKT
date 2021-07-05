package com.solana.models.Buffer

import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.BorshInput
import com.solana.vendor.borshj.BorshOutput
import com.solana.vendor.borshj.BorshRule

class EmptyInfo: BorshCodable

class EmptyInfoRule(
    override val clazz: Class<EmptyInfo> = EmptyInfo::class.java
) : BorshRule<EmptyInfo> {
    override fun read(input: BorshInput): EmptyInfo? {
        return EmptyInfo()
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        return output.writeNothing()
    }
}