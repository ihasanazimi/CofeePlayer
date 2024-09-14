package ir.ha.meproject.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseComposedViewModel<T> : ViewModel() {

    open val TAG = this::class.java.simpleName
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _data = MutableStateFlow<T?>(null)
    val data: StateFlow<T?> = _data

    protected fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    protected fun setError(message: String) {
        _error.value = message
    }

    protected fun setData(newData: T) {
        _data.value = newData
    }
}


abstract class BaseViewModel : ViewModel() {

    open val TAG = this::class.java.simpleName
}
