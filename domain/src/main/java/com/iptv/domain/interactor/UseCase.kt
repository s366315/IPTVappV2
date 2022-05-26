package com.iptv.domain.interactor

import com.iptv.domain.entities.Result
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

abstract class UseCase<Params, T> where T : Any {

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

    open suspend fun createObservable(
        onSuccess: suspend (Result.Success<T>) -> Unit
    ) {
        createObservable(params, onSuccess, {})
    }

    abstract suspend fun createObservable(
        params: Params? = null,
        onSuccess: suspend (Result.Success<T>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    )
}