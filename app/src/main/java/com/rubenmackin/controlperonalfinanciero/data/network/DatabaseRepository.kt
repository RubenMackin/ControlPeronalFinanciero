package com.rubenmackin.controlperonalfinanciero.data.network

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.rubenmackin.controlperonalfinanciero.data.dto.TransactionDTO
import com.rubenmackin.controlperonalfinanciero.data.network.response.TransactionResponse
import com.rubenmackin.controlperonalfinanciero.domain.model.TransactionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val db: FirebaseFirestore) {

    companion object {
        const val COLLECTION_NAME = "rubenmackin"
        const val FIELD_DATE = "date"
    }

    fun getTransactions(): Flow<List<TransactionModel>> {
        return db.collection(COLLECTION_NAME).orderBy(FIELD_DATE, Query.Direction.DESCENDING)
            .snapshots().map { qs ->
                qs.toObjects(TransactionResponse::class.java).mapNotNull { transactionResponse ->
                    transactionToDomain(transactionResponse)
                }

            }
    }

    private fun transactionToDomain(transactionResponse: TransactionResponse): TransactionModel? {
        if (transactionResponse.date == null
            || transactionResponse.amount == null
            || transactionResponse.title == null
            || transactionResponse.id == null
        ) return null

        val date = timestampToString(transactionResponse.date) ?: return null

        return TransactionModel(
            id = transactionResponse.id,
            title = transactionResponse.title,
            amount = transactionResponse.amount,
            date = date,
        )
    }

    private fun timestampToString(timestamp: Timestamp?): String? {
        timestamp ?: return null
        return try {
            val date = timestamp.toDate()
            val sdf = SimpleDateFormat("EEEE dd MMMM", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            return null
        }
    }

    fun addTransaction(dto: TransactionDTO) {
        val customId = getCustomId()
        val model = hashMapOf(
            "id" to customId,
            "title" to dto.title,
            "amount" to dto.amount,
            "date" to dto.date
        )

        db.collection(COLLECTION_NAME).document(customId).set(model)
    }

    private fun getCustomId(): String {
        return Date().time.toString()
    }

    fun removeTransaction(id: String) {
        db.collection(COLLECTION_NAME).document(id).delete()
    }
}