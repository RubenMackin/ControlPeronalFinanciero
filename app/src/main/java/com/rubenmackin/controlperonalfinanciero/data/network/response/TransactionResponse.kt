package com.rubenmackin.controlperonalfinanciero.data.network.response

import com.google.firebase.Timestamp

data class TransactionResponse (
    val id: String? = null,
    val date: Timestamp? = null,
    val amount: Double? = null,
    val title: String? = null,
)
