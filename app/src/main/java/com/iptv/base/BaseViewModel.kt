package com.iptv.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorData = MutableSharedFlow<String>()
    val errorData = _errorData.asSharedFlow()

    private fun <T> createData(defaultValue: T? = null): LiveData<T> = MutableLiveData(defaultValue)

    suspend fun setError(message: String) {
        _errorData.emit(message)
    }

    fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

    inner class UseCaseResult<T> {
        val data: LiveData<T> = createData()
        val error: LiveData<String> = createData()
        val state: LiveData<State> = createData(State.INITIAL)


    }

    enum class State {
        INITIAL, LOADING, SUCCESS, FAILURE
    }
}