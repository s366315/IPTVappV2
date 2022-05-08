package com.iptv.domain.entities

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val data: T) : Result<T>()
    data class Error(val code: Int, val message: String) : Result<Nothing>()
    class Loading<T: Any> : Result<T>()
}
