package com.niks.googlebooksapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niks.googlebooksapp.datasourse.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class BooksListingActivityViewModel : ViewModel() {

    val uiStateFlow = MutableSharedFlow<BooksListingUIState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    //TODO - Integrate di
    private val booksRepository = BooksRepository()

    init {
        uiStateFlow.tryEmit(BooksListingUIState(isLoading = true))
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val booksVoList = booksRepository.getBooks()
                uiStateFlow.emit(
                    BooksListingUIState(
                        isError = false,
                        isLoading = false,
                        booksList = booksVoList
                    )
                )
            } catch (e: Exception) {
                uiStateFlow.tryEmit(
                    uiStateFlow.last().copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = "Something went wrong"
                    )
                )
            }

        }
    }

    //Don't want to expose mutable shared flow
    fun listenUiState(): SharedFlow<BooksListingUIState> {
        return uiStateFlow.asSharedFlow()
    }

}

data class BooksListingUIState(
    val isLoading: Boolean,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val booksList: List<BookDetailsVo> = listOf()
)