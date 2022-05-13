package com.iptv.base

import androidx.lifecycle.*
import com.iptv.domain.interactor.UseCase
import kotlinx.coroutines.flow.*

abstract class BaseViewModel : ViewModel() {

    private val _loadingState = MutableStateFlow<Boolean>(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorData = MutableSharedFlow<String>()
    val errorData = _errorData.asSharedFlow()

    private fun <T> createData(defaultValue: T? = null): LiveData<T> = MutableLiveData(defaultValue)

    suspend fun setError(message: String) {
        _errorData.emit(message)
    }

    suspend fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

    inner class UseCaseResult<T> {
        val data: LiveData<T> = createData()
        val error: LiveData<String> = createData()
        val state: LiveData<State> = createData(State.INITIAL)


    }

    protected fun <T : Any, Params> UseCase<Params, T>.subscribe() {

    }

    enum class State {
        INITIAL, LOADING, SUCCESS, FAILURE
    }
}