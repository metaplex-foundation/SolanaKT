package com.solana.vendor

typealias ResultError = Exception

/**
 * This property is guaranteed to be never null, and is named after the
 * iOS counterpart. You must always use [localizedDescription] to get the error
 * message out of [ResultError].
 */
val ResultError.localizedDescription: String
    get() = this.localizedMessage ?: "Error: $this"

/**
 * Sum type that represent a success case (valid value) or a
 * failure case (exception).
 *
 * @param <A> The inner type of the success case.
 */
sealed class Result<out A, out E : ResultError> {
    /**
     * Attach a callback that will be called for success cases.
     */
    abstract fun onSuccess(f: (A) -> Unit): Result<A, E>

    /**
     * Attach a callback that will be called for failure cases.
     */
    abstract fun onFailure(f: (E) -> Unit): Result<A, E>

    /**
     * Attach a callback that throws the exception for failure cases.
     */
    abstract fun throwOnFailure(): Result<A, E>

    /**
     * Transform the valid case value to another type.
     */
    abstract fun <B> map(f: (A) -> B): Result<B, E>

    /**
     * Transform the failure case value to another type.
     */
    abstract fun <E2 : ResultError> mapError(f: (E) -> E2): Result<A, E2>

    /**
     * Extract the contained value, or throws and error.
     */
    abstract fun getOrThrows(): A

    companion object {
        /**
         * Make a new success case object with the given value.
         */
        fun <A> success(a: A): Result<A, Nothing> {
            return Success(a)
        }

        /**
         * Make a new failure case object containing an exception
         * with the given message.
         */
        fun failure(message: String): Result<Nothing, ResultError> {
            return Failure(ResultError(message))
        }

        /**
         * Make a new failure case object containing an exception
         * with the given exception.
         */
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

/**
 * Internal implementation of the success case. Do not make this public.
 */
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

/**
 * Internal implementation of the failure case. Do not make this public.
 */
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

/**
 * Extract the contained value, or return the default value.
 */
fun <A, E : ResultError> Result<A, E>.getOrDefault(defaultValue: A): A {
    return when (this) {
        is Success -> this.value
        is Failure -> defaultValue
    }
}

/**
 * Gives a chance to recover from the failure state, but allows
 * to confirm the failure if it can't be helped.
 */
fun <A, E : ResultError, E2 : ResultError> Result<A, E>.recover(f: (E) -> Result<A, E2>): Result<A, E2> {
    return when (this) {
        is Success -> Result.success(this.value)
        is Failure -> f(this.exception)
    }
}

/**
 * Transform the valid case value to another instance of another type.
 */
fun <A, B, E : ResultError, E2 : E> Result<A, E>.flatMap(f: (A) -> Result<B, E2>): Result<B, E> {
    return when (this) {
        is Success -> f(this.value)
        is Failure -> Result.failure(this.exception)
    }
}

/**
 * Create a [Result] with custom error from a nullable value.
 * The failure case takes a closure to avoid evaluation in
 * case it is not necessary.
 */
fun <A, E : ResultError> A?.toResult(otherwise: () -> E): Result<A, E> {
    return if (this != null) Result.success(this) else Result.failure(otherwise())
}
