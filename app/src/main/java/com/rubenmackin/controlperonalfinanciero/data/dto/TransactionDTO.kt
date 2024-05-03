package com.rubenmackin.controlperonalfinanciero.data.dto

import com.google.firebase.Timestamp

data class TransactionDTO(
    val title: String,
    val amount: Double,
    val date: Timestamp,
)
