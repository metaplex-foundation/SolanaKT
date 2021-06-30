package com.solana.vendor

import java.lang.Exception

typealias ResultError = Exception

sealed class Result<out A, out E : ResultError> {

    abstract fun onSuccess(f: (A) -> Unit): Result<A, E>

    abstract fun onFailure(f: (E) -> Unit): Result<A, E>


    abstract fun throwOnFailure(): Result<A, E>


    abstract fun <B> map(f: (A) -> B): Result<B, E>

    abstract fun <E2 : ResultError> mapError(f: (E) -> E2): Result<A, E2>


    abstract fun getOrThrows(): A

    companion object {

        fun <A> success(a: A): Result<A, Nothing> {
            return Success(a)
        }

        fun failure(message: String): Result<Nothing, ResultError> {
            return Failure(ResultError(message))
        }


        fun <E : ResultError> failure(exception: E): Result<Nothing, E> {
            return Failure(exception)
        }

        fun <A> failable(f: () -> A): Result<A, ResultError> {
            return try {
                success(f())
            } catch (e: Exception) {
                failure(e.message ?: "Unknown error")
            }
        }

        fun <A, B, C, E : ResultError> map2(ra: Result<A, E>, rb: Result<B, E>, f: (A, B) -> C): Result<C, E> {
            return ra.flatMap { a -> rb.map { b -> f(a, b) } }
        }

        fun <A, B, C, D, E : ResultError> map3(ra: Result<A, E>, rb: Result<B, E>, rc: Result<C, E>, f: (A, B, C) -> D): Result<D, E> {
            return ra.flatMap { a -> rb.flatMap { b -> rc.map { c -> f(a, b, c) } } }
        }
    }
}

private class Success<A, E : ResultError>(val value: A) : Result<A, E>() {
    override fun <B> map(f: (A) -> B): Result<B, E> {
        return Success(f(value))
    }

    override fun <E2 : ResultError> mapError(f: (E) -> E2): Result<A, E2> {
        return Success(value)
    }

    override fun onSuccess(f: (A) -> Unit): Result<A, E> {
        f(this.value)
        return this
    }

    override fun onFailure(f: (E) -> Unit): Result<A, E> { // nothing to do
        return this
    }

    override fun throwOnFailure(): Result<A, E> { // nothing to do
        return this
    }

    override fun getOrThrows(): A {
        return value
    }
}

private class Failure<A, E : ResultError> internal constructor(e: E) : Result<A, E>() {
    val exception: E = e

    override fun <B> map(f: (A) -> B): Result<B, E> {
        return Failure(exception)
    }

    override fun <E2 : ResultError> mapError(f: (E) -> E2): Result<A, E2> {
        return Failure(f(exception))
    }

    override fun onSuccess(f: (A) -> Unit): Result<A, E> {
        return this
    }

    override fun onFailure(f: (E) -> Unit): Result<A, E> {
        f(this.exception)
        return this
    }

    override fun throwOnFailure(): Result<A, E> {
        throw exception
    }

    override fun getOrThrows(): Nothing {
        throw exception
    }
}

fun <A, E : ResultError> Result<A, E>.getOrDefault(defaultValue: A): A {
    return when (this) {
        is Success -> this.value
        is Failure -> defaultValue
    }
}

fun <A, E : ResultError, E2 : ResultError> Result<A, E>.recover(f: (E) -> Result<A, E2>): Result<A, E2> {
    return when (this) {
        is Success -> Result.success(this.value)
        is Failure -> f(this.exception)
    }
}

fun <A, B, E : ResultError, E2 : E> Result<A, E>.flatMap(f: (A) -> Result<B, E2>): Result<B, E> {
    return when (this) {
        is Success -> f(this.value)
        is Failure -> Result.failure(this.exception)
    }
}

fun <A, E : ResultError> A?.toResult(otherwise: () -> E): Result<A, E> {
    return if (this != null) Result.success(this) else Result.failure(otherwise())
}

