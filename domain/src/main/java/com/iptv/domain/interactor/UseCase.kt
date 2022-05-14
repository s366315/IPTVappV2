package com.iptv.domain.interactor

import com.iptv.domain.entities.Result
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

abstract class UseCase<Params, T> where T : Any {

    sealed class Status<T> {
        data class Success<T>(val data: T) : Status<T>()
        class Loading<T> : Status<T>()
        data class Failure<T>(val e: Throwable) : Status<T>()
    }

    var params: Params? = null

    fun params(params: Params? = null): UseCase<Params, T> {
        this.params = params
        return this
    }

    open suspend fun createObservable(
        onSuccess: suspend (Result.Success<T>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    ) {
        createObservable(params, onSuccess, onError)
    }

    abstract suspend fun createObservable(
        params: Params? = null,
        onSuccess: suspend (Result.Success<T>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    )
}