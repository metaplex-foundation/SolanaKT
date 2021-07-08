package com.solana.vendor.borshj

@Target(
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldOrder(
    val order: Int
)
