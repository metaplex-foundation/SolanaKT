package com.solana.vendor.borshj

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PropertyOrdinal(
    val order: Int
)
