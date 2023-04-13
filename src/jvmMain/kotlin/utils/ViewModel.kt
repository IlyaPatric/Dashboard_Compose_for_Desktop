package utils

import kotlinx.coroutines.*

open class ViewModel() {

    val viewModelScope = MainScope() + CoroutineName("$javaClass") + Dispatchers.IO

    fun close() {
        viewModelScope.cancel(EndViewModel())
    }

    inner class EndViewModel(): CancellationException()
}