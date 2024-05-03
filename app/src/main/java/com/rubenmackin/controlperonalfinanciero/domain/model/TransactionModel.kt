package com.rubenmackin.controlperonalfinanciero.domain.model

data class TransactionModel(
    val id: String,
    val date: String,
    val amount: Double,
    val title: String,
)
