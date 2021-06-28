package com.solana.vendor

sealed class Retry(open val exception: ResultError) : ResultError() {
    data class retry(override val exception: ResultError) : Retry(exception)
    data class doNotRetry(override val exception: ResultError) : Retry(exception)
}

class Cont<out A>(val run: ((A) -> Unit) -> Unit) {

    fun <B> map(f: (A) -> B): Cont<B> {
        return Cont { cb ->
            this.run { a -> cb(f(a)) }
        }
    }

    fun <B> flatMap(f: (A) -> Cont<B>): Cont<B> {
        return Cont { cb ->
            this.run { a -> f(a).run(cb) }
        }
    }

    fun <B> then(f: () -> Cont<B>): Cont<B> {
        return Cont { cb ->
            this.run { f().run(cb) }
        }
    }

    companion object {
        fun <A> pure(a: A): Cont<A> = Cont { cb -> cb(a) }
    }
}

class ContResult<out A, out E : ResultError>(val cont: Cont<Result<A, E>>) {

    constructor(f: ((Result<A, E>) -> Unit) -> Unit) : this(Cont(f))

    fun <B> map(f: (A) -> B): ContResult<B, E> {
        return ContResult(cont.map { result -> result.map(f) })
    }

    fun <E2 : ResultError> mapError(f: (E) -> E2): ContResult<A, E2> {
        return ContResult(cont.map { result -> result.mapError(f) })
    }

    fun onSuccess(action: (A) -> Unit): ContResult<A, E> {
        return ContResult(cont.flatMap {
            it.onSuccess(action)
            Cont.pure(it)
        })
    }

    fun run(action: (Result<A, E>) -> Unit) {
        cont.run(action)
    }

    companion object {
        fun <A, E : ResultError> pure(r: Result<A, E>): ContResult<A, E> = ContResult(Cont { cb -> cb(r) })
        fun <A> success(a: A): ContResult<A, Nothing> = ContResult(Cont { cb -> cb(Result.success(a)) })
        fun <E : ResultError> failure(e: E): ContResult<Nothing, E> = ContResult(Cont { cb -> cb(Result.failure(e)) })

        fun <A> retry(attempts: Int, operation: () -> ContResult<A, Retry>): ContResult<A, ResultError> {
            return operation().recover {
                when (it) {
                    is Retry.retry -> if (attempts > 0) retry(
                        attempts - 1,
                        operation
                    ) else failure(it.exception)
                    is Retry.doNotRetry -> failure(it.exception)
                }
            }
        }

        fun <A, B, C, E : ResultError> map2(ra: ContResult<A, E>, rb: ContResult<B, E>, f: (A, B) -> C): ContResult<C, E> {
            return ra.flatMap { a -> rb.map { b -> f(a, b) } }
        }

        fun <A, B, C, E : ResultError> flatMap2(ra: ContResult<A, E>, rb: ContResult<B, E>, f: (A, B) -> ContResult<C, E>): ContResult<C, E> {
            return ra.flatMap { a -> rb.flatMap { b -> f(a, b) } }
        }
    }
}

fun <A, B, E : ResultError> ContResult<A, E>.flatMap(f: (A) -> ContResult<B, E>): ContResult<B, E> {
    return ContResult(cont.flatMap { result ->
        Cont<Result<B, E>> { cb ->
            result.onSuccess { f(it).cont.run(cb) }
                .onFailure { cb(Result.failure(it)) }
        }
    })
}

fun <A, E : ResultError, E2 : ResultError> ContResult<A, E>.recover(f: (E) -> ContResult<A, E2>): ContResult<A, E2> {
    return ContResult(cont.flatMap { result ->
        Cont<Result<A, E2>> { cb ->
            result.onSuccess { cb(Result.success(it)) }
                .onFailure { f(it).cont.run(cb) }
        }
    })
}