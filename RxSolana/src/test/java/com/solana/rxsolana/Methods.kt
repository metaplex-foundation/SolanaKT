package com.solana.rxsolana

import com.solana.rxsolana.api.getRecentBlockhash
import com.solana.Solana
import com.solana.networking.NetworkingRouter
import com.solana.networking.RPCEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert
import org.junit.Test

class Methods {
    @Test
    fun TestGetRecentBlockhash() {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(logging).build()
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana, ))
        val result = solana.api.getRecentBlockhash().blockingGet()
        Assert.assertNotNull(result.value)
    }
}