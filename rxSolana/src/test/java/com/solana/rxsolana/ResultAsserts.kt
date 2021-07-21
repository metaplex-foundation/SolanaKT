package com.solana.rxsolana

import org.junit.Assert

fun <A> assertSuccess(result: Result<A>, message: String) {
    result.onSuccess { Assert.assertTrue(true) }
        .onFailure { Assert.fail(message) }
}

fun <A> assertSuccess(result: Result<A>, predicate: (A) -> Boolean, message: String) {
    result.onSuccess { Assert.assertTrue(predicate(it)) }
        .onFailure { Assert.fail(message) }
}

fun <A> assertFailure(result: Result<A>, message: String? = null) {
    result.onSuccess { Assert.fail(message) }
        .onFailure { Assert.assertTrue(true) }
}

