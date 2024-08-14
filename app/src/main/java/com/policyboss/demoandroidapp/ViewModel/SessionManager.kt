package com.policyboss.demoandroidapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionManager @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _sessionTimeout = MutableSharedFlow<Unit>()
    val sessionTimeout = _sessionTimeout.asSharedFlow()
    private var logoutJob: Job? = null

    init {
        observeAppLifecycle()
    }

    private fun observeAppLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                resetSession()
            }

            override fun onStop(owner: LifecycleOwner) {
                logoutJob?.cancel()
            }
        })
    }

    fun resetSession() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            delay(SESSION_TIMEOUT)
            _sessionTimeout.emit(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()
        logoutJob?.cancel()
    }

    companion object {
        private const val SESSION_TIMEOUT = 1 * 60 * 1000L // 10 minutes in milliseconds
    }
}