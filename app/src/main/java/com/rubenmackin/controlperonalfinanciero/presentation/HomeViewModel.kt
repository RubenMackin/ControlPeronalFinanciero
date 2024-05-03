package com.rubenmackin.controlperonalfinanciero.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.rubenmackin.controlperonalfinanciero.data.dto.TransactionDTO
import com.rubenmackin.controlperonalfinanciero.data.network.DatabaseRepository
import com.rubenmackin.controlperonalfinanciero.domain.model.TransactionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    var _uiState = MutableStateFlow<HomeUiState>(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            databaseRepository.getTransactions().collect { transactions ->

                val totalAmount = transactions.sumOf { it.amount }
                val totalAmountFormatted = String.format("%.2f$", totalAmount)

                _uiState.update {
                    it.copy(
                        transactions = transactions,
                        totalAmount = totalAmountFormatted
                    )
                }
            }
        }
    }

    fun onAddTransactionSelected() {
        _uiState.update {
            it.copy(
                showAddTransactionDialog = true
            )
        }
    }

    fun dismissShowAddTransactionDialog() {
        _uiState.update {
            it.copy(
                showAddTransactionDialog = false
            )
        }
    }

    fun addTransaction(title: String, amount: String, date: Long?) {
        val dto = prepareDTO(title, amount, date)
        if (dto != null) {
            //mandamos a base de datos
            viewModelScope.launch {
                databaseRepository.addTransaction(dto)
            }
        }
        dismissShowAddTransactionDialog()
    }

    private fun prepareDTO(title: String, amount: String, date: Long?): TransactionDTO? {
        if (title.isBlank() || amount.isBlank()) return null
        val timeStamp = if (date != null) {
            val seconds = date / 1000
            val nanoseconds = ((date % 1000) * 1000000).toInt()
            Timestamp(seconds, nanoseconds)
        } else {
            Timestamp.now()
        }

        return try {
            TransactionDTO(
                title = title,
                amount = amount.toDouble(),
                date = timeStamp
            )
        } catch (e: Exception) {
            null
        }

    }

    fun onItemRemove(id: String) {
        databaseRepository.removeTransaction(id)
    }

}

data class HomeUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionModel> = emptyList(),
    val totalAmount: String = "",
    val showAddTransactionDialog: Boolean = false
)