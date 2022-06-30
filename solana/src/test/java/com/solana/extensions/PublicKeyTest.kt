package com.solana.extensions



import com.solana.core.PublicKey
import org.junit.Assert
import org.junit.Test

class PublicKeyTest {
    @Test
    fun testAssociatedAddress() {
        val associatedAddress = PublicKey.associatedTokenAddress(
            PublicKey("5Zzguz4NsSRFxGkHfM4FmsFpGZiCDtY72zH2jzMcqkJx"),
            PublicKey("6AUM4fSvCAxCugrbJPFxTqYFp9r3axYx973yoSyzDYVH")
        )
        Assert.assertEquals(
            "4PsGEFn43xc7ztymrt77XfUE4FespyNm6KuYYmsstz5L",
            associatedAddress.address.toBase58()
        )
    }
}