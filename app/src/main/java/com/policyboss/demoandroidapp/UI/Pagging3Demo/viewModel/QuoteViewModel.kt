package com.policyboss.demoandroidapp.UI.Pagging3Demo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.policyboss.demoandroidapp.UI.Pagging3Demo.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(private val repository: QuoteRepository) : ViewModel() {
    val list = repository.getQuotes().cachedIn(viewModelScope)
}